package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "chats",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["creatorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("creatorId"),
        Index("lastMessageAt")
    ]
)
data class ChatEntity(
    @PrimaryKey
    val chatId: String = UUID.randomUUID().toString(),
    val creatorId: String,
    val title: String,
    val isGroup: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastMessageAt: Long = 0,
    val lastMessage: String? = null,
    val lastMessageSenderId: String? = null,
    val unreadCount: Int = 0,
    val isArchived: Boolean = false
)
