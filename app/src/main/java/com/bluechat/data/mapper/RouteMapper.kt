package com.bluechat.data.mapper

import com.bluechat.data.local.entity.RoutingTableEntity
import com.bluechat.domain.model.Route
import javax.inject.Inject

class RouteMapper @Inject constructor() {

    fun toDomain(entity: RoutingTableEntity): Route {
        return Route(
            deviceId = entity.deviceId,
            nextHopId = entity.nextHopId,
            hopCount = entity.hopCount,
            lastUpdated = entity.lastUpdated,
            connectionQuality = entity.connectionQuality,
            isActive = entity.isActive
        )
    }

    fun toEntity(domain: Route): RoutingTableEntity {
        return RoutingTableEntity(
            deviceId = domain.deviceId,
            nextHopId = domain.nextHopId,
            hopCount = domain.hopCount,
            lastUpdated = domain.lastUpdated,
            connectionQuality = domain.connectionQuality,
            isActive = domain.isActive
        )
    }
}
