package com.uquiz.android.data.attempts.query

/** Resultado de [com.uquiz.android.data.attempts.dao.AttemptAnswerDao.getFastestQuestion]
 * y [com.uquiz.android.data.attempts.dao.AttemptAnswerDao.getMostFailedQuestion]. */
data class UserQuestionInsightRow(
    val questionId: String,
    val questionText: String,
    val value: Long,
)
