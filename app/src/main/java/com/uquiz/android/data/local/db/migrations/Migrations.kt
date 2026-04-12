package com.uquiz.android.data.local.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from version 1 to version 2.
 *
 * DESTRUCTIVE MIGRATION for initial development phase.
 * This drops all tables and recreates them with the new schema.
 *
 * Changes:
 * - Added FolderEntity (new)
 * - Added AttemptEntity, AttemptPackEntity, AttemptAnswerEntity (new)
 * - Modified PackEntity: added folderId (FK), description, icon, audit timestamps; renamed name->title
 * - Modified QuestionEntity: added explanation, difficulty, audit timestamps; removed correctOptionIds
 * - Modified OptionEntity: added label, isCorrect, position, audit timestamps
 * - Modified PackQuestionEntity: added Foreign Keys (CASCADE); renamed position->sortOrder
 */
val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Drop all existing tables (destructive migration for initial dev)
            db.execSQL("DROP TABLE IF EXISTS pack_questions")
            db.execSQL("DROP TABLE IF EXISTS options")
            db.execSQL("DROP TABLE IF EXISTS questions")
            db.execSQL("DROP TABLE IF EXISTS packs")

            // Create folders table (new)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS folders (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                parentId TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                FOREIGN KEY(parentId) REFERENCES folders(id) ON DELETE CASCADE
            )
        """,
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS index_folders_parentId ON folders(parentId)")

            // Create packs table (modified)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS packs (
                id TEXT PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                description TEXT,
                folderId TEXT NOT NULL,
                icon TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                FOREIGN KEY(folderId) REFERENCES folders(id) ON DELETE CASCADE
            )
        """,
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS index_packs_folderId ON packs(folderId)")

            // Create questions table (modified)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS questions (
                id TEXT PRIMARY KEY NOT NULL,
                text TEXT NOT NULL,
                explanation TEXT,
                difficulty TEXT NOT NULL DEFAULT 'MEDIUM',
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """,
            )

            // Create options table (modified)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS options (
                id TEXT PRIMARY KEY NOT NULL,
                questionId TEXT NOT NULL,
                label TEXT NOT NULL,
                text TEXT NOT NULL,
                isCorrect INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                FOREIGN KEY(questionId) REFERENCES questions(id) ON DELETE CASCADE
            )
        """,
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS index_options_questionId ON options(questionId)")

            // Create pack_questions table (modified)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS pack_questions (
                packId TEXT NOT NULL,
                questionId TEXT NOT NULL,
                sortOrder INTEGER NOT NULL,
                PRIMARY KEY(packId, questionId),
                FOREIGN KEY(packId) REFERENCES packs(id) ON DELETE CASCADE,
                FOREIGN KEY(questionId) REFERENCES questions(id) ON DELETE CASCADE
            )
        """,
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS index_pack_questions_packId ON pack_questions(packId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_pack_questions_questionId ON pack_questions(questionId)")

            // Create attempts table (new)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS attempts (
                id TEXT PRIMARY KEY NOT NULL,
                mode TEXT NOT NULL,
                startedAt INTEGER NOT NULL,
                completedAt INTEGER,
                durationMs INTEGER,
                score INTEGER NOT NULL DEFAULT 0,
                primaryPackId TEXT,
                totalQuestions INTEGER NOT NULL DEFAULT 0,
                correctAnswers INTEGER NOT NULL DEFAULT 0,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """,
            )

            // Create attempt_packs table (new)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS attempt_packs (
                attemptId TEXT NOT NULL,
                packId TEXT NOT NULL,
                PRIMARY KEY(attemptId, packId),
                FOREIGN KEY(attemptId) REFERENCES attempts(id) ON DELETE CASCADE,
                FOREIGN KEY(packId) REFERENCES packs(id) ON DELETE CASCADE
            )
        """,
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_packs_attemptId ON attempt_packs(attemptId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_packs_packId ON attempt_packs(packId)")

            // Create attempt_answers table (new)
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS attempt_answers (
                id TEXT PRIMARY KEY NOT NULL,
                attemptId TEXT NOT NULL,
                questionId TEXT NOT NULL,
                pickedOptionId TEXT,
                isCorrect INTEGER NOT NULL,
                timeMs INTEGER NOT NULL,
                timeLimitMs INTEGER,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                FOREIGN KEY(attemptId) REFERENCES attempts(id) ON DELETE CASCADE,
                FOREIGN KEY(questionId) REFERENCES questions(id) ON DELETE CASCADE,
                FOREIGN KEY(pickedOptionId) REFERENCES options(id) ON DELETE SET NULL
            )
        """,
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_answers_attemptId ON attempt_answers(attemptId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_answers_questionId ON attempt_answers(questionId)")
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS index_attempt_answers_pickedOptionId ON attempt_answers(pickedOptionId)",
            )
        }
    }

/**
 * Migration from version 10 to version 11.
 *
 * Adds CHECK constraints on content name/description lengths:
 * - `folders.name`: max 100 characters
 * - `packs.title`: max 100 characters
 * - `packs.description`: max 200 characters (nullable, only checked when non-null)
 *
 * SQLite does not support ALTER TABLE ADD CONSTRAINT, so each table is recreated
 * following the standard copy-drop-rename pattern with foreign keys disabled.
 */

/**
 * Migration from version 11 to version 12.
 *
 * Adds table `attempt_question_plan` to persist the ordered question plan for Game mode
 * sessions. Each row represents one question slot within an attempt, with its pre-calculated
 * time limit. This allows the session to be resumed without recalculating the plan.
 */
val MIGRATION_11_12 =
    object : Migration(11, 12) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS attempt_question_plan (
                    attemptId TEXT NOT NULL,
                    questionId TEXT NOT NULL,
                    orderIndex INTEGER NOT NULL,
                    timeLimitMs INTEGER NOT NULL,
                    PRIMARY KEY(attemptId, questionId)
                )
                """.trimIndent(),
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS index_attempt_question_plan_attemptId " +
                    "ON attempt_question_plan(attemptId)",
            )
        }
    }

val MIGRATION_10_11 =
    object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = OFF")

            // --- folders ---
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS folders_new (
                    id TEXT PRIMARY KEY NOT NULL,
                    name TEXT NOT NULL CHECK(length(name) <= 100),
                    parentId TEXT,
                    colorHex TEXT,
                    icon TEXT,
                    createdAt INTEGER NOT NULL,
                    updatedAt INTEGER NOT NULL,
                    FOREIGN KEY(parentId) REFERENCES folders(id) ON DELETE CASCADE
                )
                """.trimIndent(),
            )
            db.execSQL(
                "INSERT INTO folders_new (id, name, parentId, colorHex, icon, createdAt, updatedAt) " +
                    "SELECT id, name, parentId, colorHex, icon, createdAt, updatedAt FROM folders",
            )
            db.execSQL("DROP TABLE folders")
            db.execSQL("ALTER TABLE folders_new RENAME TO folders")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_folders_parentId ON folders(parentId)")

            // --- packs ---
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS packs_new (
                    id TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL CHECK(length(title) <= 100),
                    description TEXT CHECK(description IS NULL OR length(description) <= 200),
                    folderId TEXT NOT NULL,
                    icon TEXT,
                    colorHex TEXT,
                    createdAt INTEGER NOT NULL,
                    updatedAt INTEGER NOT NULL,
                    FOREIGN KEY(folderId) REFERENCES folders(id) ON DELETE CASCADE
                )
                """.trimIndent(),
            )
            db.execSQL(
                "INSERT INTO packs_new (id, title, description, folderId, icon, colorHex, createdAt, updatedAt) " +
                    "SELECT id, title, description, folderId, icon, colorHex, createdAt, updatedAt FROM packs",
            )
            db.execSQL("DROP TABLE packs")
            db.execSQL("ALTER TABLE packs_new RENAME TO packs")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_packs_folderId ON packs(folderId)")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_packs_updatedAt ON packs(updatedAt)")

            db.execSQL("PRAGMA foreign_keys = ON")
        }
    }
