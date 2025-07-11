package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "conversations",
    indices = [
        Index("lastMessageId"),
        Index("createdAt"),
        Index("isGroup")
    ]
)
data class ConversationEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val type: ConversationType = ConversationType.DIRECT,
    val lastMessageId: String? = null,
    val participantIds: List<String> = emptyList(),
    val isGroup: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val createdBy: String,
    val avatarUri: String? = null,
    val isArchived: Boolean = false,
    val isMuted: Boolean = false,
    val customData: String? = null
)
