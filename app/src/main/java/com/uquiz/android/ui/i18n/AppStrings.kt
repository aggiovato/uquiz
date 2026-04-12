package com.uquiz.android.ui.i18n

import androidx.compose.runtime.compositionLocalOf
import com.uquiz.android.ui.i18n.model.AppErrors
import com.uquiz.android.ui.i18n.model.CommonStrings
import com.uquiz.android.ui.i18n.model.FolderStrings
import com.uquiz.android.ui.i18n.model.GameHomeStrings
import com.uquiz.android.ui.i18n.model.GameIntroStrings
import com.uquiz.android.ui.i18n.model.GameSessionStrings
import com.uquiz.android.ui.i18n.model.GameSummaryStrings
import com.uquiz.android.ui.i18n.model.HomeStrings
import com.uquiz.android.ui.i18n.model.LibraryStrings
import com.uquiz.android.ui.i18n.model.NavStrings
import com.uquiz.android.ui.i18n.model.PackStatsStrings
import com.uquiz.android.ui.i18n.model.PackStrings
import com.uquiz.android.ui.i18n.model.PreferencesStrings
import com.uquiz.android.ui.i18n.model.QuestionStrings
import com.uquiz.android.ui.i18n.model.StudyIntroStrings
import com.uquiz.android.ui.i18n.model.StudySessionStrings
import com.uquiz.android.ui.i18n.model.UserStatsStrings
import com.uquiz.android.ui.i18n.strings.CaStrings
import com.uquiz.android.ui.i18n.strings.DeStrings
import com.uquiz.android.ui.i18n.strings.EnStrings
import com.uquiz.android.ui.i18n.strings.EsStrings
import com.uquiz.android.ui.i18n.strings.FrStrings
import com.uquiz.android.ui.i18n.strings.ItStrings
import com.uquiz.android.ui.i18n.strings.JaStrings

// ─── Clase principal ──────────────────────────────────────────────────────────

/**
 * Contenedor raíz de todos los strings localizados de la aplicación.
 * Agrupa los textos por pantalla/feature para evitar el límite de 256 argumentos de DEX.
 * Acceder siempre a través de [LocalStrings] o [stringsFor].
 */
class AppStrings(
    /** Mensajes de error localizados — consumidos por UiMessageResolver. */
    val errors: AppErrors,
    val nav: NavStrings,
    val home: HomeStrings,
    val library: LibraryStrings,
    val folder: FolderStrings,
    val pack: PackStrings,
    val question: QuestionStrings,
    val studyIntro: StudyIntroStrings,
    val studySession: StudySessionStrings,
    val gameHome: GameHomeStrings,
    val gameIntro: GameIntroStrings,
    val gameSession: GameSessionStrings,
    val gameSummary: GameSummaryStrings,
    val statsPack: PackStatsStrings,
    val statsUser: UserStatsStrings,
    val preferences: PreferencesStrings,
    val common: CommonStrings,
)

val LocalStrings = compositionLocalOf { EnStrings }

fun stringsFor(code: String): AppStrings =
    when (code) {
        "es" -> EsStrings
        "fr" -> FrStrings
        "de" -> DeStrings
        "ca" -> CaStrings
        "it" -> ItStrings
        "ja" -> JaStrings
        else -> EnStrings
    }
