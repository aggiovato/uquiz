package com.uquiz.android.core.analytics.model

data class SessionPerformanceSnapshot(
    val sessionPerformance: Float,
    val totalAnswers: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timeoutAnswers: Int,
    val normalizedScore: Int,
)
