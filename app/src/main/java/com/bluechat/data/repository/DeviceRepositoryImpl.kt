package com.bluechat.data.repository

import com.bluechat.data.local.dao.DeviceDao
import com.bluechat.data.mapper.DeviceMapper
import com.bluechat.domain.model.Device
import com.bluechat.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val deviceDao: DeviceDao,
    private val mapper: DeviceMapper
) : DeviceRepository {

    override fun observeDevices(): Flow<List<Device>> {
        return deviceDao.getAllDevices()
            .map { devices -> devices.map { mapper.toDomain(it) } }
    }

    override fun observeOnlineDevices(): Flow<List<Device>> {
        return deviceDao.getOnlineDevices()
            .map { devices -> devices.map { mapper.toDomain(it) } }
    }

    override suspend fun getDeviceById(deviceId: String): Device? {
        return deviceDao.getDeviceById(deviceId)?.let { mapper.toDomain(it) }
    }

    override suspend fun getDeviceByAddress(address: String): Device? {
        return deviceDao.getDeviceByAddress(address)?.let { mapper.toDomain(it) }
    }

    override suspend fun saveDevice(device: Device) {
        deviceDao.insertDevice(mapper.toEntity(device))
    }

    override suspend fun deleteDevice(device: Device) {
        deviceDao.deleteDevice(device.id)
    }

    override suspend fun updateDeviceStatus(deviceId: String, isOnline: Boolean) {
        deviceDao.updateDeviceStatus(deviceId, isOnline, System.currentTimeMillis())
    }

    override suspend fun getThisDevice(): Device? {
        return deviceDao.getThisDevice()?.let { mapper.toDomain(it) }
    }
}
