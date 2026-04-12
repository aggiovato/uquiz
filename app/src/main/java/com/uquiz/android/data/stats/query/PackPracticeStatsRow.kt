package com.uquiz.android.data.stats.query

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptDao.observePackPracticeStats]: sesiones y accuracy de práctica por pack y modo. */
data class PackPracticeStatsRow(
    val sessionsCount: Int,
    val accuracyPercent: Int?,
)
