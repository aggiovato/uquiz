package com.uquiz.android.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uquiz.android.data.local.dao.AttemptAnswerDao
import com.uquiz.android.data.local.dao.AttemptDao
import com.uquiz.android.data.local.dao.AttemptPackDao
import com.uquiz.android.data.local.dao.FolderDao
import com.uquiz.android.data.local.dao.OptionDao
import com.uquiz.android.data.local.dao.PackDao
import com.uquiz.android.data.local.dao.PackQuestionDao
import com.uquiz.android.data.local.dao.QuestionDao
import com.uquiz.android.data.local.entity.AttemptAnswerEntity
import com.uquiz.android.data.local.entity.AttemptEntity
import com.uquiz.android.data.local.entity.AttemptPackEntity
import com.uquiz.android.data.local.entity.FolderEntity
import com.uquiz.android.data.local.entity.OptionEntity
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.data.local.entity.PackQuestionEntity
import com.uquiz.android.data.local.entity.QuestionEntity

@Database(
    entities = [
        FolderEntity::class,
        PackEntity::class,
        QuestionEntity::class,
        OptionEntity::class,
        PackQuestionEntity::class,
        AttemptEntity::class,
        AttemptPackEntity::class,
        AttemptAnswerEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class UQuizDatabase : RoomDatabase() {
    // Content DAOs
    abstract fun folderDao(): FolderDao
    abstract fun packDao(): PackDao
    abstract fun questionDao(): QuestionDao
    abstract fun optionDao(): OptionDao
    abstract fun packQuestionDao(): PackQuestionDao

    // Attempt DAOs
    abstract fun attemptDao(): AttemptDao
    abstract fun attemptPackDao(): AttemptPackDao
    abstract fun attemptAnswerDao(): AttemptAnswerDao
}