package com.uquiz.android.core.reminder.copy

import com.uquiz.android.domain.ranking.enums.UserRank

val ReminderCopyIt = ReminderCopySet(
    genericTitles = listOf(
        "È il momento di esercitarti",
        "La tua prossima sessione ti aspetta",
        "Facciamo un round veloce?"
    ),
    genericBodies = listOf(
        ReminderBodyTemplate { "Bastano pochi minuti di pratica per fare la differenza." },
        ReminderBodyTemplate { "Il tuo prossimo set di domande è pronto quando vuoi." },
        ReminderBodyTemplate { "Torna per una sessione rapida e continua a crescere." }
    ),
    streakTitles = listOf(
        "Tieni viva la tua streak",
        "Sei in un buon momento"
    ),
    streakBodies = listOf(
        ReminderBodyTemplate { "Sono ${it.streak} giorni di fila. Proviamo ad arrivare a ${it.streak + 1}." },
        ReminderBodyTemplate { "Una sessione veloce tiene al sicuro la tua streak di ${it.streak} giorni." }
    ),
    progressTitles = listOf(
        "Continua a far crescere i progressi",
        "Andiamo al livello successivo",
        "La tua XP ti aspetta"
    ),
    progressBodies = listOf(
        ReminderBodyTemplate { "Sei a ${it.points} pt — una sessione può spingerti ancora più avanti." },
        ReminderBodyTemplate { "Il rango ${reminderCopyItRankLabel(it.rank)} ti sta bene — vuoi salire ancora?" }
    ),
    resumePackTitles = listOf(
        "Riprendi da dove hai lasciato",
        "Il tuo pack incompleto ti aspetta"
    ),
    resumePackBodies = listOf(
        ReminderBodyTemplate { "Riprendi \"${it.unfinishedPackTitle}\" — ti restano ancora ${it.remainingQuestions ?: 0} domande." },
        ReminderBodyTemplate { "\"${it.unfinishedPackTitle}\" ti aspetta dalla domanda ${(it.nextQuestionIndex ?: 0) + 1}." }
    ),
    rankLabel = ::reminderCopyItRankLabel
)

private fun reminderCopyItRankLabel(rank: UserRank): String = when (rank) {
    UserRank.INITIATE -> "Iniziato"
    UserRank.NEOPHYTE -> "Neofita"
    UserRank.ACOLYTE -> "Accolito"
    UserRank.DISCIPLE -> "Discepolo"
    UserRank.ADEPT -> "Adepto"
    UserRank.VIRTUOSO -> "Virtuoso"
    UserRank.ARCHON -> "Arconte"
    UserRank.PARAGON -> "Paragone"
}
