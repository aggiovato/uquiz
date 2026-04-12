package com.uquiz.android.ui.i18n.model

/** Textos exclusivos de la pantalla de introducción al modo estudio (StudyIntroScreen). */
data class StudyIntroStrings(
    val studyIntroTitle: String,
    val studyReadyDescription: String,
    val studyResumeStudy: String,
    val studyStartStudy: String,
    val studyResumeProgress: (Int, Int) -> String,
)
