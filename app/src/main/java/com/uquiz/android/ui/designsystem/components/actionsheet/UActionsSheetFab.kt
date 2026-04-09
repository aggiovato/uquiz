package com.uquiz.android.ui.designsystem.components.actionsheet

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/** Define el tipo semántico de una acción del panel de acciones. */
enum class UFabActionKind { Normal, Destructive }

/**
 * Representa una acción disponible en [UActionsSheetFab].
 *
 * @param id Identificador estable de la acción.
 * @param label Texto principal mostrado en el panel.
 * @param description Texto secundario mostrado bajo el título.
 * @param iconRes Icono mostrado para la acción.
 * @param kind Tipo semántico de la acción.
 * @param enabled Indica si la acción está disponible.
 * @param containerColor Color de fondo opcional para el icono.
 * @param contentColor Color del icono.
 * @param onClick Se invoca al ejecutar la acción.
 */
data class UFabActionItem(
    val id: String,
    val label: String,
    val description: String,
    @param:DrawableRes val iconRes: Int,
    val kind: UFabActionKind = UFabActionKind.Normal,
    val enabled: Boolean = true,
    val containerColor: Color? = null,
    val contentColor: Color = Color.White,
    val onClick: () -> Unit,
)

/**
 * Describe la acción principal usada cuando el FAB no abre un panel.
 *
 * @param label Etiqueta accesible del FAB.
 * @param iconRes Icono mostrado en el FAB.
 * @param containerColor Color de fondo del FAB.
 * @param contentColor Color del icono.
 * @param onClick Se invoca al pulsar el FAB.
 */
data class UFabPrimaryAction(
    val label: String,
    @param:DrawableRes val iconRes: Int = UIcons.Actions.Settings,
    val containerColor: Color = Navy500,
    val contentColor: Color = Color.White,
    val onClick: () -> Unit,
)

/**
 * ### UActionsSheetFab
 *
 * Muestra un FAB que puede ejecutar una acción directa o abrir un panel de acciones.
 *
 * @param actions Acciones disponibles dentro del panel.
 * @param expanded Estado expandido opcional controlado por el padre.
 * @param onExpandedChange Se invoca cuando cambia el estado expandido.
 * @param primaryAction Acción directa ejecutada cuando no hay `actions`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UActionsSheetFab(
    actions: List<UFabActionItem>,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    primaryAction: UFabPrimaryAction? = null,
) {
    if (actions.isEmpty() && primaryAction == null) return

    val strings = LocalStrings.current
    var internalExpanded by rememberSaveable { mutableStateOf(false) }
    val isSheetOpen = expanded ?: internalExpanded
    val setExpanded: (Boolean) -> Unit = { value ->
        if (onExpandedChange != null) onExpandedChange(value) else internalExpanded = value
    }
    val isExpandable = actions.isNotEmpty()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val fabIconRotation by animateFloatAsState(
        targetValue = if (isExpandable && isSheetOpen) 180f else 0f,
        animationSpec = tween(durationMillis = 260),
        label = "fabIconRotation",
    )

    if (isExpandable && isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { setExpanded(false) },
            sheetState = sheetState,
            containerColor = Color.White,
            contentColor = BrandNavy,
            tonalElevation = 0.dp,
            shape =
                RoundedCornerShape(
                    topStart = URadius * 2,
                    topEnd = URadius * 2,
                ),
            scrimColor = BrandNavy.copy(alpha = 0.34f),
            dragHandle = {
                Box(
                    modifier =
                        Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .size(width = 42.dp, height = 4.dp)
                            .background(
                                color = Neutral500,
                                shape = RoundedCornerShape(percent = 100),
                            ),
                )
            },
        ) {
            UActionsSheetContent(
                actions = actions,
                onActionClick = { action ->
                    if (!action.enabled) return@UActionsSheetContent
                    setExpanded(false)
                    action.onClick()
                },
            )
        }
    }

    Box(modifier = modifier) {
        FloatingActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            onClick = {
                if (isExpandable) setExpanded(!isSheetOpen) else primaryAction?.onClick()
            },
            containerColor = primaryAction?.containerColor ?: Navy500,
            contentColor = primaryAction?.contentColor ?: Color.White,
            elevation =
                FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 10.dp,
                    hoveredElevation = 10.dp,
                ),
        ) {
            Icon(
                painter =
                    painterResource(
                        if (isExpandable) {
                            UIcons.Actions.ArrowsUp
                        } else {
                            primaryAction?.iconRes ?: UIcons.Actions.Settings
                        },
                    ),
                contentDescription = if (isExpandable) strings.actionsLabel else primaryAction?.label,
                modifier = Modifier.rotate(fabIconRotation),
            )
        }
    }
}

@UPreview
@Composable
private fun UActionsSheetFabPreview() {
    UTheme {
        UActionsSheetFab(
            actions = listOf(
                UFabActionItem(
                    id = "editar",
                    label = "Editar",
                    description = "Modifica este elemento",
                    iconRes = UIcons.Actions.Edit,
                    onClick = {},
                ),
            ),
        )
    }
}
