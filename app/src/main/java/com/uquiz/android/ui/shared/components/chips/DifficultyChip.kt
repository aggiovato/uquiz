package com.uquiz.android.ui.shared.components.chips

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.ui.designsystem.tokens.Gold100
import com.uquiz.android.ui.designsystem.tokens.Gold200
import com.uquiz.android.ui.designsystem.tokens.Gold500
import com.uquiz.android.ui.designsystem.tokens.Gold700
import com.uquiz.android.ui.designsystem.tokens.Red100
import com.uquiz.android.ui.designsystem.tokens.Red200
import com.uquiz.android.ui.designsystem.tokens.Red500
import com.uquiz.android.ui.designsystem.tokens.Teal100
import com.uquiz.android.ui.designsystem.tokens.Teal200
import com.uquiz.android.ui.designsystem.tokens.Teal500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.i18n.LocalStrings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/** Variante visual del chip de dificultad. */
enum class DifficultyChipVariant { Outlined, Filled }

private val chipShape = RoundedCornerShape(20.dp)
private val colorSpec = tween<Color>(durationMillis = 180)

private data class DifficultyColors(
    val bg: Color,
    val content: Color,
    val border: Color,
)

private fun difficultyOutlinedColors(difficulty: DifficultyLevel): DifficultyColors = when (difficulty) {
    DifficultyLevel.EASY -> DifficultyColors(Teal100, Teal700, Teal200)
    DifficultyLevel.MEDIUM -> DifficultyColors(Gold100, Gold700, Gold200)
    DifficultyLevel.HARD -> DifficultyColors(Red100, Red500, Red200)
    DifficultyLevel.EXPERT -> DifficultyColors(
        bg = Color(0xFF5B4BB7),
        content = Color(0xFFE8E4FF),
        border = Color(0xFF7C6DC5),
    )
}

private fun difficultyFilledColors(difficulty: DifficultyLevel): DifficultyColors = when (difficulty) {
    DifficultyLevel.EASY -> DifficultyColors(Teal500, Color.White, Color.Transparent)
    DifficultyLevel.MEDIUM -> DifficultyColors(Gold500, Color.White, Color.Transparent)
    DifficultyLevel.HARD -> DifficultyColors(Red500, Color.White, Color.Transparent)
    DifficultyLevel.EXPERT -> DifficultyColors(Color(0xFF5B4BB7), Color.White, Color.Transparent)
}

/**
 * ### DifficultyChip
 *
 * Muestra un chip con la dificultad de una pregunta.
 *
 * @param difficulty Dificultad que se representa.
 * @param variant Estilo visual del chip: outlined (por defecto) o filled (color sólido, texto blanco).
 */
@Composable
fun DifficultyChip(
    difficulty: DifficultyLevel,
    modifier: Modifier = Modifier,
    variant: DifficultyChipVariant = DifficultyChipVariant.Outlined,
) {
    val strings = LocalStrings.current
    val target = when (variant) {
        DifficultyChipVariant.Outlined -> difficultyOutlinedColors(difficulty)
        DifficultyChipVariant.Filled -> difficultyFilledColors(difficulty)
    }
    val bgColor by animateColorAsState(target.bg, colorSpec, label = "diffBg")
    val contentColor by animateColorAsState(target.content, colorSpec, label = "diffContent")
    val borderColor by animateColorAsState(target.border, colorSpec, label = "diffBorder")

    val baseModifier = modifier
        .clip(chipShape)
        .drawBehind { drawRect(bgColor) }

    val chipModifier = if (variant == DifficultyChipVariant.Outlined) {
        baseModifier.border(1.dp, borderColor, chipShape)
    } else {
        baseModifier
    }

    Text(
        text = when (difficulty) {
            DifficultyLevel.EASY -> strings.common.difficultyEasy
            DifficultyLevel.MEDIUM -> strings.common.difficultyMedium
            DifficultyLevel.HARD -> strings.common.difficultyHard
            DifficultyLevel.EXPERT -> strings.common.difficultyExpert
        },
        style = MaterialTheme.typography.labelSmall,
        color = contentColor,
        modifier = chipModifier.padding(horizontal = 10.dp, vertical = 3.dp),
    )
}

@UPreview
@Composable
private fun DifficultyChipPreview() {
    UTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DifficultyChip(difficulty = DifficultyLevel.EASY)
            DifficultyChip(difficulty = DifficultyLevel.MEDIUM)
            DifficultyChip(difficulty = DifficultyLevel.HARD)
            DifficultyChip(difficulty = DifficultyLevel.EXPERT)
        }
    }
}
