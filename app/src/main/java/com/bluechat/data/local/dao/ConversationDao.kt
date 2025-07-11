package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.ConversationEntity
import com.bluechat.data.local.entity.ConversationType
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversationById(conversationId: String): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Query("UPDATE conversations SET lastMessageId = :messageId, updatedAt = :updatedAt WHERE id = :conversationId")
    suspend fun updateLastMessage(conversationId: String, messageId: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE conversations SET isArchived = :isArchived WHERE id = :conversationId")
    suspend fun updateArchiveStatus(conversationId: String, isArchived: Boolean)

    @Query("UPDATE conversations SET isMuted = :isMuted WHERE id = :conversationId")
    suspend fun updateMuteStatus(conversationId: String, isMuted: Boolean)

    @Query("DELETE FROM conversations WHERE id = :conversationId")
    suspend fun deleteConversation(conversationId: String)

    @Query("SELECT * FROM conversations WHERE type = 'DIRECT' AND :userId IN (participantIds) AND participantIds LIKE '%' || :otherUserId || '%'")
    suspend fun findDirectConversation(userId: String, otherUserId: String): ConversationEntity?

    @Query("SELECT * FROM conversations WHERE type = :type AND :userId IN (participantIds)")
    fun getConversationsByType(userId: String, type: ConversationType): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE isArchived = :isArchived ORDER BY updatedAt DESC")
    fun getConversationsByArchiveStatus(isArchived: Boolean): Flow<List<ConversationEntity>>

    @Query("SELECT COUNT(*) FROM conversations WHERE isArchived = 0")
    fun getUnarchivedConversationCount(): Flow<Int>

    @Query("""
        SELECT * FROM conversations 
        WHERE name LIKE '%' || :query || '%' 
        OR id IN (
            SELECT conversationId FROM messages 
            WHERE content LIKE '%' || :query || '%'
        )
        ORDER BY updatedAt DESC
    """)
    fun searchConversations(query: String): Flow<List<ConversationEntity>>

    @Query("UPDATE conversations SET participantIds = :participantIds WHERE id = :conversationId")
    suspend fun updateParticipants(conversationId: String, participantIds: List<String>)

    @Query("UPDATE conversations SET name = :name, avatarUri = :avatarUri, updatedAt = :updatedAt WHERE id = :conversationId")
    suspend fun updateConversationInfo(conversationId: String, name: String?, avatarUri: String?, updatedAt: Long = System.currentTimeMillis())
}
