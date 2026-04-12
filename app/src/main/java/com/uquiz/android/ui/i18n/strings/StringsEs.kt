package com.uquiz.android.ui.i18n.strings

import com.uquiz.android.ui.i18n.AppStrings
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

val EsErrors =
    AppErrors(
        folderNameTooLongMessage = "El nombre de la carpeta no puede superar los 100 caracteres.",
        packTitleTooLongMessage = "El título del pack no puede superar los 100 caracteres.",
        packDescriptionTooLongMessage = "La descripción del pack no puede superar los 200 caracteres.",
        invalidContentColorMessage = "Selecciona un color válido de la paleta disponible.",
        invalidContentIconMessage = "Selecciona un icono válido del set disponible.",
        duplicateFolderNameMessage = { name -> "Ya existe una carpeta llamada \"$name\" en este nivel." },
        duplicatePackTitleMessage = { title -> "Ya existe un pack llamado \"$title\" en esta carpeta." },
        invalidImportExtensionMessage = "Selecciona un archivo .uquiz o .uqz.",
        invalidUQuizFormatMessage = "El archivo seleccionado no es un documento UQuiz válido.",
        missingRootFolderImportMessage = "Las importaciones desde biblioteca deben incluir al menos una carpeta raíz.",
        invalidImportRootShapeMessage =
            "El archivo importado debe contener exactamente " +
                "una carpeta raíz o un pack raíz.",
        blankImportedFolderNameMessage = "Las carpetas importadas no pueden tener el nombre vacío.",
        blankImportedPackTitleMessage = "Los packs importados no pueden tener el título vacío.",
        emptyImportedPackMessage = "Los packs importados deben incluir al menos una pregunta.",
        emptyImportedQuestionTextMessage = { index -> "La pregunta $index no tiene texto." },
        importedQuestionNeedsTwoOptionsMessage = { index -> "La pregunta $index debe incluir al menos dos opciones." },
        importedQuestionNeedsSingleCorrectOptionMessage = { index ->
            "La pregunta $index debe incluir " +
                "exactamente una opción correcta."
        },
        duplicateImportedFolderNamesMessage = "Las carpetas importadas no pueden repetir nombres en el mismo nivel.",
        duplicateImportedPackTitlesMessage = "Los packs importados no pueden repetir títulos en el mismo nivel.",
        duplicateImportedOptionLabelsMessage = { index ->
            "La pregunta $index contiene etiquetas de opción duplicadas."
        },
        importReadErrorMessage = "No se pudo leer el archivo seleccionado.",
        exportWriteErrorMessage = "No se pudo guardar el archivo en la ubicación seleccionada.",
        importFailedMessage = "La importación falló.",
        exportFailedMessage = "La exportación falló.",
    )

val EsStrings: AppStrings by lazy {
    AppStrings(
        errors = EsErrors,
        nav =
            NavStrings(
                navHome = "Inicio",
                navLibrary = "Biblioteca",
                navGame = "Juego",
                navStats = "Estadísticas",
                personalStats = "Estadísticas personales",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "Hola, $name!" },
                homeReadyPrompt = "Listo para ponerte a prueba?",
                homeDayStreakLabel = "Racha diaria",
                homeScoreLabel = "Score",
                homeTotalXpLabel = "Total XP",
                homeContinuePlaying = "Continuar jugando",
                homeContinueStudying = "Continuar estudiando",
                homeRandomPlay = "Jugar al azar",
                homeRandomStudy = "Estudiar al azar",
                homeNextLevelLabel = "Siguiente nivel",
                homePointsToUnlockShort = { points -> "+$points pts para desbloquear" },
                homePointsToUnlockRank = { points, rank -> "+$points puntos para desbloquear $rank" },
                homeMaxRankUnlocked = "Rango máximo desbloqueado",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "Repasa con",
                libraryBannerLine2 = "tus",
                libraryBannerLine3 = "tarjetas :)",
                myLibrary = "Mi biblioteca",
                foldersSection = "Carpetas",
                recentPacksSection = "Packs recientes",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "Subcarpetas",
                packsInFolderSection = "Packs en esta carpeta",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "Todavía no hay preguntas",
                newQuestion = "Nueva pregunta",
                questionsSection = { n -> if (n == 1) "Pregunta (1)" else "Preguntas ($n)" },
                filterQuestionsHint = "Buscar pregunta...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "Editor de preguntas",
                newQuestionTitle = "Nueva pregunta",
                editQuestionTitle = "Editar pregunta",
                dangerZoneLabel = "Zona peligrosa",
                questionMarkdownLabel = "Pregunta",
                questionMarkdownHint = "Escribe la pregunta en Markdown",
                explanationMarkdownLabel = "Explicación",
                explanationMarkdownHint = "Escribe una explicación opcional en Markdown",
                explanationPreview = "Vista previa de la explicación",
                optionsSectionTitle = "Opciones",
                addOption = "Añadir opción",
                optionPlaceholder = { n -> "Opción ${n + 1}" },
                correctOptionLabel = "Correcta",
                difficultySectionTitle = "Dificultad",
                deleteQuestionTitle = "Eliminar pregunta",
                deleteQuestionConfirmMessage =
                    "¿Seguro que quieres eliminar esta pregunta? " +
                        "Esta acción no se puede deshacer.",
                deleteQuestionPrimaryMessage = "¿Seguro que quieres eliminar esta pregunta?",
                deleteQuestionSecondaryMessage = "Esta acción no se puede deshacer.",
                deleteQuestionTypeKeywordInstruction = { keyword ->
                    "Escribe \"$keyword\" para habilitar la eliminación."
                },
                deleteQuestionKeyword = "pregunta",
                deleteQuestionActionDescription = "Elimina esta pregunta de forma permanente.",
                createQuestionActionDescription = "Crea una nueva pregunta en este pack.",
                questionCreatedToast = "Pregunta creada",
                questionDeletedToast = "Pregunta eliminada",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "Estudiar pack",
                studyReadyDescription =
                    "Repasa este pack pregunta por pregunta y verifica cada " +
                        "respuesta para ver la explicación al instante.",
                studyResumeStudy = "Continuar estudio",
                studyStartStudy = "Comenzar estudio",
                studyResumeProgress = { current, total -> "Progreso guardado: $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "Pregunta $current de $total" },
                studyPrevious = "Anterior",
                studyNext = "Siguiente",
                studyFinish = "Finalizar",
                studyCorrectTitle = "¡Correcto!",
                studyIncorrectTitle = "Incorrecto",
                studyExplanationLabel = "Explicación",
                studyExitStudy = "Salir del estudio",
                studyExitTitle = "¿Salir de la sesión de estudio?",
                studyExitMessage = "Tu progreso se guardará para que puedas continuar esta sesión más tarde.",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "Todos los packs",
                gameContinueButton = "Continuar",
                gameBannerLetsTest = "Pon a prueba",
                gameBannerYour = "tu",
                gameBannerBrain = "mente :)",
                gameRandomPlay = "Al azar",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "Comenzar partida",
                gameResumeGame = "Retomar partida",
                gameEstimatedTimeLabel = "Tiempo est.",
                gameAnsweredSoFar = { current, total -> "Respondidas $current de $total" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "Puntuación",
                gameTimeoutLabel = "¡Tiempo!",
                gameCorrectFeedback = { pts -> "+$pts pts" },
                gameIncorrectFeedback = { pts -> "−$pts pts" },
                gameExitTitle = "¿Abandonar la partida?",
                gameExitMessage = "Si sales ahora, el progreso de esta partida se perderá.",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "¡Partida completada!",
                gameTotalScoreLabel = "Puntuación total",
                gamePlayAgain = "Jugar otra vez",
                gameRankUpTitle = "¡Rango superior!",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "Progreso de rango",
                gameNextRankLabel = { mmr -> "$mmr pts para el siguiente rango" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "Estadísticas del pack",
                packLastSessionLabel = "Última sesión",
                packMostUsedModeLabel = "Modo más usado",
                packSeeFullStats = "Ver detalles completos",
                packGeneralSummary = "Resumen general",
                packByMode = "Por modo",
                packRecentActivity = "Actividad reciente",
                packSeeAll = "Ver todo",
                packBestPerformance = "Mejor rendimiento",
                packNoSessionsYet = "Aún no hay sesiones",
                packNoGameSessionsYet = "Aún no hay sesiones de juego",
                packNoStudySessionsYet = "Aún no hay sesiones de estudio",
                packQuestionsDominated = { current, total -> "$current / $total preguntas dominadas" },
                packThisWeekLabel = { count -> "$count esta semana" },
                packStatsHelpAction = "Ayuda",
                packStatsHelpTitle = "Cómo funcionan las estadísticas",
                packStatsHelpIntro = "Estas métricas resumen solo las sesiones completadas de un pack.",
                packStatsHelpQuestions =
                    "**Qué mide**" +
                        "\n\n- Cuántas preguntas tiene ahora mismo un pack." +
                        "\n- Cambia si añades o eliminas preguntas." +
                        "\n- Refleja el contenido actual, no el histórico con el que jugaste sesiones pasadas.",
                packStatsHelpAccuracy =
                    "**Cómo se calcula**" +
                        "\n\n- Solo cuenta sesiones completadas." +
                        "\n- Cada sesión aporta su porcentaje de respuestas correctas." +
                        "\n- La tarjeta muestra el promedio de esos porcentajes." +
                        "\n- No incluye sesiones abandonadas ni progreso guardado a medias.",
                packStatsHelpSessions =
                    "**Qué entra aquí**" +
                        "\n\n- Suma sesiones finalizadas en **Estudio** y en **Juego**." +
                        "\n- Si abriste una sesión y la dejaste incompleta, no cuenta." +
                        "\n- Busca medir rendimiento consolidado, no actividad provisional.",
                packStatsHelpProgress =
                    "**Cómo sube**" +
                        "\n\n- Se calcula como preguntas dominadas dividido entre preguntas totales de un pack." +
                        "\n- No usa el avance de una sesión en curso." +
                        "\n- Una pregunta solo suma cuando llega a `MASTERED`.",
                packStatsHelpAverageTime =
                    "**Qué representa**" +
                        "\n\n- Es la duración media de las sesiones completadas de un pack." +
                        "\n- Sirve para estimar cuánto cuesta recorrerlo entero." +
                        "\n- No mide tiempo por pregunta ni tiempo de lectura dentro de una sesión abierta.",
                packStatsHelpLastSession =
                    "**Qué indica**" +
                        "\n\n- Cuándo terminó la sesión completada más reciente." +
                        "\n- Las sesiones abiertas o abandonadas no desplazan este valor.",
                packStatsHelpMostUsedMode =
                    "**Cómo se decide**" +
                        "\n\n- Compara cuántas sesiones completadas hiciste en **Estudio** y en **Juego**." +
                        "\n- El modo con más sesiones terminadas aparece como el más usado.",
                packStatsHelpBestPerformance =
                    "**Qué prioriza**" +
                        "\n\n- Si hay historial de **Juego**, se muestra la mejor puntuación conseguida ahí." +
                        "\n- Si todavía no hay partidas completadas en Juego, se usa el mejor acierto " +
                        "logrado en **Estudio**." +
                        "\n- La idea es destacar el punto más alto de rendimiento real.",
                packStatsHelpMasteryTitle = "Cómo se decide si una pregunta está aprendida",
                packStatsHelpMastery =
                    "**Qué usa la fórmula**" +
                        "\n\n- Precisión acumulada." +
                        "\n- Confianza según número de intentos." +
                        "\n- Un pequeño bonus si ya hubo al menos un intento en **Juego**." +
                        "\n- Un bonus moderado por la racha actual de aciertos." +
                        "\n\n**Niveles**\n\n- `NEW` si el score es menor que `0.20`." +
                        "\n- `LEARNING` entre `0.20` y `0.49`." +
                        "\n- `PRACTICED` entre `0.50` y `0.79`." +
                        "\n- `MASTERED` a partir de `0.80`." +
                        "\n\n**Condiciones extra para `MASTERED`**" +
                        "\n\n- Al menos `3` intentos totales." +
                        "\n- Al menos `2` respuestas correctas." +
                        "\n- Evidencia mínima de uso: `1` intento en Juego o `2` en Estudio." +
                        "\n\nSolo las preguntas en `MASTERED` cuentan como dominadas y " +
                        "hacen subir el progreso.",
            ),
        statsUser =
            UserStatsStrings(
                title = "Tus estadísticas",
                subtitle = "Progreso global entre estudio y juego.",
                bannerLine1 = "Mira",
                bannerLine2 = "tu",
                bannerLine3 = "progreso",
                allFilter = "Todo",
                last7DaysFilter = "Últ. 7d",
                last30DaysFilter = "Últ. 30d",
                answeredQuestions = "Preguntas respondidas",
                totalTime = "Tiempo total",
                averageAnswerTime = "Media por pregunta",
                packsProgress = "Progreso de packs",
                modePerformance = "Rendimiento por modo",
                bestGameScore = "Mejor score",
                averageGameScore = "Score medio",
                masteredQuestions = "Preguntas dominadas",
                accuracyTrend = "Evolución de acierto",
                packBars = "Rendimiento por pack",
                answerSplit = "Aciertos vs errores",
                difficultyPerformance = "Por dificultad",
                questionInsights = "Insights de preguntas",
                fastestQuestion = "Pregunta más rápida",
                mostFailedQuestion = "Pregunta con más fallos",
                noData = "Aún no hay datos",
                packBarsDescription = "Acierto en sesiones · completado del pack",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "Preferencias",
                profileSectionTitle = "Perfil",
                profileSectionSubtitle = "Tu nombre y avatar",
                displayNameLabel = "Nombre",
                displayNameHint = "Tu nombre",
                avatarLabel = "Avatar",
                avatarChoosePhoto = "Elegir foto",
                avatarDeletePhoto = "Eliminar foto",
                appearanceSectionTitle = "Apariencia",
                appearanceSectionSubtitle = "Elige tu tema preferido",
                languageSectionTitle = "Idioma",
                languageSectionSubtitle = "Elige tu idioma preferido",
                langEnglish = "Inglés",
                langSpanish = "Español",
                langCatalan = "Catalán",
                langItalian = "Italiano",
                langJapanese = "Japonés",
                langFrench = "Francés",
                langGerman = "Alemán",
                reminderSectionTitle = "Recordatorio diario",
                reminderSectionSubtitle = "Recibe una notificación para practicar cada día",
                reminderEnabledLabel = "Activar recordatorio diario",
                reminderDaysLabel = "Repetir en",
                reminderTimeLabel = "Hora del recordatorio",
                reminderTimePickerTitle = "Configurar hora",
                savePreferencesActionDescription = "Aplicar los cambios de preferencias.",
                discardPreferencesActionDescription = "Descartar los cambios sin guardar de esta pantalla.",
                dayShortMon = "L",
                dayShortTue = "M",
                dayShortWed = "X",
                dayShortThu = "J",
                dayShortFri = "V",
                dayShortSat = "S",
                dayShortSun = "D",
                confirm = "Confirmar",
            ),
        common =
            CommonStrings(
                back = "Volver",
                cancel = "Cancelar",
                create = "Crear",
                save = "Guardar",
                edit = "Editar",
                delete = "Eliminar",
                toggleTheme = "Cambiar tema",
                seeMore = "Ver más",
                seeLess = "Ver menos",
                comingSoon = "Próximamente",
                reorderLabel = "Ordenar",
                filterLabel = "Filtrar",
                actionsLabel = "Acciones",
                searchPlaceholder = "Buscar carpetas y packs...",
                nothingHereYet = "Nada aquí todavía",
                noSearchResults = "No se han encontrado resultados",
                newFolder = "Nueva carpeta",
                newPack = "Nuevo pack",
                questionsStatLabel = "Preguntas",
                accuracyStatLabel = "Acierto",
                sessionsStatLabel = "Sesiones",
                averageTimeStatLabel = "Tiempo medio",
                progressLabel = "Progreso",
                pointsShortLabel = "pts",
                subfoldersLabel = { n -> if (n == 1) "1 subcarpeta" else "$n subcarpetas" },
                packsLabel = { n -> if (n == 1) "1 pack" else "$n packs" },
                questionsLabel = { n -> if (n == 1) "1 pregunta" else "$n preguntas" },
                difficultyEasy = "Fácil",
                difficultyMedium = "Media",
                difficultyHard = "Difícil",
                difficultyExpert = "Experta",
                studyMode = "Modo estudio",
                studyModeShort = "Estudio",
                studyAction = "Estudiar",
                playMode = "Jugar",
                playModeShort = "Juego",
                studyModeTitle = "Modo estudio",
                gameModeTitle = "Modo juego",
                gameSummaryTitle = "Resumen de la partida",
                studySummaryTitle = "Resumen del estudio",
                studyCorrectAnswersLabel = "Correctas",
                studyIncorrectAnswersLabel = "Incorrectas",
                studyEffectiveTimeLabel = "Tiempo efectivo",
                studyBackToPack = "Volver al pack",
                studyContinueStudy = "Seguir estudiando",
                studyAverageDifficultyLabel = "Dificultad media",
                studyVerifyAnswer = "Verificar respuesta",
                previewLabel = "Vista previa",
                questionPreview = "Vista previa de la pregunta",
                cancelActionDescription = "Cierra este editor sin guardar los cambios.",
                saveActionDescription = "Guarda los cambios actuales de esta pregunta.",
                deleteFolderKeyword = "carpeta",
                deletePackKeyword = "pack",
                deleteKeywordHint = "Escribe la palabra requerida",
                createFolderDescription = "Introduce el nombre de la carpeta a crear y selecciona el color y el icono.",
                editFolderDescription = "Actualiza el nombre de la carpeta y selecciona el color y el icono.",
                folderNameLabel = "Nombre de la carpeta",
                folderNameHint = "Nombre de la carpeta",
                folderColorLabel = "Color",
                folderIconLabel = "Icono",
                editFolder = "Editar carpeta",
                deleteFolder = "Eliminar carpeta",
                deleteFolderConfirmMessage =
                    "¿Estás seguro de que quieres eliminar esta carpeta y " +
                        "todo su contenido? Esto incluye subcarpetas, paquetes de preguntas y todo el progreso vinculado.",
                deleteFolderPrimaryMessage = "¿Seguro que quieres eliminar esta carpeta?",
                deleteFolderSecondaryMessage =
                    "Esto incluye subcarpetas, paquetes de preguntas y " +
                        "todo el progreso vinculado.",
                deleteFolderTypeKeywordInstruction = { keyword ->
                    "Escribe \"$keyword\" para habilitar la eliminación."
                },
                createFolderActionDescription = "Añade una nueva carpeta en el nivel actual.",
                deleteFolderActionDescription = "Elimina esta carpeta y todo su contenido anidado.",
                folderCreatedToast = "Carpeta creada",
                folderDeletedToast = "Carpeta eliminada",
                createPackDescription =
                    "Introduce un nombre para el paquete de preguntas que " +
                        "quieres crear y selecciona el color y el icono.",
                editPackDescription =
                    "Actualiza el nombre del paquete de preguntas y selecciona el " +
                        "color y el icono.",
                packTitleLabel = "Título del pack",
                packTitleHint = "Título del pack",
                packDescriptionLabel = "Descripción",
                packDescriptionHint = "Añade una descripción opcional del pack",
                packColorLabel = "Color",
                packIconLabel = "Icono",
                editPack = "Editar pack",
                deletePack = "Eliminar pack",
                deletePackConfirmMessage =
                    "¿Estás seguro de que quieres eliminar este paquete de " +
                        "preguntas y todo el progreso vinculado?",
                deletePackPrimaryMessage = "¿Seguro que quieres eliminar este pack?",
                deletePackSecondaryMessage = "Esto incluye sus preguntas y todo el progreso vinculado.",
                deletePackTypeKeywordInstruction = { keyword -> "Escribe \"$keyword\" para habilitar la eliminación." },
                createPackActionDescription = "Añade un nuevo pack en la carpeta actual.",
                packCreatedToast = "Mazo creado",
                packDeletedToast = "Mazo eliminado",
                importUQuizAction = "Importar .uquiz",
                exportUQuizAction = "Exportar .uquiz",
                importUQuizActionDescription = "Importa un archivo .uquiz en esta ubicación.",
                exportUQuizActionDescription = "Exporta el contenido actual como archivo .uquiz.",
                importUQuizSuccess = { name -> "\"$name\" se importó correctamente." },
                exportUQuizSuccess = { name -> "\"$name\" se exportó correctamente." },
            ),
    )
}
