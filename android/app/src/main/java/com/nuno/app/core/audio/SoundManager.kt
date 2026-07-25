package com.nuno.app.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val TAG = "SoundManager"
    }

    private var gameVolume: Float = 0.8f
    private var isEnabled: Boolean = true
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 50)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create ToneGenerator", e)
        }
    }

    fun playSound(effect: SoundEffect) {
        if (!isEnabled) return

        try {
            val toneType = when (effect) {
                SoundEffect.CARD_PLAY -> ToneGenerator.TONE_PROP_ACK
                SoundEffect.CARD_DRAW -> ToneGenerator.TONE_PROP_BEEP
                SoundEffect.WIN -> ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD
                SoundEffect.LOSE -> ToneGenerator.TONE_CDMA_CALLDROP_LITE
                SoundEffect.BUTTON_CLICK -> ToneGenerator.TONE_PROP_BEEP2
                SoundEffect.UNO_CALL -> ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE
                SoundEffect.SHUFFLE -> ToneGenerator.TONE_CDMA_PRESSHOLDKEY_LITE
                SoundEffect.SKIP -> ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE
                SoundEffect.REVERSE -> ToneGenerator.TONE_CDMA_ABBR_ALERT
                SoundEffect.DRAW_TWO -> ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT
                SoundEffect.WILD -> ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE
                SoundEffect.TIMER_WARNING -> ToneGenerator.TONE_CDMA_PIP
                SoundEffect.MATCH_FOUND -> ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE
                SoundEffect.TURN_CHANGE -> ToneGenerator.TONE_PROP_BEEP
            }

            val volume = (gameVolume * 100).toInt().coerceIn(0, 100)
            toneGenerator?.startTone(toneType, 200)

            Log.d(TAG, "Played sound: $effect at volume $volume")
        } catch (e: Exception) {
            Log.e(TAG, "Error playing sound", e)
        }
    }

    fun setGameVolume(volume: Float) {
        gameVolume = (volume / 100f).coerceIn(0f, 1f)
        Log.d(TAG, "Game volume set to $gameVolume")
    }

    fun getGameVolume(): Float = gameVolume * 100f

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        Log.d(TAG, "Sound enabled: $enabled")
    }

    fun isEnabled(): Boolean = isEnabled

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}

enum class SoundEffect {
    CARD_PLAY,
    CARD_DRAW,
    WIN,
    LOSE,
    BUTTON_CLICK,
    UNO_CALL,
    SHUFFLE,
    SKIP,
    REVERSE,
    DRAW_TWO,
    WILD,
    TIMER_WARNING,
    MATCH_FOUND,
    TURN_CHANGE
}