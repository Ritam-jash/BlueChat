package com.bluechat.domain.model

import java.util.*

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val participantIds: List<String>,
    val lastMessageId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val type: ConversationType,
    val isMuted: Boolean = false,
    val isArchived: Boolean = false,
    val avatarUri: String? = null
)

enum class ConversationType {
    DIRECT, GROUP
}
