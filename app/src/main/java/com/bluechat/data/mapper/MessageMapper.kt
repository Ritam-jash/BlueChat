package com.bluechat.data.mapper

import com.bluechat.data.local.entity.MessageEntity
import com.bluechat.domain.model.Message

fun MessageEntity.toMessage(): Message {
    return Message(
        id = messageId,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        status = when (status) {
            MessageEntity.MessageStatus.SENDING -> Message.Status.SENDING
            MessageEntity.MessageStatus.SENT -> Message.Status.SENT
            MessageEntity.MessageStatus.DELIVERED -> Message.Status.DELIVERED
            MessageEntity.MessageStatus.READ -> Message.Status.READ
            MessageEntity.MessageStatus.FAILED -> Message.Status.FAILED
        },
        isRead = isRead,
        isEdited = isEdited,
        isDeleted = isDeleted,
        replyToMessageId = replyToMessageId,
        type = when (type) {
            MessageEntity.MessageType.TEXT -> Message.MessageType.TEXT
            MessageEntity.MessageType.IMAGE -> Message.MessageType.IMAGE
            MessageEntity.MessageType.VIDEO -> Message.MessageType.VIDEO
            MessageEntity.MessageType.AUDIO -> Message.MessageType.AUDIO
            MessageEntity.MessageType.FILE -> Message.MessageType.FILE
            MessageEntity.MessageType.LOCATION -> Message.MessageType.LOCATION
            MessageEntity.MessageType.CONTACT -> Message.MessageType.CONTACT
        }
    )
}

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        status = when (status) {
            Message.Status.SENDING -> MessageEntity.MessageStatus.SENDING
            Message.Status.SENT -> MessageEntity.MessageStatus.SENT
            Message.Status.DELIVERED -> MessageEntity.MessageStatus.DELIVERED
            Message.Status.READ -> MessageEntity.MessageStatus.READ
            Message.Status.FAILED -> MessageEntity.MessageStatus.FAILED
        },
        isRead = isRead,
        isEdited = isEdited,
        isDeleted = isDeleted,
        replyToMessageId = replyToMessageId,
        type = when (type) {
            Message.MessageType.TEXT -> MessageEntity.MessageType.TEXT
            Message.MessageType.IMAGE -> MessageEntity.MessageType.IMAGE
            Message.MessageType.VIDEO -> MessageEntity.MessageType.VIDEO
            Message.MessageType.AUDIO -> MessageEntity.MessageType.AUDIO
            Message.MessageType.FILE -> MessageEntity.MessageType.FILE
            Message.MessageType.LOCATION -> MessageEntity.MessageType.LOCATION
            Message.MessageType.CONTACT -> MessageEntity.MessageType.CONTACT
        }
    )
}
