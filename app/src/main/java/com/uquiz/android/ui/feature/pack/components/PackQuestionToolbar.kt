package com.uquiz.android.ui.feature.pack.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.components.chips.UToggleChip
import com.uquiz.android.ui.designsystem.components.inputs.USearchField
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.i18n.LocalStrings

/**
 * ### PackQuestionToolbar
 *
 * Barra de herramientas del listado de preguntas de un pack.
 *
 * @param questionCount Número de preguntas visibles en el listado.
 * @param reorderMode Indica si el modo reorder está activo.
 * @param filterMode Indica si el modo filtro está activo.
 * @param filterQuery Texto actual del buscador de preguntas.
 * @param onReorderToggle Acción para alternar el modo reorder.
 * @param onFilterToggle Acción para alternar el modo filtro.
 * @param onFilterQueryChange Acción al cambiar el texto del buscador.
 */
@Composable
fun PackQuestionToolbar(
    questionCount: Int,
    reorderMode: Boolean,
    filterMode: Boolean,
    filterQuery: String,
    onReorderToggle: () -> Unit,
    onFilterToggle: () -> Unit,
    onFilterQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SectionHeader(strings.pack.questionsSection(questionCount))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UToggleChip(
                    iconRes = UIcons.Actions.Reorder,
                    label = strings.common.reorderLabel,
                    isActive = reorderMode,
                    onClick = onReorderToggle,
                )
                UToggleChip(
                    iconRes = UIcons.Actions.Filter,
                    label = strings.common.filterLabel,
                    isActive = filterMode,
                    onClick = onFilterToggle,
                )
            }
        }

        AnimatedVisibility(
            visible = filterMode,
            enter = expandVertically(tween(220)) + fadeIn(tween(180)),
            exit = shrinkVertically(tween(200)) + fadeOut(tween(150)),
        ) {
            USearchField(
                value = filterQuery,
                onValueChange = onFilterQueryChange,
                placeholder = strings.pack.filterQuestionsHint,
                leadingIcon = painterResource(UIcons.Actions.Filter),
                modifier = Modifier.padding(top = 0.dp),
            )
        }
    }
}

@UPreview
@Composable
private fun PackQuestionToolbarPreview() {
    UTheme {
        PackQuestionToolbar(
            questionCount = 18,
            reorderMode = false,
            filterMode = true,
            filterQuery = "capital",
            onReorderToggle = {},
            onFilterToggle = {},
            onFilterQueryChange = {},
        )
    }
}
