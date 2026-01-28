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
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Drop all existing tables (destructive migration for initial dev)
        db.execSQL("DROP TABLE IF EXISTS pack_questions")
        db.execSQL("DROP TABLE IF EXISTS options")
        db.execSQL("DROP TABLE IF EXISTS questions")
        db.execSQL("DROP TABLE IF EXISTS packs")

        // Create folders table (new)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS folders (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                parentId TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                FOREIGN KEY(parentId) REFERENCES folders(id) ON DELETE CASCADE
            )
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_folders_parentId ON folders(parentId)")

        // Create packs table (modified)
        db.execSQL("""
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
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_packs_folderId ON packs(folderId)")

        // Create questions table (modified)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS questions (
                id TEXT PRIMARY KEY NOT NULL,
                text TEXT NOT NULL,
                explanation TEXT,
                difficulty TEXT NOT NULL DEFAULT 'MEDIUM',
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Create options table (modified)
        db.execSQL("""
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
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_options_questionId ON options(questionId)")

        // Create pack_questions table (modified)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS pack_questions (
                packId TEXT NOT NULL,
                questionId TEXT NOT NULL,
                sortOrder INTEGER NOT NULL,
                PRIMARY KEY(packId, questionId),
                FOREIGN KEY(packId) REFERENCES packs(id) ON DELETE CASCADE,
                FOREIGN KEY(questionId) REFERENCES questions(id) ON DELETE CASCADE
            )
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_pack_questions_packId ON pack_questions(packId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_pack_questions_questionId ON pack_questions(questionId)")

        // Create attempts table (new)
        db.execSQL("""
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
        """)

        // Create attempt_packs table (new)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS attempt_packs (
                attemptId TEXT NOT NULL,
                packId TEXT NOT NULL,
                PRIMARY KEY(attemptId, packId),
                FOREIGN KEY(attemptId) REFERENCES attempts(id) ON DELETE CASCADE,
                FOREIGN KEY(packId) REFERENCES packs(id) ON DELETE CASCADE
            )
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_packs_attemptId ON attempt_packs(attemptId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_packs_packId ON attempt_packs(packId)")

        // Create attempt_answers table (new)
        db.execSQL("""
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
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_answers_attemptId ON attempt_answers(attemptId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_answers_questionId ON attempt_answers(questionId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_attempt_answers_pickedOptionId ON attempt_answers(pickedOptionId)")
    }
}
