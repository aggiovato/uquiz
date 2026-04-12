package com.uquiz.android.ui.navigation.components

import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.AppStrings

/**
 * Representa una opción de idioma disponible en la navegación superior.
 *
 * @param code Código ISO corto del idioma.
 * @param name Función que resuelve el nombre localizado.
 * @param flagRes Recurso drawable de la bandera.
 */
data class ULanguageOption(
    val code: String,
    val name: (AppStrings) -> String,
    val flagRes: Int,
)

/** Lista de idiomas disponibles para los selectores de la app. */
val uAppLanguages = listOf(
    ULanguageOption("en", { it.preferences.langEnglish }, UIcons.Flags.English),
    ULanguageOption("es", { it.preferences.langSpanish }, UIcons.Flags.Spanish),
    ULanguageOption("it", { it.preferences.langItalian }, UIcons.Flags.Italian),
    ULanguageOption("ja", { it.preferences.langJapanese }, UIcons.Flags.Japanese),
)
