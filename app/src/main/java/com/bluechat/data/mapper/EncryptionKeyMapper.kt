package com.bluechat.data.mapper

import android.util.Base64
import com.bluechat.data.local.entity.EncryptionKeyEntity
import com.bluechat.domain.model.EncryptionKey
import javax.inject.Inject

class EncryptionKeyMapper @Inject constructor() {

    fun toDomain(entity: EncryptionKeyEntity): EncryptionKey {
        return EncryptionKey(
            id = entity.id,
            deviceId = entity.deviceId,
            keyId = entity.keyId,
            keyData = Base64.decode(entity.keyData, Base64.DEFAULT),
            algorithm = entity.algorithm,
            createdAt = entity.createdAt,
            expiresAt = entity.expiresAt,
            isActive = entity.isActive,
            keyType = when (entity.keyType) {
                EncryptionKeyEntity.KeyType.MESSAGE -> EncryptionKey.KeyType.MESSAGE
                EncryptionKeyEntity.KeyType.FILE -> EncryptionKey.KeyType.FILE
                EncryptionKeyEntity.KeyType.SESSION -> EncryptionKey.KeyType.SESSION
                EncryptionKeyEntity.KeyType.IDENTITY -> EncryptionKey.KeyType.IDENTITY
            }
        )
    }

    fun toEntity(domain: EncryptionKey): EncryptionKeyEntity {
        return EncryptionKeyEntity(
            id = domain.id,
            deviceId = domain.deviceId,
            keyId = domain.keyId,
            keyData = Base64.encodeToString(domain.keyData, Base64.DEFAULT),
            algorithm = domain.algorithm,
            createdAt = domain.createdAt,
            expiresAt = domain.expiresAt,
            isActive = domain.isActive,
            keyType = when (domain.keyType) {
                EncryptionKey.KeyType.MESSAGE -> EncryptionKeyEntity.KeyType.MESSAGE
                EncryptionKey.KeyType.FILE -> EncryptionKeyEntity.KeyType.FILE
                EncryptionKey.KeyType.SESSION -> EncryptionKeyEntity.KeyType.SESSION
                EncryptionKey.KeyType.IDENTITY -> EncryptionKeyEntity.KeyType.IDENTITY
            },
            metadata = null
        )
    }
}
