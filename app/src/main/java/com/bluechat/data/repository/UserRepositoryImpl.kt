package com.bluechat.data.repository

import com.bluechat.data.local.dao.UserDao
import com.bluechat.data.mapper.toUser
import com.bluechat.data.mapper.toUserEntity
import com.bluechat.domain.model.User
import com.bluechat.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    // In a real app, this would come from an authentication service
    private var _currentUserId: String = ""

    override suspend fun getCurrentUserId(): String {
        if (_currentUserId.isEmpty()) {
            // For demo purposes, create a default user if none exists
            val defaultUser = User(
                id = "user_${System.currentTimeMillis()}",
                username = "user_${(1000..9999).random()}",
                displayName = "User ${(1000..9999).random()}",
                publicKey = "",
                isOnline = true
            )
            saveUser(defaultUser)
            _currentUserId = defaultUser.id
        }
        return _currentUserId
    }

    override fun observeUser(userId: String): Flow<User?> {
        return userDao.observeUserById(userId)
            .map { it?.toUser() }
    }

    override fun observeUsers(userIds: List<String>): Flow<List<User>> {
        return userDao.observeUsersByIds(userIds)
            .map { users -> users.map { it.toUser() } }
    }

    override suspend fun getUser(userId: String): User? {
        return userDao.getUserById(userId)?.toUser()
    }

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toUserEntity())
    }

    override suspend fun updateUserStatus(userId: String, isOnline: Boolean, lastSeen: Long) {
        userDao.updateUserStatus(userId, isOnline, lastSeen)
    }

    override suspend fun searchUsers(query: String): List<User> {
        // In a real app, this would search both local and remote data sources
        return userDao.searchUsers("%${query.trim()}%")
            .map { it.toUser() }
    }
}
