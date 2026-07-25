package com.nuno.app.features.auth

import android.util.Base64
import android.util.Log
import com.nuno.app.core.common.Resource
import com.nuno.app.core.network.ApiService
import com.nuno.app.core.network.AuthTokens
import com.nuno.app.core.network.LoginRequest
import com.nuno.app.core.network.RefreshTokenRequest
import com.nuno.app.core.network.RegisterRequest
import com.nuno.app.core.network.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    companion object {
        private const val TAG = "AuthRepository"
    }

    // ─────────────────────────────────────────
    // REGISTER
    // ─────────────────────────────────────────

    fun register(
        username: String,
        email: String,
        password: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading)

        try {
            val response = apiService.register(
                RegisterRequest(username, email, password)
            )

            if (response.success && response.data != null) {
                emit(Resource.Success(response.data.message))
            } else {
                emit(Resource.Error(
                    message = response.error?.message ?: "Registration failed.",
                    code = response.error?.code
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "Network error. Please try again."
            ))
        }
    }

    // ─────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────

    fun login(
        email: String,
        password: String
    ): Flow<Resource<AuthTokens>> = flow {
        emit(Resource.Loading)

        try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.success && response.data != null) {
                tokenManager.saveTokens(
                    accessToken = response.data.accessToken,
                    refreshToken = response.data.refreshToken
                )

                // Decode JWT to extract and save userId
                extractAndSaveUserInfo(response.data.accessToken)

                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(
                    message = response.error?.message ?: "Login failed.",
                    code = response.error?.code
                ))
            }
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "Network error. Please try again."
            ))
        }
    }

    // ─────────────────────────────────────────
    // EXTRACT USER INFO FROM JWT
    // ─────────────────────────────────────────

    private suspend fun extractAndSaveUserInfo(accessToken: String) {
        try {
            val parts = accessToken.split(".")
            if (parts.size == 3) {
                val payload = Base64.decode(
                    parts[1],
                    Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
                )
                val payloadStr = String(payload)

                val userId = extractField(payloadStr, "userId")
                val username = extractField(payloadStr, "username")

                if (userId.isNotEmpty() && username.isNotEmpty()) {
                    tokenManager.saveUserInfo(userId, username)
                    Log.d(TAG, "Saved user info - userId: $userId, username: $username")
                } else {
                    Log.e(TAG, "Failed to extract userId or username from JWT")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode JWT", e)
        }
    }

    private fun extractField(json: String, field: String): String {
        val marker = "\"$field\":\""
        val startIdx = json.indexOf(marker)
        if (startIdx == -1) return ""
        val valueStart = startIdx + marker.length
        val valueEnd = json.indexOf("\"", valueStart)
        if (valueEnd == -1) return ""
        return json.substring(valueStart, valueEnd)
    }

    // ─────────────────────────────────────────
    // ENSURE USER INFO LOADED (for existing sessions)
    // ─────────────────────────────────────────

    suspend fun ensureUserInfoLoaded() {
        val existingUserId = tokenManager.getUserId()
        if (existingUserId != null) {
            Log.d(TAG, "User info already loaded: $existingUserId")
            return
        }

        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {
            Log.d(TAG, "Extracting user info from existing token")
            extractAndSaveUserInfo(accessToken)
        } else {
            Log.w(TAG, "No access token available to extract user info")
        }
    }

    // ─────────────────────────────────────────
    // LOGOUT
    // ─────────────────────────────────────────

    fun logout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        try {
            apiService.logout()
        } catch (e: Exception) {
            // Ignore errors on logout
        } finally {
            tokenManager.clearAll()
            emit(Resource.Success(Unit))
        }
    }

    // ─────────────────────────────────────────
    // REFRESH TOKEN
    // ─────────────────────────────────────────

    suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken = tokenManager.getRefreshToken() ?: return false
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken))

            if (response.success && response.data != null) {
                tokenManager.saveTokens(
                    accessToken = response.data.accessToken,
                    refreshToken = response.data.refreshToken
                )
                extractAndSaveUserInfo(response.data.accessToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // ─────────────────────────────────────────
    // IS LOGGED IN
    // ─────────────────────────────────────────

    suspend fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}