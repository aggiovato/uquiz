package com.uquiz.android.domain.stats.projection

/**
 * ### PackStudyProgress
 *
 * Modelo de proyección mínimo que representa el progreso de estudio acumulado de
 * un pack.
 *
 * Se utiliza cuando solo interesa conocer cuántas preguntas han sido trabajadas
 * dentro del pack, sin cargar otras métricas complementarias.
 *
 * Propiedades:
 * - [packId]: identificador del pack analizado.
 * - [answeredCount]: número de preguntas respondidas dentro del pack.
 */
data class PackStudyProgress(
    val packId: String,
    val answeredCount: Int,
)
