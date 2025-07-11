package com.bluechat.domain.repository

import com.bluechat.domain.model.EncryptionKey

interface EncryptionKeyRepository {
    suspend fun getActiveKey(deviceId: String): EncryptionKey?
    suspend fun getKeysForDevice(deviceId: String): List<EncryptionKey>
    suspend fun saveKey(key: EncryptionKey)
    suspend fun deleteKey(key: EncryptionKey)
    suspend fun deactivateAllKeysForDevice(deviceId: String)
    suspend fun getActiveKeyByType(deviceId: String, keyType: EncryptionKey.KeyType): EncryptionKey?
}
