package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("conversationId"),
        Index("senderId"),
        Index("timestamp"),
        Index("deliveryStatus")
    ]
)
data class MessageEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: MessageType = MessageType.TEXT,
    val isEncrypted: Boolean = false,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    val hopCount: Int = 0,
    val parentMessageId: String? = null,
    val metadata: String? = null,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null,
    val replyToMessageId: String? = null,
    val type: MessageType = MessageType.TEXT
) {
    enum class MessageStatus {
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
}
