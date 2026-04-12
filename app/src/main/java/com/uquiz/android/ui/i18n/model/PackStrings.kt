package com.uquiz.android.ui.i18n.model

/** Textos exclusivos de la pantalla de detalle de pack (PackScreen y sus componentes). */
data class PackStrings(
    val noQuestionsYet: String,
    val newQuestion: String,
    val questionsSection: (Int) -> String,
    val filterQuestionsHint: String,
)
