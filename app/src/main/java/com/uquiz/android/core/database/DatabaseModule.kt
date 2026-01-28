package com.uquiz.android.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uquiz.android.data.local.db.UQuizDatabase
import com.uquiz.android.data.local.db.migrations.MIGRATION_1_2
import com.uquiz.android.data.local.db.seeders.DatabaseSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Database Module - Singleton for UQuizDatabase instance
 *
 * Manages:
 * - Database creation with migrations
 * - Initial seeding for development
 * - Singleton pattern for app-wide access
 */
object DatabaseModule {

    @Volatile
    private var INSTANCE: UQuizDatabase? = null

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Get database instance (singleton)
     *
     * @param context Application context
     * @param enableSeeding Enable seed data on first creation (default: true for dev)
     * @return UQuizDatabase instance
     */
    fun getDatabase(context: Context, enableSeeding: Boolean = true): UQuizDatabase {
        return INSTANCE ?: synchronized(this) {
            val builder = Room.databaseBuilder(
                context.applicationContext,
                UQuizDatabase::class.java,
                "uquiz_database"
            )
                .addMigrations(MIGRATION_1_2)
                // Fallback destructive for development (change to false in production)
                .fallbackToDestructiveMigration()

            // Add seeding callback if enabled
            if (enableSeeding) {
                builder.addCallback(seedCallback)
            }

            val instance = builder.build()

            // CRITICAL: Force database creation (Room is lazy)
            // This triggers onCreate callback which runs the seeder
            applicationScope.launch {
                instance.openHelper.writableDatabase.close()
            }

            INSTANCE = instance
            instance
        }
    }

    /**
     * Callback to seed database on creation
     */
    private val seedCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Execute seeding in background
            applicationScope.launch {
                seedDatabase(db)
            }
        }
    }

    /**
     * Seed database with initial data
     */
    private suspend fun seedDatabase(db: SupportSQLiteDatabase) {
        try {
            DatabaseSeeder.seed(db)
            println("✅ Database seeded successfully")
        } catch (e: Exception) {
            println("❌ Error seeding database: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Close database instance (for testing)
     */
    fun closeDatabase() {
        INSTANCE?.close()
        INSTANCE = null
    }
}
