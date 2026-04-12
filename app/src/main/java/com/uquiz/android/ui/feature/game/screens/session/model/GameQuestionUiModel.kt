package com.uquiz.android.ui.feature.game.screens.session.model

import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * ### GameQuestionUiModel
 *
 * Modelo de UI que representa una pregunta dentro de la sesión de Game mode.
 *
 * Propiedades:
 * - [questionId]: identificador único de la pregunta.
 * - [markdownText]: texto de la pregunta en formato Markdown.
 * - [difficulty]: nivel de dificultad de la pregunta.
 * - [options]: lista de opciones de respuesta asociadas.
 * - [timeLimitMs]: tiempo límite pre-calculado para responder esta pregunta, en milisegundos.
 */
data class GameQuestionUiModel(
    val questionId: String,
    val markdownText: String,
    val difficulty: DifficultyLevel,
    val options: List<GameOptionUiModel>,
    val timeLimitMs: Long,
)
