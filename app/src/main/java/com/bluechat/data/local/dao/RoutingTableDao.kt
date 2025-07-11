package com.bluechat.data.local.dao

import androidx.room.*
import com.bluechat.data.local.entity.RoutingTableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutingTableDao {
    @Query("SELECT * FROM routing_table WHERE deviceId = :deviceId")
    fun getRoutesForDevice(deviceId: String): Flow<List<RoutingTableEntity>>

    @Query("SELECT * FROM routing_table WHERE nextHopId = :nextHopId")
    fun getRoutesViaNextHop(nextHopId: String): Flow<List<RoutingTableEntity>>

    @Query("SELECT * FROM routing_table WHERE deviceId = :deviceId AND nextHopId = :nextHopId")
    suspend fun getRoute(deviceId: String, nextHopId: String): RoutingTableEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RoutingTableEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<RoutingTableEntity>)

    @Update
    suspend fun updateRoute(route: RoutingTableEntity)

    @Query("DELETE FROM routing_table WHERE deviceId = :deviceId AND nextHopId = :nextHopId")
    suspend fun deleteRoute(deviceId: String, nextHopId: String)

    @Query("DELETE FROM routing_table WHERE deviceId = :deviceId")
    suspend fun deleteRoutesForDevice(deviceId: String)

    @Query("DELETE FROM routing_table")
    suspend fun clearRoutingTable()

    @Query("""
        SELECT * FROM routing_table 
        WHERE isActive = 1 
        AND lastUpdated > :minLastUpdated
        ORDER BY hopCount ASC, lastUpdated DESC
    """)
    fun getActiveRoutes(minLastUpdated: Long = System.currentTimeMillis() - 300000): Flow<List<RoutingTableEntity>>

    @Query("""
        SELECT * FROM routing_table 
        WHERE deviceId = :deviceId 
        AND isActive = 1 
        ORDER BY hopCount ASC, lastUpdated DESC 
        LIMIT 1
    """)
    suspend fun getBestRoute(deviceId: String): RoutingTableEntity?

    @Query("""
        UPDATE routing_table 
        SET isActive = 0 
        WHERE lastUpdated < :maxLastUpdated
    """)
    suspend fun markStaleRoutesInactive(maxLastUpdated: Long = System.currentTimeMillis() - 300000)

    @Query("""
        UPDATE routing_table 
        SET hopCount = :hopCount, 
            lastUpdated = :lastUpdated, 
            connectionQuality = :connectionQuality,
            isActive = 1 
        WHERE deviceId = :deviceId 
        AND nextHopId = :nextHopId
    """)
    suspend fun updateRouteInfo(
        deviceId: String,
        nextHopId: String,
        hopCount: Int,
        lastUpdated: Long = System.currentTimeMillis(),
        connectionQuality: Float = 1.0f
    )
}
