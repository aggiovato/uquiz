package com.uquiz.android.ui.designsystem.tokens

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.uquiz.android.domain.content.enums.AllowedUColor
import com.uquiz.android.domain.content.enums.AllowedUIcon
import androidx.core.graphics.toColorInt

/**
 * Color option available in the content appearance pickers.
 *
 * @param hex Hex string persisted in the database (e.g. `"#134C8F"`).
 * @param color Resolved [Color] value used directly in Compose.
 */
data class ContentColorOption(
    val hex: String,
    val color: Color,
)

/**
 * Icon option available in the content appearance pickers.
 *
 * @param key String key persisted in the database (e.g. `"folder"`, `"brain"`).
 * @param iconRes Drawable resource resolved at runtime.
 */
data class ContentIconOption(
    val key: String,
    @param:DrawableRes val iconRes: Int,
)

/** Full color palette offered to the user when creating or editing folders and packs. */
val contentColorPalette: List<ContentColorOption> =
    AllowedUColor.entries.map { color -> ContentColorOption(color.hex, color.toComposeColor()) }

/** @suppress Deprecated alias — prefer [contentColorPalette] directly. */
val folderColorPalette: List<Pair<String, Color>> =
    contentColorPalette.map { it.hex to it.color }

/** Full icon set available to all content types before per-type filtering. */
private val baseSelectableIconPalette: List<ContentIconOption> =
    AllowedUIcon.entries.map { icon -> ContentIconOption(icon.key, icon.toDrawableRes()) }

/**
 * Icon palette offered to the user when creating or editing a **folder**.
 * "folder" appears first; "file" is excluded because it is reserved for packs.
 */
val folderSelectableIconPalette: List<ContentIconOption> = buildList {
    add(baseSelectableIconPalette.first { it.key == "folder" })
    addAll(baseSelectableIconPalette.filterNot { it.key in setOf("folder", "file") })
}

/**
 * Icon palette offered to the user when creating or editing a **pack**.
 * "file" appears first; "folder" is excluded because it is reserved for folders.
 */
val packSelectableIconPalette: List<ContentIconOption> = buildList {
    add(baseSelectableIconPalette.first { it.key == "file" })
    addAll(baseSelectableIconPalette.filterNot { it.key in setOf("folder", "file") })
}

/** @suppress Deprecated alias — prefer [baseSelectableIconPalette] indirectly via the typed palettes above. */
val contentIconPalette: List<ContentIconOption> = baseSelectableIconPalette

/**
 * Resolves a persisted icon [key] to a drawable resource.
 *
 * Entries at the bottom of the `when` block map legacy Material-icon string values
 * that may exist in older databases to their current equivalents.
 *
 * @param key String key stored in the database. `null` or unknown values fall back
 *   to [fallback].
 * @param fallback Drawable used when [key] is `null` or not found in the palette.
 * @return A drawable resource ID ready for use with `painterResource`.
 */
@DrawableRes
fun contentIconResForKey(key: String?, @DrawableRes fallback: Int = UIcons.Select.Folder): Int =
    AllowedUIcon.fromKeyOrNull(key)?.toDrawableRes() ?: fallback

/**
 * Converts a hex string to a (backgroundTint, iconColor) pair used by icon badges.
 *
 * The background is the parsed color at 15 % opacity; the icon uses the full
 * solid color. Returns [fallback] when [hex] is null, blank, or unparseable.
 *
 * @param hex Hex color string (e.g. `"#134C8F"`). May be null.
 * @param fallback Pair used when [hex] cannot be resolved.
 * @return (backgroundTint, solidColor) ready for [com.uquiz.android.ui.designsystem.components.cards.UIconBadge].
 */
fun contentColorFromHex(hex: String?, fallback: Pair<Color, Color>): Pair<Color, Color> {
    if (hex.isNullOrBlank()) return fallback
    return try {
        val solid = Color(hex.toColorInt())
        solid.copy(alpha = 0.15f) to solid
    } catch (_: IllegalArgumentException) {
        fallback
    }
}

/**
 * Returns the light tinted surface color used for the selected state of a
 * color or icon picker cell.
 *
 * Maps each palette [Color] to its corresponding 100-level token. Falls back to
 * the color at 12 % opacity for custom or unknown values.
 *
 * @param color Solid palette color (e.g. [Navy500]).
 * @return Tinted surface color suitable as a [androidx.compose.foundation.layout.Box] background.
 */
fun contentSelectionSurface(color: Color): Color = when (color) {
    Navy500 -> Navy100
    Teal500 -> Teal100
    Gold500 -> Gold100
    Red500 -> Red100
    Neutral500 -> Neutral100
    else -> color.copy(alpha = 0.12f)
}

/**
 * Returns the border color used for the selected state of a color or icon picker cell.
 *
 * Maps each palette [Color] to its 300-level token to produce a medium-emphasis
 * outline that pairs with [contentSelectionSurface]. Falls back to the color at
 * 55 % opacity for custom or unknown values.
 *
 * @param color Solid palette color (e.g. [Navy500]).
 * @return Border color suitable as a [BorderStroke] or `border` modifier color.
 */
fun contentSelectionBorder(color: Color): Color = when (color) {
    Navy500 -> Navy300
    Teal500 -> Teal300
    Gold500 -> Gold300
    Red500 -> Red300
    Neutral500 -> Neutral300
    else -> color.copy(alpha = 0.55f)
}

private fun AllowedUColor.toComposeColor(): Color = when (this) {
    AllowedUColor.NAVY -> Navy500
    AllowedUColor.TEAL -> Teal500
    AllowedUColor.GOLD -> Gold500
    AllowedUColor.RED -> Red500
    AllowedUColor.NEUTRAL -> Neutral500
}

@DrawableRes
private fun AllowedUIcon.toDrawableRes(): Int = when (this) {
    AllowedUIcon.FOLDER -> UIcons.Select.Folder
    AllowedUIcon.FILE -> UIcons.Select.File
    AllowedUIcon.HISTORY -> UIcons.Select.History
    AllowedUIcon.GLOBE -> UIcons.Select.Globe
    AllowedUIcon.BRAIN -> UIcons.Select.Brain
    AllowedUIcon.IDEA -> UIcons.Select.Idea
    AllowedUIcon.MATH -> UIcons.Select.Math
    AllowedUIcon.MOLECULE -> UIcons.Select.Molecule
    AllowedUIcon.BACTERIA -> UIcons.Select.Bacteria
    AllowedUIcon.QUERY -> UIcons.Select.Query
    AllowedUIcon.SHELF -> UIcons.Select.Shelf
    AllowedUIcon.BOOK -> UIcons.Select.Book
    AllowedUIcon.LANG -> UIcons.Select.Lang
    AllowedUIcon.CALCULATE -> UIcons.Select.Calculate
    AllowedUIcon.CODE -> UIcons.Select.Code
    AllowedUIcon.SCIENCE -> UIcons.Select.Science
    AllowedUIcon.SCHOOL -> UIcons.Select.School
}
