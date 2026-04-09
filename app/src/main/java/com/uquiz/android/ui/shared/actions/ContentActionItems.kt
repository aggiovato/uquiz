package com.uquiz.android.ui.shared.actions

import androidx.compose.ui.graphics.Color
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionItem
import com.uquiz.android.ui.designsystem.components.actionsheet.UFabActionKind
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Red700
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.i18n.AppStrings

fun buildLibraryActionItems(
    strings: AppStrings,
    onCreateFolder: () -> Unit,
    onImport: () -> Unit,
): List<UFabActionItem> =
    listOf(
        UFabActionItem(
            id = "create_root_folder",
            label = strings.newFolder,
            description = strings.createFolderActionDescription,
            iconRes = UIcons.Content.Folder.Add,
            containerColor = Navy500,
            contentColor = Color.White,
            onClick = onCreateFolder,
        ),
        UFabActionItem(
            id = "import_uquiz",
            label = strings.importUQuizAction,
            description = strings.importUQuizActionDescription,
            iconRes = UIcons.Actions.Upload,
            containerColor = Teal700,
            contentColor = Color.White,
            onClick = onImport,
        ),
    )

fun buildFolderActionItems(
    strings: AppStrings,
    canExport: Boolean,
    canDeleteCurrent: Boolean,
    onCreatePack: () -> Unit,
    onCreateFolder: () -> Unit,
    onImport: () -> Unit,
    onExport: () -> Unit,
    onDeleteCurrent: () -> Unit,
): List<UFabActionItem> =
    listOf(
        UFabActionItem(
            id = "create_pack",
            label = strings.newPack,
            description = strings.createPackActionDescription,
            iconRes = UIcons.Content.Pack.Add,
            containerColor = Navy500,
            contentColor = Color.White,
            onClick = onCreatePack,
        ),
        UFabActionItem(
            id = "create_subfolder",
            label = strings.newFolder,
            description = strings.createFolderActionDescription,
            iconRes = UIcons.Content.Folder.Add,
            containerColor = Navy500,
            contentColor = Color.White,
            onClick = onCreateFolder,
        ),
        UFabActionItem(
            id = "import_uquiz",
            label = strings.importUQuizAction,
            description = strings.importUQuizActionDescription,
            iconRes = UIcons.Actions.Upload,
            containerColor = Teal700,
            contentColor = Color.White,
            onClick = onImport,
        ),
        UFabActionItem(
            id = "export_subtree",
            label = strings.exportUQuizAction,
            description = strings.exportUQuizActionDescription,
            iconRes = UIcons.Actions.Download,
            containerColor = Teal700,
            contentColor = Color.White,
            enabled = canExport,
            onClick = onExport,
        ),
        UFabActionItem(
            id = "delete_folder",
            label = strings.deleteFolder,
            description = strings.deleteFolderActionDescription,
            iconRes = UIcons.Actions.Delete,
            kind = UFabActionKind.Destructive,
            enabled = canDeleteCurrent,
            containerColor = Red700,
            contentColor = Color.White,
            onClick = onDeleteCurrent,
        ),
    )
