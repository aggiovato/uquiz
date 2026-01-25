package com.uquiz.android.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uquiz.android.data.local.dao.PackDao
import com.uquiz.android.data.local.dao.QuestionDao
import com.uquiz.android.data.local.entity.OptionEntity
import com.uquiz.android.data.local.entity.PackEntity
import com.uquiz.android.data.local.entity.PackQuestionEntity
import com.uquiz.android.data.local.entity.QuestionEntity

@Database(
    entities = [
        QuestionEntity::class,
        OptionEntity::class,
        PackEntity::class,
        PackQuestionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class UQuizDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun packDao(): PackDao
}