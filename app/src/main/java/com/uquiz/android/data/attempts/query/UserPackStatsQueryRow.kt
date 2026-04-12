package com.uquiz.android.data.attempts.query

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptDao.getUserPackStatsRows]: estadísticas de pack por sesión. */
data class UserPackStatsQueryRow(
    val packId: String,
    val title: String,
    val sessions: Int,
    val accuracyPercent: Int?,
    val averageDurationMs: Long?,
    val progressPercent: Int,
)
