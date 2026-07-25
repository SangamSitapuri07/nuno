package com.nuno.app.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.ApiService
import com.nuno.app.core.network.UpdateSettingsRequest
import com.nuno.app.core.network.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val language: String = "en",
    val musicVolume: Int = 80,
    val soundVolume: Int = 80,
    val voiceVolume: Int = 80,
    val pushToTalk: Boolean = false,
    val notifications: Boolean = true,
    val darkMode: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val soundManager: com.nuno.app.core.audio.SoundManager,
    private val hapticManager: com.nuno.app.core.audio.HapticManager
) : ViewModel() {

    private val _settings = MutableStateFlow(SettingsUiState())
    val settings: StateFlow<SettingsUiState> = _settings.asStateFlow()

    private val _saveMessage = MutableStateFlow<String?>(null)
    val saveMessage: StateFlow<String?> = _saveMessage.asStateFlow()

    private var saveJob: Job? = null

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val response = apiService.getSettings()
                if (response.success && response.data != null) {
                    _settings.value = SettingsUiState(
                        language = response.data.language,
                        musicVolume = response.data.musicVolume,
                        soundVolume = response.data.soundVolume,
                        voiceVolume = response.data.voiceVolume,
                        pushToTalk = response.data.pushToTalk,
                        notifications = response.data.notifications,
                        darkMode = response.data.darkMode
                    )
                }
            } catch (e: Exception) {
                // Use defaults
            }
        }
    }

    fun updateMusicVolume(value: Int) {
        _settings.value = _settings.value.copy(musicVolume = value)
        soundManager.setGameVolume(value.toFloat())
        debouncedSave()
    }

    fun updateSoundVolume(value: Int) {
        _settings.value = _settings.value.copy(soundVolume = value)
        soundManager.setGameVolume(value.toFloat())
        debouncedSave()
    }
    fun updateVoiceVolume(value: Int) {
        _settings.value = _settings.value.copy(voiceVolume = value)
        debouncedSave()
    }

    fun updatePushToTalk(value: Boolean) {
        _settings.value = _settings.value.copy(pushToTalk = value)
        saveNow()
    }

    fun updateNotifications(value: Boolean) {
        _settings.value = _settings.value.copy(notifications = value)
        saveNow()
    }

    fun updateDarkMode(value: Boolean) {
        _settings.value = _settings.value.copy(darkMode = value)
        com.nuno.app.core.theme.ThemeManager.setDarkMode(value)
        saveNow()
    }
    fun setHapticsEnabled(enabled: Boolean) {
        hapticManager.setEnabled(enabled)
    }

    private fun debouncedSave() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(1000)
            saveSettings()
        }
    }

    private fun saveNow() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            saveSettings()
        }
    }

    private suspend fun saveSettings() {
        try {
            val current = _settings.value
            val response = apiService.updateSettings(
                UpdateSettingsRequest(
                    language = current.language,
                    musicVolume = current.musicVolume,
                    soundVolume = current.soundVolume,
                    voiceVolume = current.voiceVolume,
                    pushToTalk = current.pushToTalk,
                    notifications = current.notifications,
                    darkMode = current.darkMode
                )
            )
            if (response.success) {
                _saveMessage.value = "Settings saved"
            }
        } catch (e: Exception) {
            _saveMessage.value = "Failed to save"
        }
    }

    fun clearMessage() {
        _saveMessage.value = null
    }
}