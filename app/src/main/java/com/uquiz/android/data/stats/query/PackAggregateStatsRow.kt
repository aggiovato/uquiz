package com.uquiz.android.data.stats.query

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptDao.observePackAggregateStats] y [com.uquiz.android.data.attempts.dao.AttemptDao.getPackAggregateStats]: estadísticas agregadas completas de un pack. */
data class PackAggregateStatsRow(
    val totalSessions: Int,
    val totalStudySessions: Int,
    val totalGameSessions: Int,
    val totalCorrectAnswers: Int,
    val totalQuestions: Int,
    val totalStudyCorrectAnswers: Int,
    val totalStudyQuestions: Int,
    val totalGameCorrectAnswers: Int,
    val totalGameQuestions: Int,
    val averageDurationMs: Long?,
    val averageStudyDurationMs: Long?,
    val averageGameDurationMs: Long?,
    val bestStudyAccuracyPercent: Int?,
    val bestGameScore: Int?,
    val lastSessionAt: Long?,
)
