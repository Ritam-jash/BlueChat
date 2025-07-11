package com.bluechat.domain.repository

/**
 * Base repository interface with common CRUD operations
 */
interface BaseRepository<T, ID> {
    suspend fun getById(id: ID): T?
    suspend fun getAll(): List<T>
    suspend fun save(entity: T)
    suspend fun delete(entity: T)
    suspend fun deleteById(id: ID)
}
