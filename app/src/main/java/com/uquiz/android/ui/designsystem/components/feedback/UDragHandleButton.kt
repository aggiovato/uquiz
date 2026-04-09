package com.uquiz.android.ui.designsystem.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UDragHandleButton
 *
 * Muestra un asa visual para elementos reordenables.
 *
 * @param tint Color aplicado al icono del asa.
 */
@Composable
fun UDragHandleButton(
    modifier: Modifier = Modifier,
    tint: Color = Neutral400,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(Neutral100, RoundedCornerShape(URadius)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.DragIndicator,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp),
        )
    }
}

@UPreview
@Composable
private fun UDragHandleButtonPreview() {
    UTheme {
        UDragHandleButton()
    }
}
