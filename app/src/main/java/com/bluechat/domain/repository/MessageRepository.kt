package com.bluechat.domain.repository

import com.bluechat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun getMessageById(messageId: String): Message?
    suspend fun saveMessage(message: Message)
    suspend fun saveMessages(messages: List<Message>)
    suspend fun updateMessage(message: Message)
    suspend fun updateMessageStatus(messageId: String, status: Message.Status)
    suspend fun markMessagesAsRead(chatId: String, userId: String)
    suspend fun deleteMessage(messageId: String)
}
