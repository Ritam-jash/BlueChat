package com.bluechat.domain.model

import java.util.*

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: Status = Status.SENT,
    val isRead: Boolean = false,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val replyToMessageId: String? = null,
    val type: MessageType = MessageType.TEXT
) {
    enum class Status {
        SENDING,
        SENT,
        DELIVERED,
        READ,
        FAILED
    }

    enum class MessageType {
        TEXT,
        IMAGE,
        VIDEO,
        AUDIO,
        FILE,
        LOCATION,
        CONTACT
    }

    val formattedTime: String
        get() {
            val calendar = Calendar.getInstance().apply { time = Date(timestamp) }
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            return String.format("%02d:%02d", hour, minute)
        }
}
