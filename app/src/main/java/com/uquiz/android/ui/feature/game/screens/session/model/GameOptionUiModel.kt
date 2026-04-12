package com.uquiz.android.ui.feature.game.screens.session.model

/**
 * ### GameOptionUiModel
 *
 * Modelo de UI que representa una opción de respuesta dentro de una pregunta del Game mode.
 *
 * Propiedades:
 * - [optionId]: identificador único de la opción.
 * - [label]: letra de la opción ("A", "B", "C", "D").
 * - [markdownText]: texto de la opción en formato Markdown.
 * - [isCorrect]: indica si esta opción es la correcta.
 */
data class GameOptionUiModel(
    val optionId: String,
    val label: String,
    val markdownText: String,
    val isCorrect: Boolean,
)
