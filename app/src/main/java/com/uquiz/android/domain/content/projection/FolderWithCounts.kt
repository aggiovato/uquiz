package com.uquiz.android.domain.content.projection

import com.uquiz.android.domain.content.model.Folder

/**
 * ### FolderWithCounts
 *
 * Modelo de proyección utilizado por la capa de presentación para representar
 * una carpeta junto con información agregada sobre su contenido inmediato.
 *
 * Este modelo resulta útil en vistas de listado o navegación, donde además de
 * mostrar los datos base de la carpeta se necesita indicar cuántas subcarpetas
 * y cuántos packs contiene.
 *
 * Propiedades:
 * - [folder]: modelo de dominio de la carpeta.
 * - [subfolderCount]: cantidad de subcarpetas directas contenidas dentro de la carpeta.
 * - [packCount]: cantidad de packs directos contenidos dentro de la carpeta.
 */
data class FolderWithCounts(
    val folder: Folder,
    val subfolderCount: Int,
    val packCount: Int,
)
