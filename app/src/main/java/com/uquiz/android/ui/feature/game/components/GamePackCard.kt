package com.uquiz.android.ui.feature.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.model.Pack
import com.uquiz.android.domain.stats.projection.PackGameCard
import com.uquiz.android.ui.designsystem.components.buttons.UButtonSize
import com.uquiz.android.ui.designsystem.components.buttons.UButtonTone
import com.uquiz.android.ui.designsystem.components.buttons.UFilledButton
import com.uquiz.android.ui.designsystem.components.cards.UIconBadge
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral200
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.contentAccents
import com.uquiz.android.ui.designsystem.tokens.contentColorFromHex
import com.uquiz.android.ui.designsystem.tokens.contentIconResForKey
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### GamePackCard
 *
 * Tarjeta de pack para la pantalla principal del Game mode.
 * Sigue el mismo estilo visual que las tarjetas de pack en la biblioteca:
 * texto neutro oscuro, misma altura y progreso alineado a la parte inferior.
 *
 * Si existe una partida activa, muestra la barra de progreso bajo la fila principal
 * y el botón cambia a "Continuar".
 *
 * @param packGameCard Datos del pack con su estado de partida activa.
 * @param accentIndex  Índice para elegir el color de acento cuando el pack no tiene color propio.
 * @param onPlayClick  Callback invocado al pulsar "Jugar" o "Continuar".
 */
@Composable
fun GamePackCard(
    packGameCard: PackGameCard,
    accentIndex: Int,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val accent = contentAccents[accentIndex % contentAccents.size]
    val (bgColor, iconColor) = contentColorFromHex(
        hex = packGameCard.pack.colorHex,
        fallback = accent,
    )
    val iconRes = contentIconResForKey(packGameCard.pack.icon)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Neutral100),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                UIconBadge(
                    iconRes = iconRes,
                    backgroundColor = bgColor,
                    contentColor = iconColor,
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = packGameCard.pack.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Ink950,
                        maxLines = 2,
                    )
                    if (packGameCard.questionCount > 0) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = strings.common.questionsLabel(packGameCard.questionCount),
                            style = MaterialTheme.typography.bodySmall,
                            color = Neutral500,
                        )
                    }
                }

                UFilledButton(
                    text = if (packGameCard.hasActiveAttempt) strings.gameHome.gameContinueButton else strings.common.playMode,
                    onClick = onPlayClick,
                    tone = UButtonTone.Brand,
                    size = UButtonSize.Tiny,
                )
            }

            if (packGameCard.hasActiveAttempt) {
                val progress = (packGameCard.answeredCount.toFloat() /
                    packGameCard.questionCount.toFloat()).coerceIn(0f, 1f)
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = strings.common.progressLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${packGameCard.answeredCount} / ${packGameCard.questionCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Neutral500,
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Teal700,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = Teal700,
                    trackColor = Neutral200,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun GamePackCardPreview() {
    UTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GamePackCard(
                packGameCard = PackGameCard(
                    pack = Pack(
                        id = "pack-1",
                        title = "Historia Universal",
                        description = null,
                        folderId = "folder-1",
                        icon = "brain",
                        colorHex = "#134C8F",
                        createdAt = 0L,
                        updatedAt = 0L,
                    ),
                    questionCount = 24,
                    averageDifficulty = DifficultyLevel.MEDIUM,
                    expectedPlayTimeMs = 480_000L,
                ),
                accentIndex = 0,
                onPlayClick = {},
            )
            GamePackCard(
                packGameCard = PackGameCard(
                    pack = Pack(
                        id = "pack-2",
                        title = "Kotlin Coroutines avanzadas con StateFlow",
                        description = null,
                        folderId = "folder-1",
                        icon = "code",
                        colorHex = "#00957F",
                        createdAt = 0L,
                        updatedAt = 0L,
                    ),
                    questionCount = 15,
                    averageDifficulty = DifficultyLevel.HARD,
                    expectedPlayTimeMs = 375_000L,
                    activeAttemptId = "attempt-1",
                    answeredCount = 7,
                ),
                accentIndex = 1,
                onPlayClick = {},
            )
        }
    }
}
