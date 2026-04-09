package com.uquiz.android.domain.user.model

/**
 * ### UserProfile
 *
 * Modelo de dominio que representa el perfil principal del usuario dentro de la
 * aplicación.
 *
 * Este modelo concentra la identidad visible del usuario, incluyendo su nombre
 * de visualización y la configuración de avatar utilizada en la interfaz.
 *
 * Propiedades:
 * - [id]: identificador único del usuario.
 * - [displayName]: nombre visible mostrado en la interfaz.
 * - [avatarIcon]: clave del icono de avatar seleccionado, si existe.
 * - [avatarImageUri]: URI de la imagen personalizada de avatar, si existe.
 * - [createdAt]: marca temporal de creación del perfil.
 * - [updatedAt]: marca temporal de la última actualización del perfil.
 */
data class UserProfile(
    val id: String,
    val displayName: String,
    val avatarIcon: String? = null,
    val avatarImageUri: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
