package com.uquiz.android.ui.feature.question.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.ui.designsystem.components.chips.UToggleChip
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### DifficultySelector
 *
 * Fila de chips que permite seleccionar el nivel de dificultad de una pregunta.
 * El chip activo se muestra relleno con navy; los inactivos con contorno neutro.
 *
 * @param selected Nivel de dificultad actualmente seleccionado.
 * @param onSelected Se invoca al seleccionar un nuevo nivel.
 */
@Composable
fun DifficultySelector(
    selected: DifficultyLevel,
    onSelected: (DifficultyLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val items = listOf(
        DifficultyLevel.EASY to strings.difficultyEasy,
        DifficultyLevel.MEDIUM to strings.difficultyMedium,
        DifficultyLevel.HARD to strings.difficultyHard,
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { (difficulty, label) ->
            UToggleChip(
                label = label,
                isActive = difficulty == selected,
                onClick = { onSelected(difficulty) },
            )
        }
    }
}

@UPreview
@Composable
private fun DifficultySelectorPreview() {
    UTheme {
        DifficultySelector(
            selected = DifficultyLevel.MEDIUM,
            onSelected = {},
        )
    }
}
