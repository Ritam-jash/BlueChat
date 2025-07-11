package com.bluechat.domain.repository

import com.bluechat.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface RoutingRepository {
    fun observeRoutes(): Flow<List<Route>>
    suspend fun getBestRoute(destinationDeviceId: String): Route?
    suspend fun saveRoute(route: Route)
    suspend fun deleteRoute(deviceId: String, nextHopId: String)
    suspend fun refreshRoutingTable()
    suspend fun updateRouteInfo(
        deviceId: String,
        nextHopId: String,
        hopCount: Int,
        connectionQuality: Float
    )
}
