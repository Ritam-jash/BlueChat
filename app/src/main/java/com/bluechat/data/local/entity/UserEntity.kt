package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String = UUID.randomUUID().toString(),
    val username: String,
    val displayName: String,
    val publicKey: String,
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val profileImageUri: String? = null,
    val status: String = "Available",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
