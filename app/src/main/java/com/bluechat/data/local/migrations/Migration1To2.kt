package com.bluechat.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from version 1 to 2
 * Changes:
 * - Added 'isThisDevice' column to devices table
 * - Added 'connectionState' column to devices table
 * - Added 'lastConnected' timestamp to devices table
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new columns to devices table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `devices_new` (
                `id` TEXT NOT NULL, 
                `name` TEXT NOT NULL, 
                `address` TEXT NOT NULL, 
                `publicKey` TEXT NOT NULL, 
                `lastSeen` INTEGER NOT NULL, 
                `isOnline` INTEGER NOT NULL, 
                `capabilities` TEXT NOT NULL, 
                `rssi` INTEGER NOT NULL DEFAULT 0, 
                `isPaired` INTEGER NOT NULL DEFAULT 0, 
                `alias` TEXT, 
                `connectionState` TEXT NOT NULL DEFAULT 'DISCONNECTED', 
                `lastConnected` INTEGER, 
                `isThisDevice` INTEGER NOT NULL DEFAULT 0, 
                PRIMARY KEY(`id`)
            )
        """.trimIndent())

        // Copy data from old table
        database.execSQL("""
            INSERT INTO `devices_new` 
            (id, name, address, publicKey, lastSeen, isOnline, capabilities, rssi, isPaired, alias)
            SELECT id, name, address, publicKey, lastSeen, isOnline, capabilities, rssi, isPaired, alias
            FROM `devices`
        """.trimIndent())

        // Remove old table
        database.execSQL("DROP TABLE `devices`")
        
        // Rename new table
        database.execSQL("ALTER TABLE `devices_new` RENAME TO `devices`")
        
        // Create index on address
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_devices_address` ON `devices` (`address`)")
    }
}
