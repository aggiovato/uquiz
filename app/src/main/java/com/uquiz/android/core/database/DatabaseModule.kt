package com.uquiz.android.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uquiz.android.data.local.db.UQuizDatabase
import com.uquiz.android.data.local.db.migrations.MIGRATION_10_11
import com.uquiz.android.data.local.db.migrations.MIGRATION_11_12
import com.uquiz.android.data.local.db.migrations.MIGRATION_1_2
import com.uquiz.android.data.local.db.seeders.DatabaseSeeder

/**
 * Database Module - Singleton for UQuizDatabase instance
 *
 * Manages:
 * - Database creation with migrations
 * - Initial seeding for development
 * - Singleton pattern for app-wide access
 */
object DatabaseModule {
    @Suppress("ktlint:standard:property-naming")
    @Volatile
    private var INSTANCE: UQuizDatabase? = null

    /**
     * Get database instance (singleton)
     *
     * @param context Application context
     * @param enableSeeding Enable seed data on first creation (default: true for dev)
     * @return UQuizDatabase instance
     */
    fun getDatabase(
        context: Context,
        enableSeeding: Boolean = true,
        allowDestructiveMigration: Boolean = false,
    ): UQuizDatabase =
        INSTANCE ?: synchronized(this) {
            val builder =
                Room
                    .databaseBuilder(
                        context.applicationContext,
                        UQuizDatabase::class.java,
                        "uquiz_database",
                    ).addMigrations(MIGRATION_1_2, MIGRATION_10_11, MIGRATION_11_12)

            // Activar solo en desarrollo: borra y recrea la DB si una migración falla.
            // En release debe ser false para no borrar datos del usuario silenciosamente.
            if (allowDestructiveMigration) {
                builder.fallbackToDestructiveMigration(false)
            }

            if (enableSeeding) {
                builder.addCallback(seedCallback)
            }

            val instance = builder.build()
            INSTANCE = instance
            instance
        }

    /**
     * Seed runs synchronously inside the Room callback so it completes before
     * any DAO call can race against it. The callback fires on the thread that
     * first opens the database (Dispatchers.IO via the first DAO suspend call),
     * so blocking here is safe.
     */
    private val seedCallback =
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                seedDatabase(db)
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                seedDatabase(db)
            }
        }

    private fun seedDatabase(db: SupportSQLiteDatabase) {
        try {
            DatabaseSeeder.seed(db)
        } catch (e: Exception) {
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
