package com.uquiz.android.core.reminder.copy

import com.uquiz.android.domain.ranking.enums.UserRank

val ReminderCopyEn = ReminderCopySet(
    genericTitles = listOf(
        "Time to practice",
        "Ready for a quick round?",
        "Your next session is waiting"
    ),
    genericBodies = listOf(
        ReminderBodyTemplate { "Keep your streak going — a quick session is waiting." },
        ReminderBodyTemplate { "Just a few minutes of practice can make a difference." },
        ReminderBodyTemplate { "Come back for one quick session." }
    ),
    streakTitles = listOf(
        "Keep your streak alive",
        "You're on a roll"
    ),
    streakBodies = listOf(
        ReminderBodyTemplate { "${it.streak} days in a row. Let's make it ${it.streak + 1}." },
        ReminderBodyTemplate { "One quick session keeps your ${it.streak}-day streak safe." }
    ),
    progressTitles = listOf(
        "Keep your progress moving",
        "Let's level up",
        "XP is waiting"
    ),
    progressBodies = listOf(
        ReminderBodyTemplate { "You're at ${it.points} pts — one session could push you further." },
        ReminderBodyTemplate { "Rank ${reminderCopyEnRankLabel(it.rank)} looks good on you — want to push higher?" }
    ),
    resumePackTitles = listOf(
        "Pick up where you left off",
        "Your next session is waiting"
    ),
    resumePackBodies = listOf(
        ReminderBodyTemplate { "Resume \"${it.unfinishedPackTitle}\" — you still have ${it.remainingQuestions ?: 0} questions left." },
        ReminderBodyTemplate { "\"${it.unfinishedPackTitle}\" is waiting from question ${(it.nextQuestionIndex ?: 0) + 1}." }
    ),
    rankLabel = ::reminderCopyEnRankLabel
)

private fun reminderCopyEnRankLabel(rank: UserRank): String = when (rank) {
    UserRank.INITIATE -> "Initiate"
    UserRank.NEOPHYTE -> "Neophyte"
    UserRank.ACOLYTE -> "Acolyte"
    UserRank.DISCIPLE -> "Disciple"
    UserRank.ADEPT -> "Adept"
    UserRank.VIRTUOSO -> "Virtuoso"
    UserRank.ARCHON -> "Archon"
    UserRank.PARAGON -> "Paragon"
}
