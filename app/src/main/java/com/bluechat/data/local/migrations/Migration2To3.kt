package com.bluechat.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from version 2 to 3
 * Changes:
 * - Added 'isDeleted' and 'deletedAt' to messages table for soft delete
 * - Added 'isEdited' and 'editedAt' to messages table
 * - Added 'replyToMessageId' to messages table for message threading
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new messages table with additional columns
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `messages_new` (
                `id` TEXT NOT NULL, 
                `conversationId` TEXT NOT NULL, 
                `senderId` TEXT NOT NULL, 
                `content` TEXT NOT NULL, 
                `timestamp` INTEGER NOT NULL, 
                `messageType` TEXT NOT NULL, 
                `isEncrypted` INTEGER NOT NULL, 
                `deliveryStatus` TEXT NOT NULL, 
                `hopCount` INTEGER NOT NULL, 
                `parentMessageId` TEXT, 
                `metadata` TEXT, 
                `isDeleted` INTEGER NOT NULL DEFAULT 0, 
                `deletedAt` INTEGER, 
                `isEdited` INTEGER NOT NULL DEFAULT 0, 
                `editedAt` INTEGER, 
                `replyToMessageId` TEXT, 
                PRIMARY KEY(`id`),
                FOREIGN KEY(`conversationId`) REFERENCES `conversations`(`id`) ON DELETE CASCADE,
                FOREIGN KEY(`senderId`) REFERENCES `devices`(`id`) ON DELETE CASCADE
            )
        """.trimIndent())

        // Copy data from old table
        database.execSQL("""
            INSERT INTO `messages_new` 
            (id, conversationId, senderId, content, timestamp, messageType, 
             isEncrypted, deliveryStatus, hopCount, parentMessageId, metadata,
             isDeleted, isEdited, replyToMessageId)
            SELECT id, conversationId, senderId, content, timestamp, messageType, 
                   isEncrypted, deliveryStatus, hopCount, parentMessageId, metadata,
                   0 as isDeleted, 0 as isEdited, NULL as replyToMessageId
            FROM `messages`
        """.trimIndent())

        // Remove old table
        database.execSQL("DROP TABLE `messages`")
        
        // Rename new table
        database.execSQL("ALTER TABLE `messages_new` RENAME TO `messages`")
        
        // Recreate indices
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_conversationId` ON `messages` (`conversationId`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_senderId` ON `messages` (`senderId`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_timestamp` ON `messages` (`timestamp`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_deliveryStatus` ON `messages` (`deliveryStatus`)")
    }
}
