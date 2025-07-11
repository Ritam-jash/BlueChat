package com.bluechat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bluechat.data.local.converters.Converters
import com.bluechat.data.local.dao.ConversationDao
import com.bluechat.data.local.dao.DeviceDao
import com.bluechat.data.local.dao.EncryptionKeyDao
import com.bluechat.data.local.dao.MessageDao
import com.bluechat.data.local.dao.RoutingTableDao
import com.bluechat.data.local.entity.ConversationEntity
import com.bluechat.data.local.entity.DeviceEntity
import com.bluechat.data.local.entity.EncryptionKeyEntity
import com.bluechat.data.local.entity.MessageEntity
import com.bluechat.data.local.entity.RoutingTableEntity
import com.bluechat.data.local.migrations.MIGRATION_1_2
import com.bluechat.data.local.migrations.MIGRATION_2_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Main database class for the BlueChat application.
 * 
 * Version History:
 * - Version 1: Initial database schema
 * - Version 2: Added isThisDevice, connectionState, lastConnected to devices table
 * - Version 3: Added isDeleted, deletedAt, isEdited, editedAt, replyToMessageId to messages table
 */
@Database(
    entities = [
        MessageEntity::class,
        ConversationEntity::class,
        DeviceEntity::class,
        RoutingTableEntity::class,
        EncryptionKeyEntity::class
    ],
    version = 3, // Current database version
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
    abstract fun deviceDao(): DeviceDao
    abstract fun routingTableDao(): RoutingTableDao
    abstract fun encryptionKeyDao(): EncryptionKeyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        // Migration strategy:
        // 1. For development: Use fallbackToDestructiveMigration()
        // 2. For production: Add all migrations explicitly
        // 3. For testing: Use in-memory database
        
        private const val DATABASE_NAME = "bluechat_database.db"

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = createDatabase(context)
                INSTANCE = instance
                instance
            }
        }
        
        private fun createDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Pre-populate with initial data if needed
                    CoroutineScope(Dispatchers.IO).launch {
                        getDatabase(context).apply {
                            // Initialize with default data if needed
                        }
                    }
                }
            })
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
                // Add new migrations here as needed
            )
            // For development only - remove for production
            .fallbackToDestructiveMigration()
            // For production, use:
            // .fallbackToDestructiveMigrationFrom(/* versions to allow data loss */)
            .build()
        }
        
        /**
         * For testing purposes - creates an in-memory database
         */
        fun getTestDatabase(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                AppDatabase::class.java
            )
            .allowMainThreadQueries()
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
    
    /**
     * Call this when the app is updated to perform any necessary maintenance
     */
    suspend fun onAppUpdated(oldVersionCode: Int, newVersionCode: Int) {
        // Example: Clean up old data when app is updated
        if (oldVersionCode < 2) {
            // Clean up old data if needed
        }
    }
} com.bluechat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bluechat.data.local.dao.ChatDao
import com.bluechat.data.local.dao.MessageDao
import com.bluechat.data.local.dao.UserDao
import com.bluechat.data.local.entity.ChatEntity
import com.bluechat.data.local.entity.MessageEntity
import com.bluechat.data.local.entity.UserEntity
import com.bluechat.data.local.util.Converters
import javax.inject.Singleton

@Database(
    entities = [
        UserEntity::class,
        ChatEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
@Singleton
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bluechat_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
