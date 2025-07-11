package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.DeliveryStatus
import com.bluechat.data.local.entity.MessageEntity
import com.bluechat.data.local.entity.MessageType
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC")
    fun getMessagesByConversation(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("UPDATE messages SET deliveryStatus = :status WHERE id = :messageId")
    suspend fun updateMessageStatus(messageId: String, status: DeliveryStatus)

    @Query("UPDATE messages SET deliveryStatus = :status WHERE id IN (:messageIds)")
    suspend fun updateMessagesStatus(messageIds: List<String>, status: DeliveryStatus)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversation(conversationId: String)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND messageType = :type ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessageOfType(conversationId: String, type: MessageType): MessageEntity?

    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId AND deliveryStatus = 'PENDING'")
    fun getPendingMessageCount(conversationId: String): Flow<Int>

    @Query("""
        SELECT * FROM messages 
        WHERE conversationId = :conversationId 
        AND content LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchInConversation(conversationId: String, query: String): Flow<List<MessageEntity>>

    @Query("""
        SELECT * FROM messages 
        WHERE conversationId = :conversationId 
        AND timestamp < :beforeTimestamp
        ORDER BY timestamp DESC
        LIMIT :limit
    """)
    suspend fun getMessagesBefore(
        conversationId: String,
        beforeTimestamp: Long,
        limit: Int = 50
    ): List<MessageEntity>

    @Query("""
        SELECT * FROM messages 
        WHERE conversationId = :conversationId 
        AND timestamp > :afterTimestamp
        ORDER BY timestamp ASC
    """)
    suspend fun getMessagesAfter(
        conversationId: String,
        afterTimestamp: Long
    ): List<MessageEntity>

    @Query("""
        UPDATE messages 
        SET deliveryStatus = :status 
        WHERE id IN (
            SELECT id FROM messages 
            WHERE conversationId = :conversationId 
            AND deliveryStatus = 'PENDING'
            LIMIT :batchSize
        )
    """)
    suspend fun updatePendingMessagesStatus(
        conversationId: String,
        status: DeliveryStatus,
        batchSize: Int = 100
    )
} com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun observeMessagesByChatId(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Update
    suspend fun updateMessages(messages: List<MessageEntity>)

    @Query("UPDATE messages SET status = :status WHERE messageId = :messageId")
    suspend fun updateMessageStatus(messageId: String, status: MessageEntity.MessageStatus)

    @Query("UPDATE messages SET isRead = 1 WHERE chatId = :chatId AND senderId != :currentUserId")
    suspend fun markMessagesAsRead(chatId: String, currentUserId: String)

    @Query("UPDATE messages SET isDeleted = 1, deletedAt = :timestamp WHERE messageId = :messageId")
    suspend fun deleteMessage(messageId: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM messages WHERE messageId = :messageId")
    suspend fun hardDeleteMessage(messageId: String)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND timestamp < :beforeTimestamp ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getMessagesBefore(
        chatId: String,
        beforeTimestamp: Long,
        limit: Int = 20
    ): List<MessageEntity>

    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId AND isRead = 0 AND senderId != :currentUserId")
    suspend fun getUnreadMessageCount(chatId: String, currentUserId: String): Int
}
