package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY displayName ASC")
    fun observeAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE userId IN (:userIds)")
    suspend fun getUsersByIds(userIds: List<String>): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Update
    suspend fun updateUsers(users: List<UserEntity>)

    @Query("UPDATE users SET isOnline = :isOnline, lastSeen = :lastSeen WHERE userId = :userId")
    suspend fun updateUserStatus(userId: String, isOnline: Boolean, lastSeen: Long)

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUser(userId: String)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}
