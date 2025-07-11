package com.bluechat.domain.model

import java.util.*

data class EncryptionKey(
    val id: String = UUID.randomUUID().toString(),
    val deviceId: String,
    val keyId: String,
    val keyData: ByteArray,
    val algorithm: String = "AES/GCM/NoPadding",
    val keyType: KeyType,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null,
    val isActive: Boolean = true
) {
    enum class KeyType {
        MESSAGE,     // For encrypting/decrypting message content
        FILE,        // For encrypting/decrypting files
        SESSION,     // For secure session establishment
        IDENTITY     // For device identity verification
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptionKey

        if (id != other.id) return false
        if (deviceId != other.deviceId) return false
        if (keyId != other.keyId) return false
        if (!keyData.contentEquals(other.keyData)) return false
        if (keyType != other.keyType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + deviceId.hashCode()
        result = 31 * result + keyId.hashCode()
        result = 31 * result + keyData.contentHashCode()
        result = 31 * result + keyType.hashCode()
        return result
    }
}
