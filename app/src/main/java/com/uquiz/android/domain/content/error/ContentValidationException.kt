package com.uquiz.android.domain.content.error

/** Maximum character length for a pack or folder name/title. */
const val CONTENT_NAME_MAX_LENGTH = 100

/** Maximum character length for a pack description. */
const val PACK_DESCRIPTION_MAX_LENGTH = 200

/**
 * Base exception for domain validation errors related to user-managed content.
 */
sealed class ContentValidationException(message: String) : IllegalArgumentException(message)

/**
 * Raised when a folder would duplicate a sibling folder name within the same parent.
 */
class DuplicateFolderNameException(
    val folderName: String,
) : ContentValidationException("A sibling folder named \"$folderName\" already exists.")

/**
 * Raised when a pack would duplicate another pack title inside the same folder.
 */
class DuplicatePackTitleException(
    val packTitle: String,
) : ContentValidationException("A pack titled \"$packTitle\" already exists in this folder.")

/**
 * Raised when a folder name exceeds [CONTENT_NAME_MAX_LENGTH] characters.
 */
class FolderNameTooLongException :
    ContentValidationException("Folder name cannot exceed $CONTENT_NAME_MAX_LENGTH characters.")

/**
 * Raised when a pack title exceeds [CONTENT_NAME_MAX_LENGTH] characters.
 */
class PackTitleTooLongException :
    ContentValidationException("Pack title cannot exceed $CONTENT_NAME_MAX_LENGTH characters.")

/**
 * Raised when a pack description exceeds [PACK_DESCRIPTION_MAX_LENGTH] characters.
 */
class PackDescriptionTooLongException :
    ContentValidationException("Pack description cannot exceed $PACK_DESCRIPTION_MAX_LENGTH characters.")

class InvalidContentColorException(
    val colorHex: String?
) : ContentValidationException("The provided content color is not allowed: ${colorHex ?: "null"}")

class InvalidContentIconException(
    val iconKey: String?
) : ContentValidationException("The provided content icon is not allowed: ${iconKey ?: "null"}")
