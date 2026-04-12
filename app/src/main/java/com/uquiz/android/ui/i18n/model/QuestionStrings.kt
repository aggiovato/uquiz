package com.uquiz.android.ui.i18n.model

/** Textos exclusivos del editor de preguntas (QuestionScreen y sus componentes). */
data class QuestionStrings(
    val questionEditorTitle: String,
    val newQuestionTitle: String,
    val editQuestionTitle: String,
    val dangerZoneLabel: String,
    val questionMarkdownLabel: String,
    val questionMarkdownHint: String,
    val explanationMarkdownLabel: String,
    val explanationMarkdownHint: String,
    val explanationPreview: String,
    val optionsSectionTitle: String,
    val addOption: String,
    val optionPlaceholder: (Int) -> String,
    val correctOptionLabel: String,
    val difficultySectionTitle: String,
    val deleteQuestionTitle: String,
    val deleteQuestionConfirmMessage: String,
    val deleteQuestionPrimaryMessage: String,
    val deleteQuestionSecondaryMessage: String,
    val deleteQuestionTypeKeywordInstruction: (String) -> String,
    val deleteQuestionKeyword: String,
    val deleteQuestionActionDescription: String,
    val createQuestionActionDescription: String,
    val questionCreatedToast: String,
    val questionDeletedToast: String,
)
