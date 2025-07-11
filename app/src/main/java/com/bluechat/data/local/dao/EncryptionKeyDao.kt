package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.EncryptionKeyEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EncryptionKeyDao {
    @Query("SELECT * FROM encryption_keys WHERE deviceId = :deviceId AND isActive = 1 ORDER BY createdAt DESC LIMIT 1")
    suspend fun getActiveKeyForDevice(deviceId: String): EncryptionKeyEntity?

    @Query("SELECT * FROM encryption_keys WHERE deviceId = :deviceId ORDER BY createdAt DESC")
    fun getKeysForDevice(deviceId: String): Flow<List<EncryptionKeyEntity>>

    @Query("SELECT * FROM encryption_keys WHERE keyId = :keyId")
    suspend fun getKeyById(keyId: String): EncryptionKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: EncryptionKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keys: List<EncryptionKeyEntity>)

    @Update
    suspend fun updateKey(key: EncryptionKeyEntity)

    @Query("UPDATE encryption_keys SET isActive = 0 WHERE deviceId = :deviceId")
    suspend fun deactivateAllKeysForDevice(deviceId: String)

    @Query("DELETE FROM encryption_keys WHERE deviceId = :deviceId")
    suspend fun deleteKeysForDevice(deviceId: String)

    @Query("DELETE FROM encryption_keys WHERE expiresAt < :currentTime AND expiresAt IS NOT NULL")
    suspend fun deleteExpiredKeys(currentTime: Long = System.currentTimeMillis())

    @Query("SELECT * FROM encryption_keys WHERE deviceId = :deviceId AND keyType = :keyType AND isActive = 1 ORDER BY createdAt DESC LIMIT 1")
    suspend fun getActiveKeyByType(deviceId: String, keyType: EncryptionKeyEntity.KeyType): EncryptionKeyEntity?

    @Query("""
        SELECT * FROM encryption_keys 
        WHERE deviceId = :deviceId 
        AND keyType = :keyType 
        ORDER BY createdAt DESC 
        LIMIT :limit
    """)
    suspend fun getRecentKeysByType(
        deviceId: String,
        keyType: EncryptionKeyEntity.KeyType,
        limit: Int = 10
    ): List<EncryptionKeyEntity>

    @Query("""
        UPDATE encryption_keys 
        SET isActive = :isActive,
            expiresAt = :expiresAt
        WHERE keyId = :keyId
    """)
    suspend fun updateKeyStatus(
        keyId: String,
        isActive: Boolean,
        expiresAt: Long? = null
    )

    @Query("""
        SELECT COUNT(*) FROM encryption_keys 
        WHERE deviceId = :deviceId 
        AND keyType = :keyType
    """)
    suspend fun getKeyCountByType(deviceId: String, keyType: EncryptionKeyEntity.KeyType): Int
}
