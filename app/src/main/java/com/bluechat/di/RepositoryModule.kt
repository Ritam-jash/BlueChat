package com.bluechat.di

import com.bluechat.data.local.dao.ConversationDao
import com.bluechat.data.local.dao.DeviceDao
import com.bluechat.data.local.dao.EncryptionKeyDao
import com.bluechat.data.local.dao.MessageDao
import com.bluechat.data.local.dao.RoutingTableDao
import com.bluechat.data.mapper.ConversationMapper
import com.bluechat.data.mapper.DeviceMapper
import com.bluechat.data.mapper.EncryptionKeyMapper
import com.bluechat.data.mapper.MessageMapper
import com.bluechat.data.mapper.RouteMapper
import com.bluechat.data.repository.ConversationRepositoryImpl
import com.bluechat.data.repository.DeviceRepositoryImpl
import com.bluechat.data.repository.EncryptionKeyRepositoryImpl
import com.bluechat.data.repository.MessageRepositoryImpl
import com.bluechat.data.repository.RoutingRepositoryImpl
import com.bluechat.domain.repository.ConversationRepository
import com.bluechat.domain.repository.DeviceRepository
import com.bluechat.domain.repository.EncryptionKeyRepository
import com.bluechat.domain.repository.MessageRepository
import com.bluechat.domain.repository.RoutingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(impl: ConversationRepositoryImpl): ConversationRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindRoutingRepository(impl: RoutingRepositoryImpl): RoutingRepository

    @Binds
    @Singleton
    abstract fun bindEncryptionKeyRepository(impl: EncryptionKeyRepositoryImpl): EncryptionKeyRepository
}

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    
    @Provides
    @Singleton
    fun provideMessageMapper(): MessageMapper = MessageMapper()
    
    @Provides
    @Singleton
    fun provideConversationMapper(): ConversationMapper = ConversationMapper()
    
    @Provides
    @Singleton
    fun provideDeviceMapper(): DeviceMapper = DeviceMapper()
    
    @Provides
    @Singleton
    fun provideRouteMapper(): RouteMapper = RouteMapper()
    
    @Provides
    @Singleton
    fun provideEncryptionKeyMapper(): EncryptionKeyMapper = EncryptionKeyMapper()
}
