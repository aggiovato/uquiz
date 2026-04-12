package com.uquiz.android.ui.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uquiz.android.ui.designsystem.animations.screens.UEmptyMascot
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UEmptyContent
 *
 * Composable reutilizable para estados vacíos: muestra la mascota animada [UEmptyMascot]
 * seguida de un mensaje descriptivo centrado. Se usa en pantallas sin contenido todavía
 * (Library sin packs, Folder vacía, Pack sin preguntas, etc.).
 *
 * @param message Texto descriptivo del estado vacío, localizado por el llamador.
 */
@Composable
fun UEmptyContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UEmptyMascot(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Neutral500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
    }
}

@UPreview
@Composable
private fun UEmptyContentPreview() {
    UTheme {
        UEmptyContent(
            message = "No hay nada aquí todavía.\nCrea tu primer pack para empezar.",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 32.dp),
        )
    }
}
