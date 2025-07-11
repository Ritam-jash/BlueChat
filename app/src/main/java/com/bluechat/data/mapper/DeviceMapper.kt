package com.bluechat.data.mapper

import com.bluechat.data.local.entity.DeviceCapability
import com.bluechat.data.local.entity.DeviceEntity
import com.bluechat.domain.model.Device
import com.bluechat.domain.model.DeviceCapability as DomainDeviceCapability
import javax.inject.Inject

class DeviceMapper @Inject constructor() {
    fun toDomain(entity: DeviceEntity): Device {
        return Device(
            id = entity.id,
            name = entity.name,
            address = entity.address,
            publicKey = entity.publicKey,
            lastSeen = entity.lastSeen,
            isOnline = entity.isOnline,
            capabilities = entity.capabilities.map { DomainDeviceCapability.valueOf(it.name) }.toSet(),
            rssi = entity.rssi,
            isPaired = entity.isPaired,
            alias = entity.alias,
            isThisDevice = entity.isThisDevice,
            connectionState = when (entity.connectionState) {
                DeviceEntity.ConnectionState.CONNECTED -> Device.ConnectionState.CONNECTED
                DeviceEntity.ConnectionState.CONNECTING -> Device.ConnectionState.CONNECTING
                DeviceEntity.ConnectionState.DISCONNECTED -> Device.ConnectionState.DISCONNECTED
                DeviceEntity.ConnectionState.DISCONNECTING -> Device.ConnectionState.DISCONNECTING
                DeviceEntity.ConnectionState.ERROR -> Device.ConnectionState.ERROR
            },
            lastConnected = entity.lastConnected
        )
    }

    fun toEntity(domain: Device): DeviceEntity {
        return DeviceEntity(
            id = domain.id,
            name = domain.name,
            address = domain.address,
            publicKey = domain.publicKey,
            lastSeen = domain.lastSeen,
            isOnline = domain.isOnline,
            capabilities = domain.capabilities.map { DeviceCapability.valueOf(it.name) },
            rssi = domain.rssi,
            isPaired = domain.isPaired,
            alias = domain.alias,
            isThisDevice = domain.isThisDevice,
            connectionState = when (domain.connectionState) {
                Device.ConnectionState.CONNECTED -> DeviceEntity.ConnectionState.CONNECTED
                Device.ConnectionState.CONNECTING -> DeviceEntity.ConnectionState.CONNECTING
                Device.ConnectionState.DISCONNECTED -> DeviceEntity.ConnectionState.DISCONNECTED
                Device.ConnectionState.DISCONNECTING -> DeviceEntity.ConnectionState.DISCONNECTING
                Device.ConnectionState.ERROR -> DeviceEntity.ConnectionState.ERROR
            },
            lastConnected = domain.lastConnected
        )
    }
}
