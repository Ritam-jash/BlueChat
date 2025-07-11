package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices")
    fun getAllDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE isOnline = 1")
    fun getOnlineDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE id = :deviceId")
    suspend fun getDeviceById(deviceId: String): DeviceEntity?

    @Query("SELECT * FROM devices WHERE address = :address")
    suspend fun getDeviceByAddress(address: String): DeviceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<DeviceEntity>)

    @Update
    suspend fun updateDevice(device: DeviceEntity)

    @Query("UPDATE devices SET isOnline = :isOnline, lastSeen = :lastSeen WHERE id = :deviceId")
    suspend fun updateDeviceStatus(deviceId: String, isOnline: Boolean, lastSeen: Long = System.currentTimeMillis())

    @Query("UPDATE devices SET isOnline = 0, lastSeen = :lastSeen WHERE isOnline = 1")
    suspend fun markAllDevicesOffline(lastSeen: Long = System.currentTimeMillis())

    @Query("DELETE FROM devices WHERE id = :deviceId")
    suspend fun deleteDevice(deviceId: String)

    @Query("DELETE FROM devices")
    suspend fun deleteAllDevices()

    @Query("SELECT * FROM devices WHERE name LIKE '%' || :query || '%' OR address LIKE '%' || :query || '%'")
    fun searchDevices(query: String): Flow<List<DeviceEntity>>

    @Query("UPDATE devices SET isPaired = :isPaired WHERE id = :deviceId")
    suspend fun updatePairingStatus(deviceId: String, isPaired: Boolean)

    @Query("UPDATE devices SET alias = :alias WHERE id = :deviceId")
    suspend fun updateDeviceAlias(deviceId: String, alias: String?)

    @Query("SELECT * FROM devices WHERE isThisDevice = 1 LIMIT 1")
    suspend fun getThisDevice(): DeviceEntity?

    @Query("UPDATE devices SET publicKey = :publicKey WHERE id = :deviceId")
    suspend fun updateDevicePublicKey(deviceId: String, publicKey: String)

    @Query("SELECT COUNT(*) FROM devices WHERE isOnline = 1")
    fun getOnlineDeviceCount(): Flow<Int>
}
