package com.nuno.app.core.network

import android.util.Log
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth for login/register/refresh endpoints
        val url = originalRequest.url.toString()
        if (url.contains("/auth/login") ||
            url.contains("/auth/register") ||
            url.contains("/auth/refresh")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { tokenManager.getAccessToken() }

        val requestWithToken = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(requestWithToken)

        // If token expired (401), try to refresh and retry
        if (response.code == 401) {
            Log.d(TAG, "Token expired, attempting refresh...")
            response.close()

            val refreshed = runBlocking { refreshToken(chain) }

            if (refreshed) {
                val newToken = runBlocking { tokenManager.getAccessToken() }
                Log.d(TAG, "Token refreshed successfully")

                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()

                return chain.proceed(newRequest)
            } else {
                Log.d(TAG, "Token refresh failed")
                // Return the original 401 response
                val newResponse = chain.proceed(requestWithToken)
                return newResponse
            }
        }

        return response
    }

    private suspend fun refreshToken(chain: Interceptor.Chain): Boolean {
        return try {
            val refreshToken = tokenManager.getRefreshToken() ?: return false

            val requestBody = json.encodeToString(
                RefreshTokenRequest.serializer(),
                RefreshTokenRequest(refreshToken)
            ).toRequestBody("application/json".toMediaType())

            val refreshRequest = Request.Builder()
                .url(chain.request().url.newBuilder()
                    .encodedPath("/api/v1/auth/refresh")
                    .build())
                .post(requestBody)
                .build()

            val refreshResponse = chain.proceed(refreshRequest)

            if (refreshResponse.isSuccessful) {
                val responseBody = refreshResponse.body?.string()
                refreshResponse.close()

                if (responseBody != null) {
                    val apiResponse = json.decodeFromString(
                        ApiResponse.serializer(AuthTokens.serializer()),
                        responseBody
                    )

                    if (apiResponse.success && apiResponse.data != null) {
                        tokenManager.saveTokens(
                            accessToken = apiResponse.data.accessToken,
                            refreshToken = apiResponse.data.refreshToken
                        )
                        return true
                    }
                }
            }

            refreshResponse.close()
            false

        } catch (e: Exception) {
            Log.e(TAG, "Refresh token error", e)
            false
        }
    }
}