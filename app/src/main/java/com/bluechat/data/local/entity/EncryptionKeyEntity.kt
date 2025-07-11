package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "encryption_keys",
    foreignKeys = [
        ForeignKey(
            entity = DeviceEntity::class,
            parentColumns = ["id"],
            childColumns = ["deviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("deviceId"),
        Index("keyId"),
        Index("isActive")
    ]
)
data class EncryptionKeyEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val deviceId: String,
    val keyId: String,
    val keyData: String, // Base64 encoded key data
    val algorithm: String = "AES-256-GCM",
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null,
    val isActive: Boolean = true,
    val keyType: KeyType = KeyType.MESSAGE,
    val metadata: String? = null
) {
    enum class KeyType {
        MESSAGE,
        FILE,
        SESSION,
        IDENTITY
    }
}
