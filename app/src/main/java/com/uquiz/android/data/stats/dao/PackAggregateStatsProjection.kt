package com.uquiz.android.data.stats.dao

data class PackAggregateStatsProjection(
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
    val lastSessionAt: Long?
)
