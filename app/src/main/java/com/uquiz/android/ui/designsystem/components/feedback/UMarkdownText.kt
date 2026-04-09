package com.uquiz.android.ui.designsystem.components.feedback

import android.graphics.Typeface
import android.text.TextUtils
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.uquiz.android.R
import com.uquiz.android.ui.designsystem.tokens.Ink950
import androidx.compose.material3.MaterialTheme
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme

// Color del fondo de los bloques de código: navy oscuro que contrasta bien sobre fondo blanco.
private val codeBlockBg = Color(0xFF1B2130)

// Color del texto dentro de los bloques de código: gris claro legible sobre fondo oscuro.
private val codeBlockText = Color(0xFFCDD5E0)

// Color del fondo del código inline: gray claro neutro.
private val inlineCodeBg = Color(0xFFEDEEF2)

/**
 * ### UMarkdownText
 *
 * Renderiza texto Markdown dentro de Compose usando la librería Markwon.
 *
 * Soporta negrita, cursiva, listas, bloques de código, saltos de línea y enlaces.
 * Los estilos de código y viñetas están optimizados para lectura dentro de las cards de estudio.
 *
 * @param markdown Cadena Markdown de origen.
 * @param style Estilo tipográfico base usado para renderizar el contenido.
 * @param color Color principal del texto.
 * @param textAlign Alineación opcional del texto.
 * @param maxLines Número máximo de líneas visibles.
 * @param overflow Estrategia aplicada cuando el contenido desborda.
 */
@Composable
fun UMarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    color: Color = Ink950,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val textColor = color.toArgb()
    val textSizeSp =
        remember(style.fontSize) {
            style.fontSize.value.takeIf { !it.isNaN() } ?: 14f
        }
    val fontRes =
        remember(style.fontWeight) {
            when (style.fontWeight) {
                FontWeight.ExtraBold -> R.font.plus_jakarta_sans_extrabold
                FontWeight.Bold -> R.font.plus_jakarta_sans_bold
                FontWeight.SemiBold -> R.font.plus_jakarta_sans_semibold
                FontWeight.Medium -> R.font.plus_jakarta_sans_medium
                else -> R.font.plus_jakarta_sans_regular
            }
        }

    val markwon =
        remember(density) {
            val bulletPx = with(density) { 4.dp.toPx() }.toInt()
            Markwon
                .builder(context)
                .usePlugin(
                    object : AbstractMarkwonPlugin() {
                        override fun configureTheme(builder: MarkwonTheme.Builder) {
                            builder
                                // Viñetas pequeñas y compactas.
                                .bulletWidth(bulletPx)
                                // Código inline: fondo gris claro.
                                .codeBackgroundColor(inlineCodeBg.toArgb())
                                .codeTextColor(Ink950.toArgb())
                                // Bloque de código: fondo oscuro tipo terminal.
                                .codeBlockBackgroundColor(codeBlockBg.toArgb())
                                .codeBlockTextColor(codeBlockText.toArgb())
                                .codeTypeface(Typeface.MONOSPACE)
                        }
                    },
                ).build()
        }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextView(ctx).apply {
                setTextColor(textColor)
                textSize = textSizeSp
                typeface = ResourcesCompat.getFont(ctx, fontRes) ?: Typeface.DEFAULT
                // Desactiva la captura de toques para que los eventos lleguen al Composable padre.
                isClickable = false
                isFocusable = false
                isLongClickable = false
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
            textView.setTextColor(textColor)
            textView.textSize = textSizeSp
            textView.typeface = ResourcesCompat.getFont(textView.context, fontRes) ?: Typeface.DEFAULT
            textView.textAlignment =
                when (textAlign) {
                    TextAlign.Center -> TextView.TEXT_ALIGNMENT_CENTER
                    TextAlign.End, TextAlign.Right -> TextView.TEXT_ALIGNMENT_VIEW_END
                    else -> TextView.TEXT_ALIGNMENT_VIEW_START
                }
            textView.maxLines = maxLines
            textView.ellipsize =
                when (overflow) {
                    TextOverflow.Ellipsis -> TextUtils.TruncateAt.END
                    else -> null
                }
        },
    )
}

@UPreview
@Composable
private fun UMarkdownTextPreview() {
    UTheme {
        UMarkdownText(
            markdown = "Texto en **negrita** e *italica* con `código`",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
