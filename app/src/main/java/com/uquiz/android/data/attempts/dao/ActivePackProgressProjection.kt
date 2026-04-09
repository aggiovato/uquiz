package com.uquiz.android.data.attempts.dao

import com.uquiz.android.domain.attempts.enums.AttemptMode

data class ActivePackProgressProjection(
    val attemptId: String,
    val packId: String,
    val mode: AttemptMode,
    val answeredCount: Int,
    val startedAt: Long
)
