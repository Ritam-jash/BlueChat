package com.bluechat.domain.model

data class Route(
    val deviceId: String,
    val nextHopId: String,
    val hopCount: Int = 1,
    val lastUpdated: Long = System.currentTimeMillis(),
    val connectionQuality: Float = 1.0f, // 0.0 to 1.0
    val isActive: Boolean = true
) {
    /**
     * Returns true if this route is considered stale (not updated recently)
     */
    fun isStale(now: Long = System.currentTimeMillis(), staleThreshold: Long = 300_000): Boolean {
        return (now - lastUpdated) > staleThreshold
    }

    /**
     * Returns true if this route is better than the other route
     * based on hop count and connection quality
     */
    fun isBetterThan(other: Route?): Boolean {
        if (other == null) return true
        if (hopCount < other.hopCount) return true
        if (hopCount == other.hopCount) {
            return connectionQuality > other.connectionQuality
        }
        return false
    }
}
