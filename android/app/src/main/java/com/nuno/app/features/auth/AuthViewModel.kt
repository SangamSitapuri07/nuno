package com.nuno.app.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.common.Resource
import com.nuno.app.core.common.UiState
import com.nuno.app.core.utils.isValidEmail
import com.nuno.app.core.utils.isValidPassword
import com.nuno.app.core.utils.isValidUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val registerState: StateFlow<UiState<Unit>> = _registerState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        checkLoginStatus()
    }

    // ─────────────────────────────────────────
    // CHECK LOGIN STATUS
    // ─────────────────────────────────────────

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val loggedIn = authRepository.isLoggedIn()
            if (loggedIn) {
                // Ensure user info is extracted from token
                authRepository.ensureUserInfoLoaded()
            }
            _isLoggedIn.value = loggedIn
        }
    }

    // ─────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────

    fun login(email: String, password: String) {
        // Validation
        if (email.isBlank()) {
            _loginState.value = UiState.Error("Email is required.")
            return
        }
        if (!email.isValidEmail()) {
            _loginState.value = UiState.Error("Invalid email format.")
            return
        }
        if (password.isBlank()) {
            _loginState.value = UiState.Error("Password is required.")
            return
        }

        authRepository.login(email, password)
            .onEach { result ->
                _loginState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(Unit)
                    is Resource.Error -> UiState.Error(result.message, result.code)
                    is Resource.Idle -> UiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    // ─────────────────────────────────────────
    // REGISTER
    // ─────────────────────────────────────────

    fun register(username: String, email: String, password: String) {
        // Validation
        if (username.isBlank()) {
            _registerState.value = UiState.Error("Username is required.")
            return
        }
        if (!username.isValidUsername()) {
            _registerState.value = UiState.Error(
                "Username must be 3-20 characters (letters, numbers, underscores)."
            )
            return
        }
        if (email.isBlank()) {
            _registerState.value = UiState.Error("Email is required.")
            return
        }
        if (!email.isValidEmail()) {
            _registerState.value = UiState.Error("Invalid email format.")
            return
        }
        if (password.isBlank()) {
            _registerState.value = UiState.Error("Password is required.")
            return
        }
        if (!password.isValidPassword()) {
            _registerState.value = UiState.Error(
                "Password must be 8+ chars with uppercase, lowercase, number, and special character."
            )
            return
        }

        authRepository.register(username, email, password)
            .onEach { result ->
                _registerState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(Unit)
                    is Resource.Error -> UiState.Error(result.message, result.code)
                    is Resource.Idle -> UiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    // ─────────────────────────────────────────
    // LOGOUT
    // ─────────────────────────────────────────

    fun logout() {
        authRepository.logout()
            .onEach {
                _isLoggedIn.value = false
            }
            .launchIn(viewModelScope)
    }

    // ─────────────────────────────────────────
    // RESET STATES
    // ─────────────────────────────────────────

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }

    fun resetRegisterState() {
        _registerState.value = UiState.Idle
    }
}