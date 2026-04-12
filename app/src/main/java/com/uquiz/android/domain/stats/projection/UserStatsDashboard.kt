package com.uquiz.android.domain.stats.projection

import com.uquiz.android.domain.content.enums.DifficultyLevel

/**
 * ### UserStatsDashboard
 *
 * Proyección global de estadísticas del usuario, lista para alimentar el dashboard
 * principal de Stats sin exponer detalles de Room ni cálculos de agregación a la UI.
 */
data class UserStatsDashboard(
    val summary: UserStatsSummary = UserStatsSummary(),
    val modeStats: UserModeStats = UserModeStats(),
    val answerSplit: UserAnswerSplit = UserAnswerSplit(),
    val accuracyTrend: List<UserAccuracyTrendPoint> = emptyList(),
    val packRows: List<UserPackStatsRow> = emptyList(),
    val difficultyStats: List<UserDifficultyStats> = emptyList(),
    val fastestQuestion: UserQuestionInsight? = null,
    val mostFailedQuestion: UserQuestionInsight? = null,
)

/** Métricas universales mostradas en la cabecera del dashboard. */
data class UserStatsSummary(
    val totalSessions: Int = 0,
    val answeredQuestions: Int = 0,
    val accuracyPercent: Int? = null,
    val totalStudyTimeMs: Long = 0L,
    val averageAnswerTimeMs: Long? = null,
    val completedPacks: Int = 0,
    val inProgressPacks: Int = 0,
)

/** Métricas comparativas entre Study y Game. */
data class UserModeStats(
    val studyAccuracyPercent: Int? = null,
    val gameAccuracyPercent: Int? = null,
    val bestGameScore: Int? = null,
    val averageGameScore: Int? = null,
    val masteredQuestionPercent: Int? = null,
)

/** División acumulada entre respuestas correctas e incorrectas. */
data class UserAnswerSplit(
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
)

/** Punto de la línea de evolución de accuracy. */
data class UserAccuracyTrendPoint(
    val timestamp: Long,
    val accuracyPercent: Int,
)

/** Fila resumida de rendimiento por pack. */
data class UserPackStatsRow(
    val packId: String,
    val title: String,
    val sessions: Int,
    val accuracyPercent: Int? = null,
    val averageDurationMs: Long? = null,
    val progressPercent: Int = 0,
)

/** Métricas agregadas por dificultad. */
data class UserDifficultyStats(
    val difficulty: DifficultyLevel,
    val answeredCount: Int,
    val accuracyPercent: Int? = null,
    val averageTimeMs: Long? = null,
)

/** Insight destacado asociado a una pregunta concreta. */
data class UserQuestionInsight(
    val questionId: String,
    val questionText: String,
    val valueLabel: String,
)
