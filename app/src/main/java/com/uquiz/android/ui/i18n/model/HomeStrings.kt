package com.uquiz.android.ui.i18n.model

/** Textos exclusivos de la pantalla de inicio (HomeScreen y sus componentes). */
data class HomeStrings(
    val homeGreeting: (String) -> String,
    val homeReadyPrompt: String,
    val homeDayStreakLabel: String,
    val homeScoreLabel: String,
    val homeTotalXpLabel: String,
    val homeContinuePlaying: String,
    val homeContinueStudying: String,
    val homeRandomPlay: String,
    val homeRandomStudy: String,
    val homeNextLevelLabel: String,
    val homePointsToUnlockShort: (Int) -> String,
    val homePointsToUnlockRank: (Int, String) -> String,
    val homeMaxRankUnlocked: String,
)
