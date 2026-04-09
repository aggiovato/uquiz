package com.uquiz.android.ui.designsystem.components.dialogs

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.tokens.UTheme

/**
 * ### UDialogScaffold
 *
 * Proporciona la base visual compartida para los diálogos de la app.
 *
 * @param title Título mostrado en la cabecera.
 * @param onDismiss Se invoca cuando el diálogo solicita cerrarse.
 * @param headerColor Color de fondo de la cabecera.
 * @param headerContentColor Color del contenido de la cabecera.
 * @param headerIconRes Recurso drawable opcional del icono de cabecera.
 * @param headerIconColor Color del icon, por defecto es el mismo de la cabecera.
 * @param showDecorativeBackground Indica si debe mostrarse el fondo decorativo.
 * @param decorativeTint Color usado para teñir el fondo decorativo.
 * @param actions Contenido de la fila de acciones.
 * @param content Contenido principal del cuerpo del diálogo.
 */
@Composable
fun UDialogScaffold(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    headerColor: Color,
    headerContentColor: Color = Color.White,
    @DrawableRes headerIconRes: Int? = null,
    headerIconColor: Color = headerContentColor,
    showDecorativeBackground: Boolean = true,
    decorativeTint: Color = headerColor,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(URadius),
            color = Color.White,
            tonalElevation = 0.dp,
        ) {
            Column {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                color = headerColor,
                                shape = RoundedCornerShape(topStart = URadius, topEnd = URadius),
                            ).padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    Row {
                        if (headerIconRes != null) {
                            Icon(
                                painter = painterResource(headerIconRes),
                                contentDescription = null,
                                tint = headerIconColor,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(Modifier.size(10.dp))
                        }

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = headerContentColor,
                        )
                    }
                }

                Box {
                    if (showDecorativeBackground) {
                        val decorColor = lerp(decorativeTint, Color.White, 0.70f).copy(alpha = 0.20f)
                        Image(
                            painter = painterResource(UIcons.Decorative.Dialog),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(decorColor),
                            modifier =
                                Modifier
                                    .align(Alignment.BottomStart)
                                    .offset(x = (-218).dp, y = 285.dp)
                                    .rotate(30f)
                                    .scale(2.3f),
                        )
                    }

                    Column(modifier = Modifier.padding(20.dp)) {
                        content()
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 18.dp),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            actions()
                        }
                    }
                }
            }
        }
    }
}

@UPreview
@Composable
private fun UDialogScaffoldPreview() {
    UTheme {
        UDialogScaffold(
            title = "Este es el preview",
            onDismiss = {},
            headerColor = BrandNavy,
            headerContentColor = Color.White,
            headerIconRes = R.drawable.uic_action_details,
            decorativeTint = BrandNavy,
            actions = {
                TextButton(onClick = {}) {
                    Text("Cancel")
                }
            },
        ) {
            Text(
                text =
                    "Este es un ejemplo de contenido dentro del diálogo. " +
                        "Aquí puedes colocar formularios, mensajes o cualquier composable.",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandNavy,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
