package com.bluechat.domain.repository

import com.bluechat.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun observeDevices(): Flow<List<Device>>
    fun observeOnlineDevices(): Flow<List<Device>>
    suspend fun getDeviceById(deviceId: String): Device?
    suspend fun getDeviceByAddress(address: String): Device?
    suspend fun saveDevice(device: Device)
    suspend fun deleteDevice(device: Device)
    suspend fun updateDeviceStatus(deviceId: String, isOnline: Boolean)
    suspend fun getThisDevice(): Device?
}
