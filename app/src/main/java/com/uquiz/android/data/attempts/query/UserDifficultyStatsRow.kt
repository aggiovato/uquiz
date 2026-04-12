package com.uquiz.android.data.attempts.query

import com.uquiz.android.domain.content.enums.DifficultyLevel

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptAnswerDao.getDifficultyStats]: estadísticas por nivel de dificultad. */
data class UserDifficultyStatsRow(
    val difficulty: DifficultyLevel,
    val answeredCount: Int,
    val correctAnswers: Int,
    val averageTimeMs: Long?,
)
