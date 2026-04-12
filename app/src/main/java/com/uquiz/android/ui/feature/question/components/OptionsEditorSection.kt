package com.uquiz.android.ui.feature.question.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.buttons.UIconActionButton
import com.uquiz.android.ui.designsystem.components.chips.UToggleChip
import com.uquiz.android.ui.feature.question.model.EditableOptionUiModel
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.AppRadius
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Ink950
import com.uquiz.android.ui.designsystem.tokens.Neutral300
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Teal200
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.Teal900
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### OptionsEditorSection
 *
 * Sección de edición de opciones de una pregunta. Permite añadir, editar,
 * eliminar y marcar la opción correcta.
 *
 * @param options Lista de opciones editables.
 * @param onOptionTextChange Se invoca al cambiar el texto de una opción.
 * @param onOptionSelected Se invoca al seleccionar una opción como correcta.
 * @param onRemoveOption Se invoca al eliminar una opción.
 * @param onAddOption Se invoca al añadir una nueva opción.
 */
@Composable
fun OptionsEditorSection(
    options: List<EditableOptionUiModel>,
    onOptionTextChange: (String, String) -> Unit,
    onOptionSelected: (String) -> Unit,
    onRemoveOption: (String) -> Unit,
    onAddOption: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = strings.question.optionsSectionTitle,
                style = MaterialTheme.typography.titleSmall,
                color = Neutral700,
            )
            UToggleChip(
                iconRes = UIcons.Actions.Add,
                label = strings.question.addOption,
                isActive = true,
                onClick = onAddOption,
            )
        }
        Spacer(Modifier.height(12.dp))

        options.forEachIndexed { index, option ->
            EditableOptionCard(
                option = option,
                placeholder = strings.question.optionPlaceholder(index),
                onValueChange = { onOptionTextChange(option.id, it) },
                onSelect = { onOptionSelected(option.id) },
                onDelete = { onRemoveOption(option.id) },
                canDelete = options.size > 2,
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun EditableOptionCard(
    option: EditableOptionUiModel,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    canDelete: Boolean,
) {
    val strings = LocalStrings.current
    val containerColor = if (option.isCorrect) Teal700 else Color.White
    val borderColor = if (option.isCorrect) Teal700 else Neutral300
    val textColor = if (option.isCorrect) Color.White else Ink950
    val placeholderColor = if (option.isCorrect) Color.White.copy(alpha = 0.55f) else Neutral400
    val cursorColor = if (option.isCorrect) Color.White else BrandNavy
    val inputShape = RoundedCornerShape(AppRadius)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Card with overlay for the "Correct" badge
        Box(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(containerColor, inputShape)
                    .border(1.dp, borderColor, inputShape)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                // Circular selector
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(20.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, if (option.isCorrect) Teal700 else Neutral300, CircleShape)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSelect,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (option.isCorrect) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = strings.question.correctOptionLabel,
                            tint = Teal700,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                }

                // Text input
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = option.text,
                        onValueChange = onValueChange,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                        cursorBrush = SolidColor(cursorColor),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (option.text.isEmpty()) {
                                    Text(
                                        text = placeholder,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = placeholderColor,
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )
                }
            }

            // Chip de "Correcto" que desborda la esquina superior derecha de la tarjeta,
            // con rotación y color sólido naranja — espejo visual del chip de dificultad.
            if (option.isCorrect) {
                Text(
                    text = strings.question.correctOptionLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Teal900,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .rotate(8f)
                        .clip(RoundedCornerShape(20.dp))
                        .drawBehind { drawRect(Teal200) }
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                )
            }
        }

        // Delete button — solid red
        UIconActionButton(
            iconRes = UIcons.Actions.Delete,
            contentDescription = strings.common.delete,
            onClick = onDelete,
            enabled = canDelete,
            containerColor = Red700,
            contentColor = Color.White,
            elevated = true,
        )
    }
}

@UPreview
@Composable
private fun OptionsEditorSectionPreview() {
    UTheme {
        OptionsEditorSection(
            options = listOf(
                EditableOptionUiModel(id = "o1", text = "Núcleo"),
                EditableOptionUiModel(id = "o2", text = "Mitocondria", isCorrect = true),
                EditableOptionUiModel(id = "o3", text = "Ribosoma"),
                EditableOptionUiModel(id = "o4", text = ""),
            ),
            onOptionTextChange = { _, _ -> },
            onOptionSelected = {},
            onRemoveOption = {},
            onAddOption = {},
        )
    }
}
