package com.bluechat.domain.repository

import com.bluechat.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUserId(): String
    fun observeUser(userId: String): Flow<User?>
    fun observeUsers(userIds: List<String>): Flow<List<User>>
    suspend fun getUser(userId: String): User?
    suspend fun saveUser(user: User)
    suspend fun updateUserStatus(userId: String, isOnline: Boolean, lastSeen: Long)
    suspend fun searchUsers(query: String): List<User>
}
