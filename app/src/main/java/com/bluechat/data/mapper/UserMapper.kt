package com.bluechat.data.mapper

import com.bluechat.data.local.entity.UserEntity
import com.bluechat.domain.model.User

fun UserEntity.toUser(): User {
    return User(
        id = userId,
        username = username,
        displayName = displayName,
        publicKey = publicKey,
        lastSeen = lastSeen,
        isOnline = isOnline,
        profileImageUri = profileImageUri,
        status = status,
        createdAt = createdAt
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = id,
        username = username,
        displayName = displayName,
        publicKey = publicKey,
        lastSeen = lastSeen,
        isOnline = isOnline,
        profileImageUri = profileImageUri,
        status = status,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )
}
