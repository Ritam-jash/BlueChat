package com.bluechat.data.local.migrations

import android.content.Context
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.bluechat.data.local.AppDatabase
import org.junit.Rule
import java.io.IOException

class MigrationTestHelper {
    companion object {
        private const val TEST_DB = "migration-test"
    }

    @JvmField
    @Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Throws(IOException::class)
    fun <T> testMigration(
        startVersion: Int,
        endVersion: Int,
        crossValidation: (db: AppDatabase) -> T
    ): T {
        // Create the database with version startVersion
        val db = helper.createDatabase(TEST_DB, startVersion).apply {
            close()
        }

        // Re-open the database with the next version and provide MIGRATION_1_2
        helper.runMigrationsAndValidate(
            TEST_DB,
            endVersion,
            true,
            when (startVersion) {
                1 -> MIGRATION_1_2
                2 -> MIGRATION_2_3
                else -> throw IllegalArgumentException("No migration defined for version $startVersion")
            }
        )

        // MigrationTestHelper verifies the schema changes
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appDb = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_3
        ).build()

        // Return the database for further testing
        return crossValidation(appDb).also {
            appDb.close()
        }
    }
}
