package com.uquiz.android.domain.ranking.model

import com.uquiz.android.domain.ranking.enums.UserRank

/**
 * ### UserRankState
 *
 * Modelo de dominio que representa el estado agregado del usuario dentro del
 * sistema de ranking.
 *
 * Esta estructura concentra el rango actual, el rating interno y métricas
 * históricas necesarias para recalcular la progresión del usuario y mostrar
 * su evolución en la interfaz.
 *
 * Propiedades:
 * - [userId]: identificador del usuario al que pertenece el estado.
 * - [currentRank]: rango actual calculado del usuario.
 * - [mmr]: rating principal usado para determinar el rango.
 * - [perfEwma]: media móvil exponencial del rendimiento reciente.
 * - [lifetimeCorrect]: total histórico de respuestas correctas.
 * - [lifetimeIncorrect]: total histórico de respuestas incorrectas.
 * - [lifetimeTimeout]: total histórico de respuestas agotadas por tiempo.
 * - [totalGameAnswers]: total histórico de respuestas en modo juego.
 * - [totalStudyAnswers]: total histórico de respuestas en modo estudio.
 * - [lastRankChangeAt]: marca temporal del último cambio de rango.
 * - [answersSinceRankChange]: número de respuestas desde el último cambio de rango.
 * - [totalXp]: experiencia total acumulada.
 * - [createdAt]: marca temporal de creación del estado.
 * - [updatedAt]: marca temporal de la última actualización del estado.
 */
data class UserRankState(
    val userId: String,
    val currentRank: UserRank = UserRank.INITIATE,
    val mmr: Float = 1200f,
    val perfEwma: Float = 0.5f,
    val lifetimeCorrect: Int = 0,
    val lifetimeIncorrect: Int = 0,
    val lifetimeTimeout: Int = 0,
    val totalGameAnswers: Int = 0,
    val totalStudyAnswers: Int = 0,
    val lastRankChangeAt: Long? = null,
    val answersSinceRankChange: Int = 0,
    val totalXp: Long = 0L,
    val createdAt: Long,
    val updatedAt: Long,
)
