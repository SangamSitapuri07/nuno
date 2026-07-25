package com.nuno.app.core.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // AUTH
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<AuthTokens>

    @POST("auth/logout")
    suspend fun logout(): ApiResponse<EmptyResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): ApiResponse<AuthTokens>

    // PROFILE
    @GET("profile")
    suspend fun getProfile(): ApiResponse<UserProfile>

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ApiResponse<UserProfile>

    // SETTINGS
    @GET("settings")
    suspend fun getSettings(): ApiResponse<UserSettings>

    @PUT("settings")
    suspend fun updateSettings(@Body request: UpdateSettingsRequest): ApiResponse<UserSettings>

    // STATISTICS
    @GET("statistics")
    suspend fun getStatistics(): ApiResponse<UserStatistics>

    @GET("health")
    suspend fun getHealth(): ApiResponse<EmptyResponse>

    // FRIENDS
    @GET("friends")
    suspend fun getFriends(): ApiResponse<List<com.nuno.app.features.friends.Friend>>

    @GET("friends/requests")
    suspend fun getFriendRequests(): ApiResponse<List<com.nuno.app.features.friends.FriendRequest>>

    @POST("friends/request")
    suspend fun sendFriendRequest(
        @Body request: com.nuno.app.features.friends.SendFriendRequestBody
    ): ApiResponse<EmptyResponse>

    @POST("friends/accept")
    suspend fun acceptFriendRequest(
        @Body request: com.nuno.app.features.friends.RequestActionBody
    ): ApiResponse<EmptyResponse>

    @POST("friends/reject")
    suspend fun rejectFriendRequest(
        @Body request: com.nuno.app.features.friends.RequestActionBody
    ): ApiResponse<EmptyResponse>

    @DELETE("friends/{friendId}")
    suspend fun removeFriend(
        @Path("friendId") friendId: String
    ): ApiResponse<EmptyResponse>

    @GET("players/search")
    suspend fun searchPlayers(
        @Query("query") query: String
    ): ApiResponse<List<com.nuno.app.features.friends.PlayerSearchResult>>
    @GET("history")
    suspend fun getMatchHistory(): ApiResponse<List<MatchHistoryData>>
    // LEADERBOARD
    @GET("leaderboard/global")
    suspend fun getGlobalLeaderboard(): ApiResponse<List<com.nuno.app.features.leaderboard.LeaderboardEntry>>

    @GET("leaderboard/friends")
    suspend fun getFriendsLeaderboard(): ApiResponse<List<com.nuno.app.features.leaderboard.LeaderboardEntry>>
    // ─────────────────────────────────────────
    // STORE
    // ─────────────────────────────────────────

    @GET("store")
    suspend fun getStore(): ApiResponse<List<com.nuno.app.features.store.StoreItem>>

    @POST("store/purchase")
    suspend fun purchaseItem(
        @Body request: com.nuno.app.features.store.PurchaseRequest
    ): ApiResponse<EmptyResponse>

    @GET("balance")
    suspend fun getBalance(): ApiResponse<com.nuno.app.features.store.Balance>

    @POST("rewards/daily")
    suspend fun claimDailyReward(): ApiResponse<EmptyResponse>
    // ─────────────────────────────────────────
    // NOTIFICATIONS
    // ─────────────────────────────────────────

    @GET("notifications")
    suspend fun getNotifications(): ApiResponse<List<com.nuno.app.features.notifications.Notification>>

    @PATCH("notifications/read")
    suspend fun markNotificationsRead(): ApiResponse<EmptyResponse>
    // ─────────────────────────────────────────
    // REPORTS & BLOCK
    // ─────────────────────────────────────────

    @POST("reports")
    suspend fun reportPlayer(
        @Body request: com.nuno.app.features.reports.ReportRequest
    ): ApiResponse<EmptyResponse>

    @POST("block")
    suspend fun blockPlayer(
        @Body request: com.nuno.app.features.reports.BlockRequest
    ): ApiResponse<EmptyResponse>

    @DELETE("block/{playerId}")
    suspend fun unblockPlayer(
        @Path("playerId") playerId: String
    ): ApiResponse<EmptyResponse>

    @GET("block")
    suspend fun getBlockedPlayers(): ApiResponse<List<String>>
}