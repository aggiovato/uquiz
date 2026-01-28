package com.uquiz.android.data.local.entity

/**
 * Audit timestamps for all entities.
 * Use with @Embedded to add createdAt and updatedAt columns.
 */
data class AuditTimestamps(
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
