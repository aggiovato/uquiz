package com.uquiz.android.ui.i18n.model

/** Textos exclusivos del resumen de partida (GameSummaryScreen y GameCircularRankProgress). */
data class GameSummaryStrings(
    val gameCompleteTitle: String,
    val gameTotalScoreLabel: String,
    val gamePlayAgain: String,
    val gameRankUpTitle: String,
    val gameXpGained: (Long) -> String,
    val gameRankProgressLabel: String,
    val gameNextRankLabel: (Int) -> String,
)
