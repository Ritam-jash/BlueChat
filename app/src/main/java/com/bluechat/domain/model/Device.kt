package com.bluechat.domain.model

import java.util.*

data class Device(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val address: String,
    val publicKey: String,
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val capabilities: Set<DeviceCapability> = emptySet(),
    val rssi: Int = 0,
    val isPaired: Boolean = false,
    val alias: String? = null,
    val isThisDevice: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val lastConnected: Long? = null
) {
    enum class ConnectionState {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCONNECTING,
        ERROR
    }
}

enum class DeviceCapability {
    MESSAGING,
    FILE_TRANSFER,
    VOICE_CALL,
    VIDEO_CALL,
    SCREEN_SHARING,
    HIGH_POWER,
    ROUTING
}
