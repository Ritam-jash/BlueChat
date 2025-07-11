package com.bluechat.di

import android.content.Context
import com.bluechat.data.local.AppDatabase
import com.bluechat.data.repository.*
import com.bluechat.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideMessageDao(database: AppDatabase) = database.messageDao()

    @Provides
    @Singleton
    fun provideChatDao(database: AppDatabase) = database.chatDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideMessageRepository(impl: MessageRepositoryImpl): MessageRepository = impl

    @Provides
    @Singleton
    fun provideChatRepository(impl: ChatRepositoryImpl): ChatRepository = impl

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    @Singleton
    fun provideBluetoothRepository(impl: BluetoothRepositoryImpl): BluetoothRepository = impl
}
