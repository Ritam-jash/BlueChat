package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE isArchived = 0 ORDER BY lastMessageAt DESC")
    fun observeAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE chatId = :chatId")
    suspend fun getChatById(chatId: String): ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Update
    suspend fun updateChats(chats: List<ChatEntity>)

    @Query("UPDATE chats SET lastMessage = :message, lastMessageAt = :timestamp, lastMessageSenderId = :senderId, updatedAt = :timestamp WHERE chatId = :chatId")
    suspend fun updateLastMessage(chatId: String, message: String, senderId: String, timestamp: Long)

    @Query("UPDATE chats SET unreadCount = unreadCount + 1 WHERE chatId = :chatId")
    suspend fun incrementUnreadCount(chatId: String)

    @Query("UPDATE chats SET unreadCount = 0 WHERE chatId = :chatId")
    suspend fun resetUnreadCount(chatId: String)

    @Query("UPDATE chats SET isArchived = :isArchived WHERE chatId = :chatId")
    suspend fun updateArchiveStatus(chatId: String, isArchived: Boolean)

    @Query("DELETE FROM chats WHERE chatId = :chatId")
    suspend fun deleteChat(chatId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()
}
