package com.uquiz.android.core.reminder.copy

import com.uquiz.android.domain.ranking.enums.UserRank

val ReminderCopyEs = ReminderCopySet(
    genericTitles = listOf(
        "Hora de practicar",
        "Tu próxima sesión te espera",
        "¿Una ronda rápida?"
    ),
    genericBodies = listOf(
        ReminderBodyTemplate { "Unos minutos de práctica pueden marcar la diferencia." },
        ReminderBodyTemplate { "Tu siguiente sesión está lista cuando quieras." },
        ReminderBodyTemplate { "Vuelve para una sesión corta y sigue avanzando." }
    ),
    streakTitles = listOf(
        "Protege tu racha",
        "Vas con buen ritmo"
    ),
    streakBodies = listOf(
        ReminderBodyTemplate { "Llevas ${it.streak} días seguidos. Vamos a por el ${it.streak + 1}." },
        ReminderBodyTemplate { "Una sesión rápida mantiene viva tu racha de ${it.streak} días." }
    ),
    progressTitles = listOf(
        "Haz crecer tu progreso",
        "Vamos a subir",
        "Tu XP te espera"
    ),
    progressBodies = listOf(
        ReminderBodyTemplate { "Ya tienes ${it.points} pts — una sesión más puede empujarte todavía más." },
        ReminderBodyTemplate { "Tu rango ${reminderCopyEsRankLabel(it.rank)} va bien contigo — ¿quieres llevarlo más arriba?" }
    ),
    resumePackTitles = listOf(
        "Retoma donde lo dejaste",
        "Tu pack pendiente te espera"
    ),
    resumePackBodies = listOf(
        ReminderBodyTemplate { "Reanuda \"${it.unfinishedPackTitle}\" — todavía te quedan ${it.remainingQuestions ?: 0} preguntas." },
        ReminderBodyTemplate { "\"${it.unfinishedPackTitle}\" te espera desde la pregunta ${(it.nextQuestionIndex ?: 0) + 1}." }
    ),
    rankLabel = ::reminderCopyEsRankLabel
)

private fun reminderCopyEsRankLabel(rank: UserRank): String = when (rank) {
    UserRank.INITIATE -> "Iniciado"
    UserRank.NEOPHYTE -> "Neófito"
    UserRank.ACOLYTE -> "Acólito"
    UserRank.DISCIPLE -> "Discípulo"
    UserRank.ADEPT -> "Adepto"
    UserRank.VIRTUOSO -> "Virtuoso"
    UserRank.ARCHON -> "Arconte"
    UserRank.PARAGON -> "Paragón"
}
