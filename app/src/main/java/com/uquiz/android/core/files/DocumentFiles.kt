package com.uquiz.android.core.files

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.uquiz.android.domain.importexport.error.InvalidImportExtensionException
import java.io.IOException

fun Context.readDocumentText(uri: Uri): String =
    contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
        ?: throw IOException("Unable to read document.")

fun Context.writeDocumentText(uri: Uri, content: String) {
    contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
        writer.write(content)
    } ?: throw IOException("Unable to write document.")
}

fun Context.documentDisplayName(uri: Uri): String? =
    contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        }

fun requireSupportedImportExtension(fileName: String?) {
    val normalized = fileName?.lowercase()
    if (normalized == null || (!normalized.endsWith(".uquiz") && !normalized.endsWith(".uqz"))) {
        throw InvalidImportExtensionException()
    }
}
