package com.uquiz.android.ui.designsystem.tokens

import androidx.annotation.DrawableRes
import com.uquiz.android.R

/**
 * Central icon registry for drawable-based icons used by the design system.
 *
 * UI layers should prefer this object instead of referencing drawable resources directly.
 */
object UIcons {
    /**
     * Decorative assets used as subtle themed backgrounds in surfaces.
     */
    object Decorative {
        @DrawableRes
        val Dialog: Int = R.drawable.bck_image
    }

    /**
     * Action icons used by floating actions, sheets, and contextual controls.
     */
    object Actions {
        @DrawableRes
        val ArrowsUp: Int = R.drawable.uic_action_arrowsup

        @DrawableRes
        val ArrowsDown: Int = R.drawable.uic_action_arrowsdown

        @DrawableRes
        val Upload: Int = R.drawable.uic_action_upload

        @DrawableRes
        val Download: Int = R.drawable.uic_action_download

        @DrawableRes
        val Settings: Int = R.drawable.uic_action_settings

        @DrawableRes
        val Delete: Int = R.drawable.uic_action_delete

        @DrawableRes
        val DeleteAlt: Int = R.drawable.uic_action_delete2

        @DrawableRes
        val Edit: Int = R.drawable.uic_action_edit

        @DrawableRes
        val Details: Int = R.drawable.uic_action_details

        @DrawableRes
        val Filter: Int = R.drawable.uic_action_filter

        @DrawableRes
        val Reorder: Int = R.drawable.uic_action_reorder

        @DrawableRes
        val Drag: Int = R.drawable.uic_action_drag

        @DrawableRes
        val LightMode: Int = R.drawable.uic_action_lightmode

        @DrawableRes
        val DarkMode: Int = R.drawable.uic_action_darkmode

        @DrawableRes
        val Study: Int = R.drawable.uic_select_book

        @DrawableRes
        val Play: Int = R.drawable.uic_menu_game

        @DrawableRes
        val Stats: Int = R.drawable.uic_menu_stats

        @DrawableRes
        val Close: Int = R.drawable.uic_action_close

        @DrawableRes
        val Image: Int = R.drawable.uic_action_image

        @DrawableRes
        val Leave: Int = R.drawable.uic_action_leave

        @DrawableRes
        val See: Int = R.drawable.uic_action_see

        @DrawableRes
        val Add: Int = R.drawable.uic_action_add
    }

    /**
     * Input affordances and utility icons used inside fields and selectors.
     */
    object Inputs {
        @DrawableRes
        val ColorPicker: Int = R.drawable.uic_colorpicker
    }

    /**
     * Full selectable icon catalog used by folder and pack pickers.
     */
    object Select {
        @DrawableRes
        val Folder: Int = R.drawable.uic_select_folder

        @DrawableRes
        val File: Int = R.drawable.uic_select_file

        @DrawableRes
        val History: Int = R.drawable.uic_select_history

        @DrawableRes
        val Globe: Int = R.drawable.uic_select_globe

        @DrawableRes
        val Brain: Int = R.drawable.uic_select_brain

        @DrawableRes
        val Idea: Int = R.drawable.uic_select_idea

        @DrawableRes
        val Math: Int = R.drawable.uic_select_math

        @DrawableRes
        val Molecule: Int = R.drawable.uic_select_molecule

        @DrawableRes
        val Bacteria: Int = R.drawable.uic_select_bacteria

        @DrawableRes
        val Query: Int = R.drawable.uic_select_query

        @DrawableRes
        val Shelf: Int = R.drawable.uic_select_shelf

        @DrawableRes
        val Book: Int = R.drawable.uic_select_book

        @DrawableRes
        val Lang: Int = R.drawable.uic_select_lang

        @DrawableRes
        val Calculate: Int = R.drawable.uic_select_calculate

        @DrawableRes
        val Code: Int = R.drawable.uic_select_code

        @DrawableRes
        val Science: Int = R.drawable.uic_select_science

        @DrawableRes
        val School: Int = R.drawable.uic_select_school
    }

    /**
     * Content icons used to represent folders and packs in different states.
     */
    object Content {
        object Folder {
            @DrawableRes
            val Idle: Int = R.drawable.uic_folder_idle

            @DrawableRes
            val Add: Int = R.drawable.uic_folder_add

            @DrawableRes
            val Up: Int = R.drawable.uic_folder_up

            @DrawableRes
            val Down: Int = R.drawable.uic_folder_down

            @DrawableRes
            val Ok: Int = R.drawable.uic_folder_ok

            @DrawableRes
            val Error: Int = R.drawable.uic_folder_ko
        }

        object Pack {
            @DrawableRes
            val Idle: Int = R.drawable.uic_pack_idle

            @DrawableRes
            val Add: Int = R.drawable.uic_pack_add

            @DrawableRes
            val Up: Int = R.drawable.uic_pack_up

            @DrawableRes
            val Down: Int = R.drawable.uic_pack_down

            @DrawableRes
            val Ok: Int = R.drawable.uic_pack_ok

            @DrawableRes
            val Error: Int = R.drawable.uic_pack_ko
        }
    }

    /**
     * Stat-focused icons used in analytics cards and data summaries.
     */
    object Cards {
        @DrawableRes
        val Question: Int = R.drawable.uic_card_question

        @DrawableRes
        val Check: Int = R.drawable.uic_card_check

        @DrawableRes
        val Session: Int = R.drawable.uic_card_session

        @DrawableRes
        val Progress: Int = R.drawable.uic_card_progress

        @DrawableRes
        val Clock: Int = R.drawable.uic_card_clock

        @DrawableRes
        val User: Int = R.drawable.uic_card_user

        @DrawableRes
        val ArrowUp: Int = R.drawable.uic_card_arrowup

        @DrawableRes
        val Trophy: Int = R.drawable.uic_card_trophy

        @DrawableRes
        val Coins: Int = R.drawable.uic_card_coins
    }

    /**
     * Feedback icons used by the global toast system and status messaging.
     */
    object Feedback {
        @DrawableRes
        val Success: Int = R.drawable.uic_card_check

        @DrawableRes
        val Error: Int = R.drawable.uic_card_error

        @DrawableRes
        val Info: Int = R.drawable.uic_card_info
    }

    /**
     * Icons used by the bottom navigation menu.
     */
    object Menu {
        @DrawableRes
        val Home: Int = R.drawable.uic_menu_home

        @DrawableRes
        val Library: Int = R.drawable.uic_menu_library

        @DrawableRes
        val Game: Int = R.drawable.uic_menu_game

        @DrawableRes
        val Stats: Int = R.drawable.uic_menu_stats
    }

    /**
     * Rank illustrations used to represent the user's current progression stage.
     */
    object Ranks {
        @DrawableRes
        val Initiate: Int = R.drawable.u_initiate

        @DrawableRes
        val Neophyte: Int = R.drawable.u_neophyte

        @DrawableRes
        val Acolyte: Int = R.drawable.u_acolyte

        @DrawableRes
        val Disciple: Int = R.drawable.u_disciple

        @DrawableRes
        val Adept: Int = R.drawable.u_adept

        @DrawableRes
        val Virtuoso: Int = R.drawable.u_virtuoso

        @DrawableRes
        val Archon: Int = R.drawable.u_archon

        @DrawableRes
        val Paragon: Int = R.drawable.u_paragon
    }

    /**
     * Flag icons used by the language selector.
     */
    object Flags {
        @DrawableRes
        val English: Int = R.drawable.uic_flag_en

        @DrawableRes
        val Spanish: Int = R.drawable.uic_flag_es

        @DrawableRes
        val Catalan: Int = R.drawable.uic_flag_ca

        @DrawableRes
        val Italian: Int = R.drawable.uic_flag_it

        @DrawableRes
        val Japanese: Int = R.drawable.uic_flag_ja

        @DrawableRes
        val French: Int = R.drawable.uic_flag_fr

        @DrawableRes
        val German: Int = R.drawable.uic_flag_de
    }
}
