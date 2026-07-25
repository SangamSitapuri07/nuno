package com.nuno.app.core.audio

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var isEnabled: Boolean = true

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun vibrate(pattern: HapticPattern) {
        if (!isEnabled) return
        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = when (pattern) {
                    HapticPattern.LIGHT -> VibrationEffect.createOneShot(20, 50)
                    HapticPattern.MEDIUM -> VibrationEffect.createOneShot(50, 100)
                    HapticPattern.HEAVY -> VibrationEffect.createOneShot(100, 200)
                    HapticPattern.SUCCESS -> VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50), -1)
                    HapticPattern.ERROR -> VibrationEffect.createWaveform(longArrayOf(0, 100, 100, 100), -1)
                }
                v.vibrate(effect)
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
}

enum class HapticPattern {
    LIGHT,
    MEDIUM,
    HEAVY,
    SUCCESS,
    ERROR
}