package com.uquiz.android.ui.designsystem.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral300
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.ContentIconOption
import com.uquiz.android.ui.designsystem.tokens.contentSelectionBorder
import com.uquiz.android.ui.designsystem.tokens.contentSelectionSurface
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.folderSelectableIconPalette
import kotlinx.coroutines.launch

/**
 * ### UContentIconCarouselPicker
 *
 * Muestra un selector paginado de iconos de contenido.
 *
 * @param options Iconos disponibles para seleccionar.
 * @param selectedKey Clave del icono actualmente seleccionado.
 * @param tintColor Color de acento aplicado a la selección activa.
 * @param onSelect Se invoca al seleccionar un icono.
 */
@Composable
fun UContentIconCarouselPicker(
    options: List<ContentIconOption>,
    selectedKey: String,
    tintColor: Color,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pages = remember(options) { options.chunked(8) }
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                page.chunked(4).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        rowItems.forEach { option ->
                            val isSelected = option.key == selectedKey
                            val shape = RoundedCornerShape(URadius)
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(shape)
                                    .background(
                                        color = if (isSelected) contentSelectionSurface(tintColor) else Color.White,
                                        shape = shape,
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) contentSelectionBorder(tintColor) else Neutral100,
                                        shape = shape,
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) { onSelect(option.key) },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(option.iconRes),
                                    contentDescription = null,
                                    tint = if (isSelected) tintColor else Neutral300,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .background(
                            color = if (isSelected) tintColor else Neutral100,
                            shape = CircleShape,
                        )
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                )
            }
        }
    }
}

@UPreview
@Composable
private fun UContentIconCarouselPickerPreview() {
    UTheme {
        UContentIconCarouselPicker(
            options = folderSelectableIconPalette,
            selectedKey = folderSelectableIconPalette.first().key,
            tintColor = BrandNavy,
            onSelect = {},
        )
    }
}
