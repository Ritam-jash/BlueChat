package com.bluechat.domain.model

data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val publicKey: String,
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val profileImageUri: String? = null,
    val status: String = "Available",
    val createdAt: Long = System.currentTimeMillis()
) {
    val isCurrentUser: Boolean
        get() = id == currentUserId

    companion object {
        // This would be set after login/registration
        var currentUserId: String = ""
            private set

        fun setCurrentUserId(userId: String) {
            currentUserId = userId
        }
    }
}
