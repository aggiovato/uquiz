package com.uquiz.android.ui.designsystem.components.buttons

import androidx.compose.ui.graphics.Color
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Navy100
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Neutral700
import com.uquiz.android.ui.designsystem.tokens.Red100
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal100
import com.uquiz.android.ui.designsystem.tokens.Teal900

/** Tono semántico soportado por la familia de botones compartidos. */
enum class UButtonTone { Brand, Secondary, Danger, Neutral }

/** Tamaños disponibles para la familia de botones compartidos. */
enum class UButtonSize { Regular, Compact, Tiny }

/** Devuelve el color de fondo tonal para botones y controles afines. */
fun uTonalButtonContainerColor(tone: UButtonTone): Color = when (tone) {
    UButtonTone.Brand -> Navy100
    UButtonTone.Secondary -> Teal100
    UButtonTone.Danger -> Red100
    UButtonTone.Neutral -> Neutral100
}

/** Devuelve el color de contenido asociado al fondo tonal. */
fun uTonalButtonContentColor(tone: UButtonTone): Color = when (tone) {
    UButtonTone.Brand -> BrandNavy
    UButtonTone.Secondary -> Teal900
    UButtonTone.Danger -> Red700
    UButtonTone.Neutral -> Neutral700
}
