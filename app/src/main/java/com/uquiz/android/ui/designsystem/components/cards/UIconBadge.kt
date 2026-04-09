package com.uquiz.android.ui.designsystem.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import com.uquiz.android.ui.designsystem.tokens.UIcons

/**
 * ### UIconBadge
 *
 * Muestra una insignia cuadrada con un único icono.
 *
 * @param iconRes Recurso drawable del icono.
 * @param backgroundColor Color de fondo de la insignia.
 * @param contentColor Color aplicado al icono.
 */
@Composable
fun UIconBadge(
    @DrawableRes iconRes: Int,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp),
        )
    }
}

@UPreview
@Composable
private fun UIconBadgePreview() {
    UTheme {
        UIconBadge(
            iconRes = UIcons.Select.Book,
            backgroundColor = Color(0xFF0B1929),
            contentColor = Color.White,
        )
    }
}
