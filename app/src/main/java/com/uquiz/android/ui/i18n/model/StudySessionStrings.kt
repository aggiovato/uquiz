package com.uquiz.android.ui.i18n.model

/** Textos exclusivos de la sesión de estudio (StudySessionScreen y sus componentes). */
data class StudySessionStrings(
    val studyQuestionCounter: (Int, Int) -> String,
    val studyPrevious: String,
    val studyNext: String,
    val studyFinish: String,
    val studyCorrectTitle: String,
    val studyIncorrectTitle: String,
    val studyExplanationLabel: String,
    val studyExitStudy: String,
    val studyExitTitle: String,
    val studyExitMessage: String,
)
