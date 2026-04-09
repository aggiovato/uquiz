package com.uquiz.android.core.reminder.model

import com.uquiz.android.domain.ranking.enums.UserRank

data class ReminderNotificationContext(
    val languageCode: String,
    val streak: Int,
    val points: Long,
    val rank: UserRank,
    val unfinishedPackTitle: String? = null,
    val remainingQuestions: Int? = null,
    val nextQuestionIndex: Int? = null,
)
