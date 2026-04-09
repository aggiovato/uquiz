package com.uquiz.android.ui.feature.pack.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.ui.designsystem.components.feedback.UDragHandleButton
import com.uquiz.android.ui.designsystem.components.feedback.UMarkdownText
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.shared.components.chips.DifficultyChip

val QuestionListItemMinHeight = 92.dp

@Composable
fun QuestionListItem(
    order: Int,
    markdownText: String,
    difficulty: DifficultyLevel,
    modifier: Modifier = Modifier,
    translationY: Float = 0f,
    isDragging: Boolean = false,
    showDragHandle: Boolean = false,
    onClick: () -> Unit = {},
    onDrag: (Float) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .graphicsLayer { this.translationY = translationY }
                .zIndex(if (isDragging) 1f else 0f),
        verticalAlignment = Alignment.Top,
    ) {
        Card(
            modifier = Modifier.clickable(onClick = onClick),
            shape = RoundedCornerShape(AppRadius),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isDragging) 4.dp else 1.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .padding(top = 6.dp)
                            .size(25.dp)
                            .background(BrandNavy, RoundedCornerShape(AppRadius)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = order.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .heightIn(min = QuestionListItemMinHeight - 28.dp),
                ) {
                    UMarkdownText(
                        markdown = markdownText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink950,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.size(8.dp))
                    DifficultyChip(difficulty = difficulty)
                }

                AnimatedVisibility(
                    visible = showDragHandle,
                    enter = fadeIn(tween(180)) + slideInHorizontally(tween(200)) { it / 2 },
                    exit = fadeOut(tween(150)) + slideOutHorizontally(tween(180)) { it / 2 },
                ) {
                    Box(
                        modifier =
                            Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {},
                                ).pointerInput(showDragHandle) {
                                    if (!showDragHandle) return@pointerInput
                                    detectDragGestures(
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            onDrag(dragAmount.y)
                                        },
                                        onDragEnd = onDragEnd,
                                        onDragCancel = onDragCancel,
                                    )
                                },
                    ) {
                        UDragHandleButton()
                    }
                }
            }
        }
    }
}
