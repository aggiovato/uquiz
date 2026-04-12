package com.uquiz.android.data.local.db.model

/**
 * ### AuditTimestamps
 *
 * Marcas de tiempo de auditoría compartidas por todas las entidades.
 * Se incluye via `@Embedded` para añadir las columnas `createdAt` y `updatedAt` a cada tabla.
 */
data class AuditTimestamps(
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
