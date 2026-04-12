package com.uquiz.android.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uquiz.android.data.attempts.dao.AttemptAnswerDao
import com.uquiz.android.data.attempts.dao.AttemptDao
import com.uquiz.android.data.attempts.dao.AttemptPackDao
import com.uquiz.android.data.attempts.dao.AttemptQuestionPlanDao
import com.uquiz.android.data.content.dao.FolderDao
import com.uquiz.android.data.content.dao.OptionDao
import com.uquiz.android.data.content.dao.PackDao
import com.uquiz.android.data.content.dao.PackQuestionDao
import com.uquiz.android.data.stats.dao.PackStatsDao
import com.uquiz.android.data.content.dao.QuestionDao
import com.uquiz.android.data.stats.dao.QuestionStatsDao
import com.uquiz.android.data.ranking.dao.UserRankDao
import com.uquiz.android.data.user.dao.UserProfileDao
import com.uquiz.android.data.attempts.entity.AttemptAnswerEntity
import com.uquiz.android.data.attempts.entity.AttemptEntity
import com.uquiz.android.data.attempts.entity.AttemptPackEntity
import com.uquiz.android.data.attempts.entity.AttemptQuestionPlanEntity
import com.uquiz.android.data.content.entity.FolderEntity
import com.uquiz.android.data.content.entity.OptionEntity
import com.uquiz.android.data.content.entity.PackEntity
import com.uquiz.android.data.content.entity.PackQuestionEntity
import com.uquiz.android.data.stats.entity.PackStatsEntity
import com.uquiz.android.data.stats.entity.QuestionStatsEntity
import com.uquiz.android.data.content.entity.QuestionEntity
import com.uquiz.android.data.ranking.entity.UserRankEntity
import com.uquiz.android.data.user.entity.UserProfileEntity

/**
 * Base de datos Room de UQuiz. Declara todas las entidades y expone los DAOs de cada slice.
 * No contiene lógica de negocio; toda la orquestación ocurre en los repositorios.
 */
@Database(
    entities = [
        FolderEntity::class,
        PackEntity::class,
        QuestionEntity::class,
        OptionEntity::class,
        PackQuestionEntity::class,
        AttemptEntity::class,
        AttemptPackEntity::class,
        AttemptAnswerEntity::class,
        AttemptQuestionPlanEntity::class,
        UserProfileEntity::class,
        QuestionStatsEntity::class,
        PackStatsEntity::class,
        UserRankEntity::class
    ],
    version = 12,
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
    abstract fun attemptQuestionPlanDao(): AttemptQuestionPlanDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun questionStatsDao(): QuestionStatsDao
    abstract fun packStatsDao(): PackStatsDao
    abstract fun userRankDao(): UserRankDao
}
