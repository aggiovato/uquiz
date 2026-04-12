package com.uquiz.android.data.stats.query

/** Resultado de [com.uquiz.android.data.stats.dao.QuestionStatsDao.getUserMasterySummary]: resumen de mastery global del usuario. */
data class UserMasterySummaryRow(
    val masteredQuestions: Int,
    val trackedQuestions: Int,
)
