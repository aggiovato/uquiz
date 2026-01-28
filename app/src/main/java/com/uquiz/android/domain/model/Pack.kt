package com.uquiz.android.domain.model

/**
 * Domain model for Pack
 *
 * Represents a collection of questions
 */
data class Pack(
    val id: String,
    val title: String,
    val description: String? = null,
    val folderId: String,
    val icon: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
