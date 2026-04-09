package com.uquiz.android.ui.designsystem.preview

import androidx.compose.ui.tooling.preview.Preview

/**
 * ### UPreview
 *
 * Anotación de preview estándar de la app.
 *
 * Equivale a `@Preview(showBackground = true, backgroundColor = 0xFF0B1929)`.
 * Usar esta anotación en lugar de `@Preview` directo garantiza coherencia visual
 * entre los previews de todas las pantallas y componentes.
 */
@Preview(showBackground = true, backgroundColor = 0xFF0B1929)
@Retention(AnnotationRetention.BINARY)
annotation class UPreview
