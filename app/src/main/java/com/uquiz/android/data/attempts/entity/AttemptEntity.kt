package com.uquiz.android.data.attempts.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uquiz.android.data.local.db.model.AuditTimestamps
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.enums.AttemptStatus

/**
 * Fila de la tabla `attempts`. Representa una sesión de intento del usuario (estudio o partida).
 * El campo [primaryPackId] puede ser nulo cuando el intento agrupa varios packs simultáneamente.
 *
 * @see com.uquiz.android.domain.attempts.model.Attempt
 */
@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val mode: AttemptMode,
    val status: AttemptStatus = AttemptStatus.IN_PROGRESS,
    val startedAt: Long,
    val completedAt: Long? = null,
    val durationMs: Long? = null,
    val score: Int = 0,
    val primaryPackId: String? = null,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val currentQuestionIndex: Int = 0,
    @Embedded
    val audit: AuditTimestamps = AuditTimestamps(),
)
