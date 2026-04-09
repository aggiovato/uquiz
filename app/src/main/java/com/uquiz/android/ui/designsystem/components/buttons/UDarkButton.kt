package com.uquiz.android.ui.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme

/** Variante visual usada por [UDarkButton]. */
enum class UDarkButtonVariant { Primary, Secondary }

/**
 * ### UDarkButton
 *
 * Muestra un botón de ancho completo para superficies oscuras.
 *
 * @param text Texto visible del botón.
 * @param onClick Se invoca al pulsar el botón.
 * @param variant Jerarquía visual aplicada al botón.
 * @param enabled Indica si la acción está disponible.
 */
@Composable
fun UDarkButton(
    text: String,
    onClick: () -> Unit,
    variant: UDarkButtonVariant = UDarkButtonVariant.Primary,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val baseModifier =
        modifier
            .fillMaxWidth()
            .height(50.dp)

    when (variant) {
        UDarkButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = baseModifier,
                shape = RoundedCornerShape(URadius),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = BrandNavy,
                        disabledContainerColor = Color.White.copy(alpha = 0.45f),
                        disabledContentColor = BrandNavy.copy(alpha = 0.45f),
                    ),
                elevation =
                    ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp,
                        disabledElevation = 0.dp,
                    ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        UDarkButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = baseModifier,
                shape = RoundedCornerShape(URadius),
                border =
                    BorderStroke(
                        1.dp,
                        if (enabled) Color.White.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.14f),
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.08f),
                        contentColor = Color.White,
                        disabledContainerColor = Color.White.copy(alpha = 0.04f),
                        disabledContentColor = Color.White.copy(alpha = 0.45f),
                    ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@UPreview
@Composable
private fun UDarkButtonPreview() {
    UTheme {
        UDarkButton(text = "Acción", onClick = {})
    }
}
