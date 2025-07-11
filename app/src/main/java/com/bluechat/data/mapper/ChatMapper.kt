package com.bluechat.data.mapper

import com.bluechat.data.local.entity.ChatEntity
import com.bluechat.domain.model.Chat

fun ChatEntity.toChat(): Chat {
    return Chat(
        id = chatId,
        title = title,
        participants = emptyList(), // You'll need to load participants separately
        lastMessage = lastMessage,
        lastMessageTime = java.util.Date(lastMessageAt),
        unreadCount = unreadCount,
        isGroup = isGroup,
        avatarUrl = null // You can add avatar URL to the entity if needed
    )
}

fun Chat.toChatEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        creatorId = participants.firstOrNull() ?: "", // This assumes the first participant is the creator
        title = title,
        isGroup = isGroup,
        lastMessage = lastMessage,
        lastMessageAt = lastMessageTime.time,
        lastMessageSenderId = participants.firstOrNull() ?: "",
        unreadCount = unreadCount,
        isArchived = false
    )
}
