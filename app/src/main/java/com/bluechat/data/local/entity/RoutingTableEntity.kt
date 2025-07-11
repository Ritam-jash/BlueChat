package com.bluechat.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "routing_table",
    primaryKeys = ["deviceId", "nextHopId"],
    foreignKeys = [
        ForeignKey(
            entity = DeviceEntity::class,
            parentColumns = ["id"],
            childColumns = ["deviceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DeviceEntity::class,
            parentColumns = ["id"],
            childColumns = ["nextHopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("deviceId"),
        Index("nextHopId"),
        Index("lastUpdated")
    ]
)
data class RoutingTableEntity(
    val deviceId: String,
    val nextHopId: String,
    val hopCount: Int,
    val lastUpdated: Long = System.currentTimeMillis(),
    val connectionQuality: Float = 1.0f,
    val isActive: Boolean = true
)
