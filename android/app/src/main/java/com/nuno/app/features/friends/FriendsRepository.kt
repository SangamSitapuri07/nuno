package com.nuno.app.features.friends

import com.nuno.app.core.common.Resource
import com.nuno.app.core.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getFriends(): Flow<Resource<List<Friend>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getFriends()
            if (response.success && response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed to load friends."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun getFriendRequests(): Flow<Resource<List<FriendRequest>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getFriendRequests()
            if (response.success && response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed to load requests."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun sendFriendRequest(playerId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.sendFriendRequest(SendFriendRequestBody(playerId))
            if (response.success) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed to send request."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun acceptRequest(requestId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.acceptFriendRequest(RequestActionBody(requestId))
            if (response.success) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed to accept."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun rejectRequest(requestId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.rejectFriendRequest(RequestActionBody(requestId))
            if (response.success) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed to reject."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun removeFriend(friendId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.removeFriend(friendId)
            if (response.success) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.error?.message ?: "Failed to remove."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    fun searchPlayers(query: String): Flow<Resource<List<PlayerSearchResult>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.searchPlayers(query)
            if (response.success && response.data != null) {
                emit(Resource.Success(response.data))
            } else {
                emit(Resource.Error(response.error?.message ?: "Search failed."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}