package com.uquiz.android.domain.content.projection

import com.uquiz.android.domain.content.model.Pack

/**
 * ### PackWithQuestionCount
 *
 * Modelo de proyección utilizado por la capa de presentación para representar
 * un pack junto con la cantidad de preguntas asociadas.
 *
 * Este modelo resulta útil en pantallas de listado, detalle o selección, donde
 * además de mostrar la información base del pack se necesita indicar cuántas
 * preguntas contiene.
 *
 * Propiedades:
 * - [pack]: modelo de dominio del pack.
 * - [questionCount]: cantidad de preguntas asociadas al pack.
 */
data class PackWithQuestionCount(
    val pack: Pack,
    val questionCount: Int,
)
