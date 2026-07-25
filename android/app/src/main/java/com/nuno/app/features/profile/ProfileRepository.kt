package com.nuno.app.features.profile

import com.nuno.app.core.common.Resource
import com.nuno.app.core.network.ApiService
import com.nuno.app.core.network.UpdateProfileRequest
import com.nuno.app.core.network.UserProfile
import com.nuno.app.features.profile.models.Achievement
import com.nuno.app.features.profile.models.MatchHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import com.nuno.app.core.network.MatchHistoryData


@Singleton
class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getProfile(): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getProfile()
            if (response.success && response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun updateProfile(username: String? = null, avatarUrl: String? = null): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.updateProfile(UpdateProfileRequest(username, avatarUrl))
            if (response.success && response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun getMatchHistory(): Flow<Resource<List<com.nuno.app.core.network.MatchHistoryData>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getMatchHistory()
            if (response.success && response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Success(emptyList()))
            }
        } catch (_: Exception) {
            emit(Resource.Success(emptyList()))
        }
    }

    fun getAchievements(): List<Achievement> {
        // Sample achievements - replace with backend data
        return listOf(
            Achievement("first_win", "First Victory", "Win your first match", "🏆", 0, 1, false, 100, 50, "COMMON"),
            Achievement("win_10", "Rising Star", "Win 10 matches", "⭐", 0, 10, false, 200, 100, "RARE"),
            Achievement("win_100", "Champion", "Win 100 matches", "👑", 0, 100, false, 1000, 500, "EPIC"),
            Achievement("streak_5", "On Fire", "Win 5 in a row", "🔥", 0, 5, false, 300, 150, "RARE"),
            Achievement("cards_1000", "Card Master", "Play 1000 cards", "🎴", 0, 1000, false, 500, 250, "EPIC")
        )
    }
}