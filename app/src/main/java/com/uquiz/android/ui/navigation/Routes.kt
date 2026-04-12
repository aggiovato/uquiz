package com.uquiz.android.ui.navigation

object Routes {
    const val HOME = "home"
    const val LIBRARY = "library"
    const val GAME = "game"
    const val STATS = "stats"
    const val FOLDER = "folder"
    const val PACK = "pack"
    const val STUDY = "study"
    const val STUDY_SESSION = "study_session"
    const val STUDY_SUMMARY = "study_summary"
    const val GAME_INTRO = "game_intro"
    const val GAME_SESSION = "game_session"
    const val GAME_SUMMARY = "game_summary"
    const val QUESTION_CREATE = "question_create"
    const val QUESTION_EDIT = "question_edit"
    const val PACK_STATS = "pack_stats"
    const val PACK_STATS_HELP = "pack_stats_help"
    const val PREFERENCES = "preferences"

    const val ARG_FOLDER_ID = "folderId"
    const val ARG_PACK_ID = "packId"
    const val ARG_QUESTION_ID = "questionId"
    const val ARG_ATTEMPT_ID = "attemptId"
    const val ARG_XP_GAINED = "xpGained"
    const val ARG_PREVIOUS_RANK_ORDINAL = "previousRankOrdinal"
    const val ARG_CURRENT_RANK_ORDINAL = "currentRankOrdinal"
    const val ARG_PREVIOUS_MMR_INT = "previousMmrInt"

    fun folderRoute(folderId: String) = "$FOLDER/$folderId"

    fun packRoute(packId: String) = "$PACK/$packId"

    fun studyRoute(packId: String) = "$STUDY/$packId"

    fun studySessionRoute(packId: String) = "$STUDY_SESSION/$packId"

    fun studySummaryRoute(attemptId: String) = "$STUDY_SUMMARY/$attemptId"

    fun gameIntroRoute(packId: String) = "$GAME_INTRO/$packId"

    fun gameSessionRoute(packId: String) = "$GAME_SESSION/$packId"

    fun gameSummaryRoute(
        attemptId: String,
        xpGained: Long,
        previousRankOrdinal: Int,
        currentRankOrdinal: Int,
        previousMmrInt: Int,
    ) = "$GAME_SUMMARY/$attemptId/$xpGained/$previousRankOrdinal/$currentRankOrdinal/$previousMmrInt"

    fun questionCreateRoute(packId: String) = "$QUESTION_CREATE/$packId"

    fun questionEditRoute(
        packId: String,
        questionId: String,
    ) = "$QUESTION_EDIT/$packId/$questionId"

    fun packStatsRoute(packId: String) = "$PACK_STATS/$packId"

    fun packStatsHelpRoute(packId: String) = "$PACK_STATS_HELP/$packId"

    /**
     * Patrones de ruta con marcadores de posición para [androidx.navigation.NavHost] y [NavigationContextResolver].
     * Cada constante corresponde a la ruta dinámica del destino del mismo nombre.
     */
    object Patterns {
        const val FOLDER = "${Routes.FOLDER}/{${Routes.ARG_FOLDER_ID}}"
        const val PACK = "${Routes.PACK}/{${Routes.ARG_PACK_ID}}"
        const val STUDY = "${Routes.STUDY}/{${Routes.ARG_PACK_ID}}"
        const val STUDY_SESSION = "${Routes.STUDY_SESSION}/{${Routes.ARG_PACK_ID}}"
        const val STUDY_SUMMARY = "${Routes.STUDY_SUMMARY}/{${Routes.ARG_ATTEMPT_ID}}"
        const val GAME_INTRO = "${Routes.GAME_INTRO}/{${Routes.ARG_PACK_ID}}"
        const val GAME_SESSION = "${Routes.GAME_SESSION}/{${Routes.ARG_PACK_ID}}"
        const val GAME_SUMMARY =
            "${Routes.GAME_SUMMARY}/{${Routes.ARG_ATTEMPT_ID}}/{${Routes.ARG_XP_GAINED}}/{${Routes.ARG_PREVIOUS_RANK_ORDINAL}}/{${Routes.ARG_CURRENT_RANK_ORDINAL}}/{${Routes.ARG_PREVIOUS_MMR_INT}}"
        const val QUESTION_CREATE = "${Routes.QUESTION_CREATE}/{${Routes.ARG_PACK_ID}}"
        const val QUESTION_EDIT = "${Routes.QUESTION_EDIT}/{${Routes.ARG_PACK_ID}}/{${Routes.ARG_QUESTION_ID}}"
        const val PACK_STATS = "${Routes.PACK_STATS}/{${Routes.ARG_PACK_ID}}"
        const val PACK_STATS_HELP = "${Routes.PACK_STATS_HELP}/{${Routes.ARG_PACK_ID}}"
    }
}
