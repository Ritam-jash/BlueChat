package com.bluechat.data.repository

import com.bluechat.data.local.dao.MessageDao
import com.bluechat.data.mapper.toMessage
import com.bluechat.data.mapper.toMessageEntity
import com.bluechat.domain.model.Message
import com.bluechat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return messageDao.observeMessagesByChatId(chatId)
            .map { messages -> messages.map { it.toMessage() } }
    }

    override suspend fun getMessageById(messageId: String): Message? {
        return messageDao.getMessageById(messageId)?.toMessage()
    }

    override suspend fun saveMessage(message: Message) {
        messageDao.insertMessage(message.toMessageEntity())
    }

    override suspend fun saveMessages(messages: List<Message>) {
        messageDao.insertMessages(messages.map { it.toMessageEntity() })
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message.toMessageEntity())
    }

    override suspend fun updateMessageStatus(messageId: String, status: Message.Status) {
        messageDao.updateMessageStatus(
            messageId = messageId,
            status = when (status) {
                Message.Status.SENDING -> com.bluechat.data.local.entity.MessageEntity.MessageStatus.SENDING
                Message.Status.SENT -> com.bluechat.data.local.entity.MessageEntity.MessageStatus.SENT
                Message.Status.DELIVERED -> com.bluechat.data.local.entity.MessageEntity.MessageStatus.DELIVERED
                Message.Status.READ -> com.bluechat.data.local.entity.MessageEntity.MessageStatus.READ
                Message.Status.FAILED -> com.bluechat.data.local.entity.MessageEntity.MessageStatus.FAILED
            }
        )
    }

    override suspend fun markMessagesAsRead(chatId: String, userId: String) {
        messageDao.markMessagesAsRead(chatId, userId)
    }

    override suspend fun deleteMessage(messageId: String) {
        messageDao.hardDeleteMessage(messageId)
    }
}
