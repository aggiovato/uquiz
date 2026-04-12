package com.uquiz.android.ui.i18n.model

/** Textos exclusivos de la introducción a una partida (GameIntroScreen). */
data class GameIntroStrings(
    val gameStartGame: String,
    val gameResumeGame: String,
    val gameEstimatedTimeLabel: String,
    val gameAnsweredSoFar: (Int, Int) -> String,
)
