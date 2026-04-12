package com.uquiz.android.ui.feature.stats.utils

import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.stats.projection.PackBestPerformance
import com.uquiz.android.domain.stats.projection.PackRecentActivity
import com.uquiz.android.ui.i18n.AppStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

/**
 * Modelo que representa el cambio de una métrica respecto al período anterior.
 *
 * @property label  Texto formateado del delta (p.ej. "+3 esta semana", "-5pp").
 * @property improved `true` si el cambio es favorable para el usuario.
 */
internal data class StatDelta(
    val label: String,
    val improved: Boolean,
)

/** Formatea un timestamp de epoch-ms como fecha/hora legible ("Apr 9, 14:30"). */
internal fun Long.formatAsSessionTime(): String =
    SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(this))

/** Convierte una duración en milisegundos a una cadena legible ("2m 15s" o "45s"). */
internal fun Long.toReadableDuration(): String {
    val totalSeconds = (this / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"
}

/** Devuelve la etiqueta localizada del modo de intento, o el literal de "sin sesiones" si es null. */
internal fun AttemptMode?.asModeLabel(strings: AppStrings): String = when (this) {
    AttemptMode.STUDY -> strings.common.studyMode
    AttemptMode.GAME -> strings.common.playMode
    null -> strings.statsPack.packNoSessionsYet
}

/**
 * Formatea el mejor rendimiento de un pack como cadena legible.
 *
 * Para el modo Game devuelve el score numérico; para Study, el porcentaje de precisión.
 */
internal fun PackBestPerformance.toDisplayValue(strings: AppStrings): String = when (mode) {
    AttemptMode.GAME -> numericScore?.let { "$it ${strings.common.pointsShortLabel}" }
        ?: "$scoreLabel ${strings.common.pointsShortLabel}"
    AttemptMode.STUDY -> numericScore?.let { "$it%" } ?: scoreLabel
}

/**
 * Calcula el delta de sesiones entre la última semana y la anterior.
 *
 * Devuelve `null` si no hay actividad suficiente para comparar.
 */
internal fun recentSessionsDelta(
    activity: List<PackRecentActivity>,
    strings: AppStrings,
): StatDelta? {
    if (activity.isEmpty()) return null
    val now = System.currentTimeMillis()
    val weekMs = 7L * 24L * 60L * 60L * 1000L
    val last7 = activity.count { now - it.completedAt <= weekMs }
    val previous7 = activity.count {
        val diff = now - it.completedAt
        diff > weekMs && diff <= 2 * weekMs
    }
    val delta = last7 - previous7
    return when {
        delta > 0 -> StatDelta("+${strings.statsPack.packThisWeekLabel(delta)}", improved = true)
        delta < 0 -> StatDelta("-${strings.statsPack.packThisWeekLabel(abs(delta))}", improved = false)
        else -> StatDelta(strings.statsPack.packThisWeekLabel(last7), improved = true)
    }
}

/**
 * Calcula el delta de precisión comparando las últimas 3 sesiones con las 3 anteriores.
 *
 * Devuelve `null` si no hay suficiente historial en ambos períodos.
 */
internal fun accuracyDelta(activity: List<PackRecentActivity>): StatDelta? {
    val latest = activity.take(3).mapNotNull { it.accuracyPercent }
    val previous = activity.drop(3).take(3).mapNotNull { it.accuracyPercent }
    if (latest.isEmpty() || previous.isEmpty()) return null
    val delta = latest.average() - previous.average()
    return StatDelta(
        label = if (delta >= 0) "+${delta.toInt()}pp" else "${delta.toInt()}pp",
        improved = delta >= 0,
    )
}

/**
 * Calcula el delta de duración media comparando las últimas 3 sesiones con las 3 anteriores.
 *
 * Devuelve `null` si no hay suficiente historial en ambos períodos.
 * Una duración más corta se considera una mejora.
 */
internal fun durationDelta(activity: List<PackRecentActivity>): StatDelta? {
    val latest = activity.take(3).mapNotNull { it.durationMs }
    val previous = activity.drop(3).take(3).mapNotNull { it.durationMs }
    if (latest.isEmpty() || previous.isEmpty()) return null
    val deltaSeconds = ((latest.average() - previous.average()) / 1000.0).toInt()
    return StatDelta(
        label = if (deltaSeconds >= 0) "+${deltaSeconds}s" else "${deltaSeconds}s",
        // Una duración menor es favorable (el usuario es más rápido)
        improved = deltaSeconds <= 0,
    )
}
