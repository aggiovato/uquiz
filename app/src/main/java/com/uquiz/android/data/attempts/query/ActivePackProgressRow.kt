package com.uquiz.android.data.attempts.query

import com.uquiz.android.domain.attempts.enums.AttemptMode

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptDao.observeActivePackProgress]: progreso de intentos activos por pack. */
data class ActivePackProgressRow(
    val attemptId: String,
    val packId: String,
    val mode: AttemptMode,
    val answeredCount: Int,
    val startedAt: Long,
)
