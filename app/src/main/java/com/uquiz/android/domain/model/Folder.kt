package com.uquiz.android.domain.model

/**
 * Domain model for Folder
 *
 * Represents a folder that can contain packs and other folders (hierarchical)
 */
data class Folder(
    val id: String,
    val name: String,
    val parentId: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
