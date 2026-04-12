package com.uquiz.android.data.stats.query

import com.uquiz.android.domain.attempts.enums.AttemptMode

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptDao.observeRecentCompletedByPack]: actividad reciente completada por pack. */
data class PackRecentActivityRow(
    val attemptId: String,
    val mode: AttemptMode,
    val completedAt: Long,
    val durationMs: Long?,
    val score: Int?,
    val correctAnswers: Int,
    val totalQuestions: Int,
)
