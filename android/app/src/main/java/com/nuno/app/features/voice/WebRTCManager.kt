package com.nuno.app.features.voice

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import org.webrtc.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebRTCManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val TAG = "WebRTCManager"
    }

    private val eglBase: EglBase = EglBase.create()
    private var peerConnectionFactory: PeerConnectionFactory? = null
    private val peerConnections = mutableMapOf<String, PeerConnection>()
    private val pendingIceCandidates = mutableMapOf<String, MutableList<IceCandidate>>()
    private var localAudioTrack: AudioTrack? = null
    private var isMuted = false
    private var isInitialized = false
    private val mutedPlayers = mutableSetOf<String>()
    private var isSpeakerMuted = false
    private var dynamicIceServers: List<PeerConnection.IceServer> = emptyList()
    private var audioManager: AudioManager? = null

    private var onIceCandidateCallback: ((targetUserId: String, candidate: IceCandidate) -> Unit)? = null
    private var onRemoteStreamCallback: ((userId: String, stream: MediaStream) -> Unit)? = null

    fun initialize() {
        if (isInitialized) return

        try {
            // Setup audio manager FIRST
            setupAudioManager()

            val initOptions = PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(false)
                .createInitializationOptions()

            PeerConnectionFactory.initialize(initOptions)

            val encoderFactory = DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true)
            val decoderFactory = DefaultVideoDecoderFactory(eglBase.eglBaseContext)

            peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(PeerConnectionFactory.Options())
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .createPeerConnectionFactory()

            createLocalAudioTrack()
            isInitialized = true

            Log.d(TAG, "WebRTC initialized with proper audio routing")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize WebRTC", e)
        }
    }

    private fun setupAudioManager() {
        try {
            audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            audioManager?.let { am ->
                am.mode = AudioManager.MODE_IN_COMMUNICATION

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val devices = am.availableCommunicationDevices
                    val speakerDevice = devices.find { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
                    if (speakerDevice != null) {
                        val result = am.setCommunicationDevice(speakerDevice)
                        Log.d(TAG, "Android 12+ setCommunicationDevice speaker result: $result")
                    } else {
                        @Suppress("DEPRECATION")
                        am.isSpeakerphoneOn = true
                    }
                } else {
                    @Suppress("DEPRECATION")
                    am.isSpeakerphoneOn = true
                }

                val maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
                am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, maxVoice, 0)

                val maxMusic = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                am.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusic, 0)

                Log.d(TAG, "Audio manager configured for Android ${Build.VERSION.SDK_INT}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up audio manager", e)
        }
    }

    fun setSpeakerphone(enabled: Boolean) {
        try {
            audioManager?.let { am ->
                if (enabled) {
                    am.isBluetoothScoOn = false
                    try { am.stopBluetoothSco() } catch (e: Exception) {}
                    am.isSpeakerphoneOn = true
                } else {
                    // Check if Bluetooth is available
                    if (am.isBluetoothScoAvailableOffCall) {
                        try {
                            am.startBluetoothSco()
                            am.isBluetoothScoOn = true
                            am.isSpeakerphoneOn = false
                        } catch (e: Exception) {
                            am.isSpeakerphoneOn = false
                        }
                    } else {
                        am.isSpeakerphoneOn = false
                    }
                }
                Log.d(TAG, "Speakerphone: $enabled, bluetooth: ${am.isBluetoothScoOn}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting speakerphone", e)
        }
    }

    private fun createLocalAudioTrack() {
        try {
            // Simple constraints - let the device handle audio processing
            val audioConstraints = MediaConstraints()

            val audioSource = peerConnectionFactory?.createAudioSource(audioConstraints)
            localAudioTrack = peerConnectionFactory?.createAudioTrack("audio_local", audioSource)
            localAudioTrack?.setEnabled(true)

            Log.d(TAG, "Audio track created")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create audio track", e)
        }
    }

    fun setIceServers(servers: List<PeerConnection.IceServer>) {
        dynamicIceServers = servers
    }

    fun getOrCreatePeerConnection(userId: String): PeerConnection? {
        if (!isInitialized) initialize()

        val existing = peerConnections[userId]
        if (existing != null && existing.signalingState() != PeerConnection.SignalingState.CLOSED) {
            return existing
        }

        return createPeerConnection(userId)
    }

    fun createPeerConnection(userId: String): PeerConnection? {
        if (!isInitialized) initialize()

        // Close existing connection if any
        peerConnections[userId]?.let {
            try { it.close() } catch (e: Exception) {}
            peerConnections.remove(userId)
        }

        val iceServers = if (dynamicIceServers.isNotEmpty()) {
            dynamicIceServers
        } else {
            listOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
                PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
                PeerConnection.IceServer.builder("stun:stun2.l.google.com:19302").createIceServer()
            )
        }

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            iceTransportsType = PeerConnection.IceTransportsType.ALL
            continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
        }

        val observer = object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {
                Log.d(TAG, "ICE candidate for $userId")
                onIceCandidateCallback?.invoke(userId, candidate)
            }

            override fun onAddStream(stream: MediaStream) {
                Log.d(TAG, "Stream from $userId, audio tracks: ${stream.audioTracks.size}")
                stream.audioTracks.forEach { track ->
                    track.setEnabled(true)
                    track.setVolume(10.0)
                    Log.d(TAG, "Enabled remote audio track from $userId with volume 10.0")
                }
                onRemoteStreamCallback?.invoke(userId, stream)
            }

            override fun onTrack(transceiver: RtpTransceiver) {
                Log.d(TAG, "Track received from $userId: ${transceiver.mediaType}")
                if (transceiver.mediaType == MediaStreamTrack.MediaType.MEDIA_TYPE_AUDIO) {
                    val track = transceiver.receiver.track() as? AudioTrack
                    track?.setEnabled(true)
                    track?.setVolume(10.0)
                    Log.d(TAG, "Enabled remote audio track transceiver from $userId with volume 10.0")
                }
            }

            override fun onSignalingChange(state: PeerConnection.SignalingState) {
                Log.d(TAG, "Signaling $userId: $state")
            }

            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
                Log.d(TAG, "ICE $userId: $state")
                when (state) {
                    PeerConnection.IceConnectionState.CONNECTED -> {
                        Log.d(TAG, "Voice CONNECTED with $userId")
                        // Ensure audio is routing correctly
                        setupAudioManager()
                    }
                    PeerConnection.IceConnectionState.DISCONNECTED,
                    PeerConnection.IceConnectionState.FAILED -> {
                        Log.w(TAG, "Voice DISCONNECTED from $userId")
                    }
                    else -> {}
                }
            }

            override fun onIceConnectionReceivingChange(receiving: Boolean) {}
            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {
                Log.d(TAG, "ICE gathering $userId: $state")
            }
            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
            override fun onRemoveStream(stream: MediaStream?) {}
            override fun onDataChannel(dataChannel: DataChannel?) {}
            override fun onRenegotiationNeeded() {}
            override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {
                receiver?.track()?.setEnabled(true)
                Log.d(TAG, "Track added from remote")
            }
        }

        val peerConnection = peerConnectionFactory?.createPeerConnection(rtcConfig, observer)

        // Add local audio track using addTrack (Unified Plan compatible)
        localAudioTrack?.let { track ->
            peerConnection?.addTrack(track, listOf("local_audio"))
        }

        if (peerConnection != null) {
            peerConnections[userId] = peerConnection
            Log.d(TAG, "Peer connection created for $userId")
        }

        return peerConnection
    }

    fun createOffer(userId: String, onOfferCreated: (sdp: SessionDescription) -> Unit) {
        val pc = peerConnections[userId] ?: return

        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"))
        }

        pc.createOffer(object : SdpObserver {
            override fun onCreateSuccess(sdp: SessionDescription) {
                pc.setLocalDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        Log.d(TAG, "Offer created and set for $userId")
                        onOfferCreated(sdp)
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onSetFailure(error: String?) { Log.e(TAG, "Set local desc failed: $error") }
                    override fun onCreateFailure(p0: String?) {}
                }, sdp)
            }
            override fun onSetSuccess() {}
            override fun onSetFailure(p0: String?) {}
            override fun onCreateFailure(error: String?) { Log.e(TAG, "Create offer failed: $error") }
        }, constraints)
    }

    fun createAnswer(userId: String, onAnswerCreated: (sdp: SessionDescription) -> Unit) {
        val pc = peerConnections[userId] ?: return

        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"))
        }

        pc.createAnswer(object : SdpObserver {
            override fun onCreateSuccess(sdp: SessionDescription) {
                pc.setLocalDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        Log.d(TAG, "Answer created and set for $userId")
                        onAnswerCreated(sdp)
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onSetFailure(error: String?) { Log.e(TAG, "Set local desc failed: $error") }
                    override fun onCreateFailure(p0: String?) {}
                }, sdp)
            }
            override fun onSetSuccess() {}
            override fun onSetFailure(p0: String?) {}
            override fun onCreateFailure(error: String?) { Log.e(TAG, "Create answer failed: $error") }
        }, constraints)
    }

    fun setRemoteDescription(userId: String, sdp: SessionDescription) {
        peerConnections[userId]?.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {
                Log.d(TAG, "Remote desc set for $userId")
                pendingIceCandidates[userId]?.let { list ->
                    list.forEach { candidate -> peerConnections[userId]?.addIceCandidate(candidate) }
                    pendingIceCandidates.remove(userId)
                    Log.d(TAG, "Drained ${list.size} pending ICE candidates for $userId")
                }
            }
            override fun onCreateSuccess(p0: SessionDescription?) {}
            override fun onSetFailure(error: String?) { Log.e(TAG, "Set remote desc failed: $error") }
            override fun onCreateFailure(p0: String?) {}
        }, sdp)
    }

    fun addIceCandidate(userId: String, candidate: IceCandidate) {
        val pc = peerConnections[userId]
        if (pc != null && pc.remoteDescription != null) {
            pc.addIceCandidate(candidate)
        } else {
            pendingIceCandidates.getOrPut(userId) { mutableListOf() }.add(candidate)
            Log.d(TAG, "Queued ICE candidate for $userId")
        }
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        localAudioTrack?.setEnabled(!muted)
        Log.d(TAG, "Mic muted: $muted")
    }

    fun isMuted(): Boolean = isMuted

    fun mutePlayer(userId: String) {
        mutedPlayers.add(userId)
        peerConnections[userId]?.receivers?.forEach { it.track()?.setEnabled(false) }
    }

    fun unmutePlayer(userId: String) {
        mutedPlayers.remove(userId)
        peerConnections[userId]?.receivers?.forEach { it.track()?.setEnabled(true) }
    }

    fun isPlayerMuted(userId: String): Boolean = mutedPlayers.contains(userId)

    fun setSpeakerMuted(muted: Boolean) {
        isSpeakerMuted = muted
        if (muted) {
            peerConnections.values.forEach { pc ->
                pc.receivers.forEach { it.track()?.setEnabled(false) }
            }
        } else {
            peerConnections.values.forEach { pc ->
                pc.receivers.forEach { it.track()?.setEnabled(true) }
            }
        }
        setSpeakerphone(!muted)
    }

    fun isSpeakerMuted(): Boolean = isSpeakerMuted

    fun setOnIceCandidateCallback(callback: (String, IceCandidate) -> Unit) {
        onIceCandidateCallback = callback
    }

    fun setOnRemoteStreamCallback(callback: (String, MediaStream) -> Unit) {
        onRemoteStreamCallback = callback
    }

    fun closeConnection(userId: String) {
        try { peerConnections[userId]?.close() } catch (e: Exception) {}
        peerConnections.remove(userId)
        pendingIceCandidates.remove(userId)
    }

    fun closeAll() {
        try {
            peerConnections.toMap().values.forEach { pc ->
                try { pc.close() } catch (e: Exception) {}
            }
            peerConnections.clear()
        } catch (e: Exception) {}

        // Reset audio
        try {
            audioManager?.let { am ->
                am.mode = AudioManager.MODE_NORMAL
                am.isSpeakerphoneOn = false
                try { am.stopBluetoothSco() } catch (e: Exception) {}
                am.isBluetoothScoOn = false
            }
        } catch (e: Exception) {}

        localAudioTrack = null
        isInitialized = false
        mutedPlayers.clear()
        isMuted = false
        isSpeakerMuted = false

        Log.d(TAG, "WebRTC cleanup done")
    }
}