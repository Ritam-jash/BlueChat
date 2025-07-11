package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "devices",
    indices = [
        Index("address", unique = true),
        Index("lastSeen"),
        Index("isOnline")
    ]
)
data class DeviceEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val address: String, // MAC address or BLE address
    val publicKey: String,
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val capabilities: List<DeviceCapability> = emptyList(),
    val rssi: Int = 0,
    val isPaired: Boolean = false,
    val alias: String? = null,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val lastConnected: Long? = null,
    val isTrusted: Boolean = false
) {
    enum class ConnectionState {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCONNECTING,
        ERROR
    }
}
