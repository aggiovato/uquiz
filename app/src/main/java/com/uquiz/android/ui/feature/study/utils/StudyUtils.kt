package com.uquiz.android.ui.feature.study.utils

import androidx.compose.ui.graphics.Color
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.ui.designsystem.tokens.Gold500
import com.uquiz.android.ui.designsystem.tokens.Navy200
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.i18n.AppStrings

/**
 * Devuelve el texto localizado y el color semántico para [difficulty] en el contexto
 * del fondo oscuro del modo estudio.
 *
 * @return Par de (texto i18n, color del sistema de diseño).
 */
internal fun studyDifficultyInfo(
    difficulty: DifficultyLevel,
    strings: AppStrings
): Pair<String, Color> = when (difficulty) {
    DifficultyLevel.EASY -> strings.difficultyEasy to Teal500
    DifficultyLevel.MEDIUM -> strings.difficultyMedium to Gold500
    DifficultyLevel.HARD, DifficultyLevel.EXPERT -> strings.difficultyHard to Navy200
}

/**
 * Normaliza [difficulty] para su uso con [com.uquiz.android.ui.shared.components.chips.DifficultyChip], que no admite la variante EXPERT.
 * EXPERT se colapsa a HARD para mantener coherencia visual en todos los modos de juego.
 */
internal fun mapStudyDifficulty(difficulty: DifficultyLevel): DifficultyLevel =
    when (difficulty) {
        DifficultyLevel.EXPERT -> DifficultyLevel.HARD
        else -> difficulty
    }

/**
 * Formatea una duración en milisegundos como cadena mm:ss.
 *
 * @param durationMs Duración en milisegundos.
 * @return Texto con formato "mm:ss", p.ej. "03:45".
 */
internal fun formatStudyDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
