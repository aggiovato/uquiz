package com.uquiz.android.ui.designsystem.components.feedback

import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.graphics.drawable.toDrawable
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius
import com.uquiz.android.ui.designsystem.preview.UPreview
import com.uquiz.android.ui.designsystem.tokens.UTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Tono visual disponible para los mensajes emergentes de la aplicación. */
enum class UToastTone {
    Info,
    Success,
    Error,
}

/** Representa una solicitud concreta de mensaje emergente en cola o visible. */
data class UToastRequest(
    val id: Long,
    val message: String,
    val tone: UToastTone,
    val durationMillis: Long,
)

/** Define el contrato básico para mostrar mensajes emergentes temporales. */
interface UToastController {
    fun show(
        message: String,
        tone: UToastTone = UToastTone.Info,
        durationMillis: Long? = null,
    )
}

private object NoOpToastController : UToastController {
    override fun show(
        message: String,
        tone: UToastTone,
        durationMillis: Long?,
    ) = Unit
}

val LocalToastController = staticCompositionLocalOf<UToastController> { NoOpToastController }

/** Estado y cola de presentación para los mensajes emergentes compartidos. */
@Stable
class UToastManager(
    private val scope: CoroutineScope,
) : UToastController {
    var currentRequest by mutableStateOf<UToastRequest?>(null)
        private set

    private val queue = ArrayDeque<UToastRequest>()
    private var dismissJob: Job? = null
    private var nextId = 0L

    override fun show(
        message: String,
        tone: UToastTone,
        durationMillis: Long?,
    ) {
        val request =
            UToastRequest(
                id = ++nextId,
                message = message,
                tone = tone,
                durationMillis = durationMillis ?: tone.defaultDurationMillis(),
            )
        if (currentRequest == null) {
            display(request)
        } else {
            queue.addLast(request)
        }
    }

    /** Oculta el toast actual y muestra el siguiente si existe. */
    fun dismissCurrent() {
        dismissJob?.cancel()
        dismissJob = null
        currentRequest = null
        val next = queue.removeFirstOrNull() ?: return
        display(next)
    }

    private fun display(request: UToastRequest) {
        dismissJob?.cancel()
        currentRequest = request
        dismissJob =
            scope.launch {
                delay(request.durationMillis)
                dismissCurrent()
            }
    }
}

/**
 * Recuerda el controlador de mensajes emergentes asociado al alcance recibido.
 *
 * @param scope Alcance usado para programar la cola y el cierre automático.
 */
@Composable
fun rememberUToastController(scope: CoroutineScope): UToastManager =
    remember(scope) {
        UToastManager(scope)
    }

/**
 * ### UToastHost
 *
 * Muestra el mensaje emergente activo gestionado por [manager].
 *
 * @param manager Gestor que mantiene la cola y el elemento visible.
 */
@Composable
fun UToastHost(
    manager: UToastManager,
    modifier: Modifier = Modifier,
) {
    val request = manager.currentRequest ?: return
    Dialog(
        onDismissRequest = { manager.dismissCurrent() },
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        DisposableEffect(dialogWindow) {
            dialogWindow?.apply {
                setBackgroundDrawable(
                    android.graphics.Color.TRANSPARENT
                        .toDrawable(),
                )
                setDimAmount(0f)
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                addFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                )
            }
            onDispose {
                dialogWindow?.clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
            ) {
                UToastCard(
                    request = request,
                    modifier = modifier,
                )
            }
        }
    }
}

/**
 * Renderiza la tarjeta visual del mensaje emergente actual.
 *
 * @param request Datos del mensaje que se va a mostrar.
 */
@Composable
private fun UToastCard(
    request: UToastRequest,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = request.tone.backgroundColor()
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape =
            androidx.compose.foundation.shape
                .RoundedCornerShape(URadius),
        shadowElevation = 18.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(request.tone.iconRes()),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = request.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 2,
            )
        }
    }
}

private fun UToastTone.defaultDurationMillis(): Long =
    when (this) {
        UToastTone.Info -> 2600L
        UToastTone.Success -> 2600L
        UToastTone.Error -> 3200L
    }

private fun UToastTone.backgroundColor(): Color =
    when (this) {
        UToastTone.Info -> BrandNavy
        UToastTone.Success -> Teal700
        UToastTone.Error -> Red700
    }

@DrawableRes
private fun UToastTone.iconRes(): Int =
    when (this) {
        UToastTone.Info -> UIcons.Feedback.Info
        UToastTone.Success -> UIcons.Feedback.Success
        UToastTone.Error -> UIcons.Feedback.Error
    }

@UPreview
@Composable
private fun UToastHostPreview() {
    val scope = rememberCoroutineScope()
    val manager = rememberUToastController(scope)
    UTheme {
        UToastHost(manager = manager)
    }
}
