package com.uquiz.android.ui.i18n.model

/** Textos exclusivos de la sesión de juego (GameSessionScreen y GameSessionHeader). */
data class GameSessionStrings(
    val gameScoreLabel: String,
    val gameTimeoutLabel: String,
    val gameCorrectFeedback: (Int) -> String,
    val gameIncorrectFeedback: (Int) -> String,
    val gameExitTitle: String,
    val gameExitMessage: String,
)
