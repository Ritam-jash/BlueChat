package com.bluechat.data.repository

import com.bluechat.data.local.dao.RoutingTableDao
import com.bluechat.data.mapper.RouteMapper
import com.bluechat.domain.model.Route
import com.bluechat.domain.repository.RoutingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutingRepositoryImpl @Inject constructor(
    private val routingTableDao: RoutingTableDao,
    private val mapper: RouteMapper
) : RoutingRepository {

    override fun observeRoutes(): Flow<List<Route>> {
        return routingTableDao.getActiveRoutes()
            .map { routes -> routes.map { mapper.toDomain(it) } }
    }

    override suspend fun getBestRoute(destinationDeviceId: String): Route? {
        return routingTableDao.getBestRoute(destinationDeviceId)?.let { mapper.toDomain(it) }
    }

    override suspend fun saveRoute(route: Route) {
        routingTableDao.insertRoute(mapper.toEntity(route))
    }

    override suspend fun deleteRoute(deviceId: String, nextHopId: String) {
        routingTableDao.deleteRoute(deviceId, nextHopId)
    }

    override suspend fun refreshRoutingTable() {
        routingTableDao.markStaleRoutesInactive()
    }

    override suspend fun updateRouteInfo(
        deviceId: String,
        nextHopId: String,
        hopCount: Int,
        connectionQuality: Float
    ) {
        routingTableDao.updateRouteInfo(
            deviceId = deviceId,
            nextHopId = nextHopId,
            hopCount = hopCount,
            connectionQuality = connectionQuality
        )
    }
}
