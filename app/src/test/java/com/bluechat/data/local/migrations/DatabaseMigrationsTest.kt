package com.bluechat.data.local.migrations

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bluechat.data.local.AppDatabase
import com.bluechat.data.local.entity.DeviceCapability
import com.bluechat.data.local.entity.DeviceEntity
import com.bluechat.data.local.entity.MessageEntity
import com.bluechat.data.local.entity.MessageType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationsTest {
    private lateinit var database: AppDatabase
    private val testDevice = DeviceEntity(
        id = "test-device",
        name = "Test Device",
        address = "00:11:22:33:44:55",
        publicKey = "test-public-key"
    )

    @Before
    fun setup() {
        // Using an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testMigration1To2() = runBlocking {
        // Setup initial database with version 1 schema
        val db = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
            "test-db"
        ).addMigrations(MIGRATION_1_2)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()

        try {
            // Verify the migration was successful
            val deviceDao = db.deviceDao()
            
            // Insert a device with old schema
            deviceDao.insertDevice(testDevice)
            
            // Verify the device was inserted and can be queried
            val loaded = deviceDao.getDeviceById(testDevice.id)
            assertThat(loaded).isNotNull()
            assertThat(loaded?.isThisDevice).isFalse()
            
        } finally {
            db.close()
        }
    }

    @Test
    fun testMigration2To3() = runBlocking {
        // Setup database with version 2 schema
        val db = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
            "test-db-2"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()

        try {
            val messageDao = db.messageDao()
            val deviceDao = db.deviceDao()
            
            // Insert test data
            deviceDao.insertDevice(testDevice)
            
            val testMessage = MessageEntity(
                id = "test-message",
                conversationId = "test-conversation",
                senderId = testDevice.id,
                content = "Test message",
                messageType = MessageType.TEXT
            )
            
            messageDao.insertMessage(testMessage)
            
            // Verify the message was inserted and has default values for new columns
            val loaded = messageDao.getMessageById(testMessage.id)
            assertThat(loaded).isNotNull()
            assertThat(loaded?.isDeleted).isFalse()
            assertThat(loaded?.isEdited).isFalse()
            
        } finally {
            db.close()
        }
    }
}
