package com.bluechat.data.repository

import com.bluechat.data.local.dao.EncryptionKeyDao
import com.bluechat.data.mapper.EncryptionKeyMapper
import com.bluechat.domain.model.EncryptionKey
import com.bluechat.domain.repository.EncryptionKeyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class EncryptionKeyRepositoryImpl @Inject constructor(
    private val encryptionKeyDao: EncryptionKeyDao,
    private val mapper: EncryptionKeyMapper
) : EncryptionKeyRepository {

    override suspend fun getActiveKey(deviceId: String): EncryptionKey? {
        return encryptionKeyDao.getActiveKeyForDevice(deviceId)?.let { mapper.toDomain(it) }
    }

    override suspend fun getKeysForDevice(deviceId: String): List<EncryptionKey> {
        return encryptionKeyDao.getKeysForDevice(deviceId)
            .map { mapper.toDomain(it) }
    }

    override suspend fun saveKey(key: EncryptionKey) {
        encryptionKeyDao.insertKey(mapper.toEntity(key))
    }

    override suspend fun deleteKey(key: EncryptionKey) {
        encryptionKeyDao.deleteKey(key.id)
    }

    override suspend fun deactivateAllKeysForDevice(deviceId: String) {
        encryptionKeyDao.deactivateAllKeysForDevice(deviceId)
    }

    override suspend fun getActiveKeyByType(
        deviceId: String, 
        keyType: EncryptionKey.KeyType
    ): EncryptionKey? {
        return encryptionKeyDao.getActiveKeyByType(deviceId, keyType)?.let { mapper.toDomain(it) }
    }
}
