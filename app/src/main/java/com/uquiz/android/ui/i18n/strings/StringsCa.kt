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

val CaErrors =
    AppErrors(
        folderNameTooLongMessage = "El nom de la carpeta no pot superar els 100 caràcters.",
        packTitleTooLongMessage = "El títol del pack no pot superar els 100 caràcters.",
        packDescriptionTooLongMessage = "La descripció del pack no pot superar els 200 caràcters.",
        duplicateFolderNameMessage = { name -> "Ja existeix una carpeta anomenada \"$name\" en aquest nivell." },
        duplicatePackTitleMessage = { title -> "Ja existeix un pack anomenat \"$title\" en aquesta carpeta." },
        invalidImportExtensionMessage = "Selecciona un fitxer .uquiz o .uqz.",
        invalidUQuizFormatMessage = "El fitxer seleccionat no és un document UQuiz vàlid.",
        missingRootFolderImportMessage = "Les importacions des de biblioteca han d'incloure almenys una carpeta arrel.",
        invalidImportRootShapeMessage =
            "El fitxer importat ha de contenir exactament " +
                "una carpeta arrel o un pack arrel.",
        blankImportedFolderNameMessage = "Les carpetes importades no poden tenir el nom buit.",
        blankImportedPackTitleMessage = "Els packs importats no poden tenir el títol buit.",
        emptyImportedPackMessage = "Els packs importats han d'incloure almenys una pregunta.",
        emptyImportedQuestionTextMessage = { index -> "La pregunta $index no té text." },
        importedQuestionNeedsTwoOptionsMessage = { index -> "La pregunta $index ha d'incloure almenys dues opcions." },
        importedQuestionNeedsSingleCorrectOptionMessage = { index ->
            "La pregunta $index ha d'incloure " +
                "exactament una opció correcta."
        },
        duplicateImportedFolderNamesMessage = "Les carpetes importades no poden repetir noms al mateix nivell.",
        duplicateImportedPackTitlesMessage = "Els packs importats no poden repetir títols al mateix nivell.",
        duplicateImportedOptionLabelsMessage = { index -> "La pregunta $index conté etiquetes d'opció duplicades." },
        importReadErrorMessage = "No s'ha pogut llegir el fitxer seleccionat.",
        exportWriteErrorMessage = "No s'ha pogut desar el fitxer a la ubicació seleccionada.",
        importFailedMessage = "La importació ha fallat.",
        exportFailedMessage = "L'exportació ha fallat.",
    )

val CaStrings: AppStrings by lazy {
    AppStrings(
        errors = CaErrors,
        nav =
            NavStrings(
                navHome = "Inici",
                navLibrary = "Biblioteca",
                navGame = "Joc",
                navStats = "Estadístiques",
                personalStats = "Estadístiques personals",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "Hola, $name!" },
                homeReadyPrompt = "Preparat per posar-te a prova?",
                homeDayStreakLabel = "Ratxa diària",
                homeScoreLabel = "Score",
                homeTotalXpLabel = "XP total",
                homeContinuePlaying = "Continua jugant",
                homeContinueStudying = "Continua estudiant",
                homeRandomPlay = "Jugar a l'atzar",
                homeRandomStudy = "Estudiar a l'atzar",
                homeNextLevelLabel = "Nivell següent",
                homePointsToUnlockShort = { points -> "+$points pts per desbloquejar" },
                homePointsToUnlockRank = { points, rank -> "+$points punts per desbloquejar $rank" },
                homeMaxRankUnlocked = "Rang màxim desbloquejat",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "Repassa amb",
                libraryBannerLine2 = "les teves",
                libraryBannerLine3 = "fitxes :)",
                myLibrary = "La meva biblioteca",
                foldersSection = "Carpetes",
                recentPacksSection = "Packs recents",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "Subcarpetes",
                packsInFolderSection = "Packs en aquesta carpeta",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "Sense preguntes encara",
                newQuestion = "Nova pregunta",
                questionsSection = { n -> if (n == 1) "Pregunta (1)" else "Preguntes ($n)" },
                filterQuestionsHint = "Cerca pregunta...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "Editor de preguntes",
                newQuestionTitle = "Nova pregunta",
                editQuestionTitle = "Editar pregunta",
                dangerZoneLabel = "Zona perillosa",
                questionMarkdownLabel = "Pregunta",
                questionMarkdownHint = "Escriu la pregunta en Markdown",
                explanationMarkdownLabel = "Explicació",
                explanationMarkdownHint = "Escriu una explicació opcional en Markdown",
                explanationPreview = "Vista prèvia de l'explicació",
                optionsSectionTitle = "Opcions",
                addOption = "Afegir opció",
                optionPlaceholder = { n -> "Opció ${n + 1}" },
                correctOptionLabel = "Correcta",
                difficultySectionTitle = "Dificultat",
                deleteQuestionTitle = "Eliminar pregunta",
                deleteQuestionConfirmMessage =
                    "Segur que vols eliminar aquesta pregunta? " +
                        "Aquesta acció no es pot desfer.",
                deleteQuestionPrimaryMessage = "Segur que vols eliminar aquesta pregunta?",
                deleteQuestionSecondaryMessage = "Aquesta acció no es pot desfer.",
                deleteQuestionTypeKeywordInstruction = { keyword -> "Escriu \"$keyword\" per habilitar l'eliminació." },
                deleteQuestionKeyword = "pregunta",
                deleteQuestionActionDescription = "Elimina aquesta pregunta de manera permanent.",
                createQuestionActionDescription = "Crea una pregunta nova en aquest pack.",
                questionCreatedToast = "Pregunta creada",
                questionDeletedToast = "Pregunta eliminada",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "Estudiar pack",
                studyReadyDescription =
                    "Repassa aquest pack pregunta per pregunta i verifica cada " +
                        "resposta per veure l'explicació immediatament.",
                studyResumeStudy = "Continuar estudi",
                studyStartStudy = "Començar estudi",
                studyResumeProgress = { current, total -> "Progrés desat: $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "Pregunta $current de $total" },
                studyPrevious = "Anterior",
                studyNext = "Següent",
                studyFinish = "Finalitza",
                studyCorrectTitle = "Correcte!",
                studyIncorrectTitle = "Incorrecte",
                studyExplanationLabel = "Explicació",
                studyExitStudy = "Surt de l'estudi",
                studyExitTitle = "Vols sortir de la sessió d'estudi?",
                studyExitMessage = "El teu progrés es desarà perquè puguis continuar aquesta sessió més tard.",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "Tots els packs",
                gameContinueButton = "Continuar",
                gameBannerLetsTest = "Posem a prova",
                gameBannerYour = "el teu",
                gameBannerBrain = "cervell :)",
                gameRandomPlay = "Partida aleatòria",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "Iniciar partida",
                gameResumeGame = "Reprendre partida",
                gameEstimatedTimeLabel = "Temps est.",
                gameAnsweredSoFar = { current, total -> "Has respost $current de $total" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "Puntuació",
                gameTimeoutLabel = "Temps esgotat!",
                gameCorrectFeedback = { pts -> "+$pts pts" },
                gameIncorrectFeedback = { pts -> "−$pts pts" },
                gameExitTitle = "Abandonar la partida?",
                gameExitMessage = "Si surts ara, el progrés d'aquesta partida es perdrà.",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "Partida completada!",
                gameTotalScoreLabel = "Puntuació total",
                gamePlayAgain = "Tornar a jugar",
                gameRankUpTitle = "Rang superior!",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "Progrés de rang",
                gameNextRankLabel = { mmr -> "$mmr MMR per al rang següent" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "Estadístiques del pack",
                packLastSessionLabel = "Última sessió",
                packMostUsedModeLabel = "Mode més utilitzat",
                packSeeFullStats = "Veure tots els detalls",
                packGeneralSummary = "Resum general",
                packByMode = "Per mode",
                packRecentActivity = "Activitat recent",
                packSeeAll = "Veure-ho tot",
                packBestPerformance = "Millor rendiment",
                packNoSessionsYet = "Encara no hi ha sessions",
                packNoGameSessionsYet = "Encara no hi ha sessions de joc",
                packNoStudySessionsYet = "Encara no hi ha sessions d'estudi",
                packQuestionsDominated = { current, total -> "$current / $total preguntes dominades" },
                packThisWeekLabel = { n -> if (n == 1) "1 sessió aquesta setmana" else "$n sessions aquesta setmana" },
                packStatsHelpAction = "Ajuda",
                packStatsHelpTitle = "Com funcionen les estadístiques",
                packStatsHelpIntro = "Aquestes mètriques resumeixen només les sessions completades d'aquest pack.",
                packStatsHelpQuestions =
                    "**Què mesura**" +
                        "\n\n- Quantes preguntes té actualment el pack." +
                        "\n- Canvia si afegeixes o elimines preguntes." +
                        "\n- Reflecteix el contingut actual, no el contingut històric de sessions antigues.",
                packStatsHelpAccuracy =
                    "**Com es calcula**" +
                        "\n\n- Només compten les sessions completades." +
                        "\n- Cada sessió acabada aporta la seva proporció de respostes correctes." +
                        "\n- La targeta mostra la mitjana d'aquestes proporcions." +
                        "\n- Les sessions abandonades o en curs no hi estan incloses.",
                packStatsHelpSessions =
                    "**Què compta aquí**" +
                        "\n\n- Sessions finalitzades en **Estudi** i en **Joc**." +
                        "\n- Si vas obrir una sessió i no la vas acabar, no compta." +
                        "\n- Mesura rendiment consolidat, no activitat provisional.",
                packStatsHelpProgress =
                    "**Com puja**" +
                        "\n\n- Es calcula com preguntes dominades dividides pel total de preguntes del pack." +
                        "\n- No usa el progrés actiu d'una sessió en curs." +
                        "\n- Una pregunta només suma quan arriba a `MASTERED`.",
                packStatsHelpAverageTime =
                    "**Què representa**" +
                        "\n\n- Durada mitjana de les sessions completades del pack." +
                        "\n- Útil per estimar quant triga una passada completa." +
                        "\n- No mesura el temps per pregunta ni el temps de lectura dins una sessió oberta.",
                packStatsHelpLastSession =
                    "**Què indica**" +
                        "\n\n- Quan es va acabar la sessió completada més recent." +
                        "\n- Les sessions obertes o abandonades no actualitzen aquest valor.",
                packStatsHelpMostUsedMode =
                    "**Com es decideix**" +
                        "\n\n- Compara les sessions **Estudi** completades amb les sessions **Joc** completades." +
                        "\n- El mode amb més sessions acabades apareix com el més utilitzat.",
                packStatsHelpBestPerformance =
                    "**Què prioritza**" +
                        "\n\n- Si hi ha historial **Joc** completat, es mostra la millor puntuació obtinguda." +
                        "\n- Si encara no hi ha sessions Joc completades, s'usa la millor precisió en **Estudi**." +
                        "\n- L'objectiu és destacar el punt de màxim rendiment real.",
                packStatsHelpMasteryTitle = "Com decideix l'app que una pregunta està apresa",
                packStatsHelpMastery =
                    "**Què usa la fórmula**" +
                        "\n\n- Precisió acumulada." +
                        "\n- Confiança pel nombre d'intents." +
                        "\n- Un petit bonus si la pregunta ja ha aparegut en **Joc**." +
                        "\n- Un bonus moderat per la ratxa actual d'encerts." +
                        "\n\n**Nivells**" +
                        "\n\n- `NEW` si el score és inferior a `0.20`." +
                        "\n- `LEARNING` entre `0.20` i `0.49`." +
                        "\n- `PRACTICED` entre `0.50` i `0.79`." +
                        "\n- `MASTERED` a partir de `0.80`." +
                        "\n\n**Condicions extra per a `MASTERED`**" +
                        "\n\n- Almenys `3` intents totals." +
                        "\n- Almenys `2` respostes correctes." +
                        "\n- Evidència mínima d'ús: `1` intent en Joc o `2` intents en Estudi." +
                        "\n\nNomés les preguntes `MASTERED` compten com a dominades i fan " +
                        "pujar el progrés del pack.",
            ),
        statsUser =
            UserStatsStrings(
                title = "Les teves estadístiques",
                subtitle = "Progrés global entre estudi i joc.",
                bannerLine1 = "Descobreix",
                bannerLine2 = "el teu",
                bannerLine3 = "progrés",
                allFilter = "Tot",
                last7DaysFilter = "Últ. 7d",
                last30DaysFilter = "Últ. 30d",
                answeredQuestions = "Preguntes respostes",
                totalTime = "Temps total",
                averageAnswerTime = "Mitjana per pregunta",
                packsProgress = "Progrés de packs",
                modePerformance = "Rendiment per mode",
                bestGameScore = "Millor score",
                averageGameScore = "Score mitjà",
                masteredQuestions = "Preguntes dominades",
                accuracyTrend = "Evolució d'encert",
                packBars = "Rendiment per pack",
                answerSplit = "Encerts vs errors",
                difficultyPerformance = "Per dificultat",
                questionInsights = "Insights de preguntes",
                fastestQuestion = "Pregunta més ràpida",
                mostFailedQuestion = "Pregunta amb més errors",
                noData = "Encara no hi ha dades",
                packBarsDescription = "Encert per sessió · progrés del pack",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "Preferències",
                profileSectionTitle = "Perfil",
                profileSectionSubtitle = "El teu nom i avatar",
                displayNameLabel = "Nom",
                displayNameHint = "El teu nom",
                avatarLabel = "Avatar",
                avatarChoosePhoto = "Triar foto",
                avatarDeletePhoto = "Eliminar foto",
                appearanceSectionTitle = "Aparença",
                appearanceSectionSubtitle = "Tria el tema preferit",
                languageSectionTitle = "Idioma",
                languageSectionSubtitle = "Tria el teu idioma preferit",
                langEnglish = "Anglès",
                langSpanish = "Espanyol",
                langCatalan = "Català",
                langItalian = "Italià",
                langJapanese = "Japonès",
                langFrench = "Francès",
                langGerman = "Alemany",
                reminderSectionTitle = "Recordatori diari",
                reminderSectionSubtitle = "Rep una notificació per practicar cada dia",
                reminderEnabledLabel = "Activar recordatori diari",
                reminderDaysLabel = "Repetir a",
                reminderTimeLabel = "Hora del recordatori",
                reminderTimePickerTitle = "Configura l'hora",
                savePreferencesActionDescription = "Aplica els canvis de preferències.",
                discardPreferencesActionDescription = "Descarta els canvis sense desar d'aquesta pantalla.",
                dayShortMon = "Dl",
                dayShortTue = "Dt",
                dayShortWed = "Dc",
                dayShortThu = "Dj",
                dayShortFri = "Dv",
                dayShortSat = "Ds",
                dayShortSun = "Dg",
                confirm = "Confirmar",
            ),
        common =
            CommonStrings(
                back = "Tornar",
                cancel = "Cancel·lar",
                create = "Crear",
                save = "Desar",
                edit = "Editar",
                delete = "Eliminar",
                toggleTheme = "Canviar tema",
                seeMore = "Veure més",
                seeLess = "Veure menys",
                comingSoon = "Properament",
                reorderLabel = "Reordenar",
                filterLabel = "Filtrar",
                actionsLabel = "Accions",
                searchPlaceholder = "Cerca carpetes i packs...",
                nothingHereYet = "Res aquí encara",
                noSearchResults = "No s'han trobat resultats",
                newFolder = "Nova carpeta",
                newPack = "Nou pack",
                questionsStatLabel = "Preguntes",
                accuracyStatLabel = "Precisió",
                sessionsStatLabel = "Sessions",
                averageTimeStatLabel = "Temps mitjà",
                progressLabel = "Progrés",
                pointsShortLabel = "pts",
                subfoldersLabel = { n -> if (n == 1) "1 subcarpeta" else "$n subcarpetes" },
                packsLabel = { n -> if (n == 1) "1 pack" else "$n packs" },
                questionsLabel = { n -> if (n == 1) "1 pregunta" else "$n preguntes" },
                difficultyEasy = "Fàcil",
                difficultyMedium = "Mitjana",
                difficultyHard = "Difícil",
                difficultyExpert = "Expert",
                studyMode = "Mode estudi",
                studyModeShort = "Estudi",
                studyAction = "Estudiar",
                playMode = "Jugar",
                playModeShort = "Joc",
                studyModeTitle = "Mode estudi",
                gameModeTitle = "Mode joc",
                gameSummaryTitle = "Resum de la partida",
                studySummaryTitle = "Resum de l'estudi",
                studyCorrectAnswersLabel = "Correctes",
                studyIncorrectAnswersLabel = "Incorrectes",
                studyEffectiveTimeLabel = "Temps efectiu",
                studyBackToPack = "Torna al pack",
                studyContinueStudy = "Continua estudiant",
                studyAverageDifficultyLabel = "Dificultat mitjana",
                studyVerifyAnswer = "Verifica la resposta",
                previewLabel = "Vista prèvia",
                questionPreview = "Vista prèvia de la pregunta",
                cancelActionDescription = "Tanca aquest editor sense desar els canvis.",
                saveActionDescription = "Desa els canvis actuals d'aquesta pregunta.",
                deleteFolderKeyword = "carpeta",
                deletePackKeyword = "pack",
                deleteKeywordHint = "Escriu la paraula requerida",
                createFolderDescription =
                    "Introdueix el nom de la carpeta que vols crear i selecciona " +
                        "el color i la icona.",
                editFolderDescription = "Actualitza el nom de la carpeta i selecciona el color i la icona.",
                folderNameLabel = "Nom de la carpeta",
                folderNameHint = "Nom de la carpeta",
                folderColorLabel = "Color",
                folderIconLabel = "Icona",
                editFolder = "Editar carpeta",
                deleteFolder = "Eliminar carpeta",
                deleteFolderConfirmMessage =
                    "Segur que vols eliminar aquesta carpeta i tot el seu contingut? " +
                        "Això inclou subcarpetes, paquets de preguntes i tot el progrés vinculat.",
                deleteFolderPrimaryMessage = "Segur que vols eliminar aquesta carpeta?",
                deleteFolderSecondaryMessage =
                    "Això inclou subcarpetes, paquets de preguntes i tot el " +
                        "progrés vinculat.",
                deleteFolderTypeKeywordInstruction = { keyword ->
                    "Escriu \"$keyword\" per habilitar l'eliminació."
                },
                createFolderActionDescription = "Afegeix una carpeta nova al nivell actual.",
                deleteFolderActionDescription = "Elimina aquesta carpeta i tot el contingut que conté.",
                folderCreatedToast = "Carpeta creada",
                folderDeletedToast = "Carpeta eliminada",
                createPackDescription =
                    "Introdueix un nom per al paquet de preguntes " +
                        "que vols crear i selecciona el color i la icona.",
                editPackDescription = "Actualitza el nom del paquet de preguntes i selecciona el color i la icona.",
                packTitleLabel = "Títol del pack",
                packTitleHint = "Títol del pack",
                packDescriptionLabel = "Descripció",
                packDescriptionHint = "Afegeix una descripció opcional del pack",
                packColorLabel = "Color",
                packIconLabel = "Icona",
                editPack = "Editar pack",
                deletePack = "Eliminar pack",
                deletePackConfirmMessage =
                    "Segur que vols eliminar aquest paquet de preguntes i tot el " +
                        "progrés vinculat?",
                deletePackPrimaryMessage = "Segur que vols eliminar aquest pack?",
                deletePackSecondaryMessage = "Això inclou les seves preguntes i tot el progrés vinculat.",
                deletePackTypeKeywordInstruction = { keyword -> "Escriu \"$keyword\" per habilitar l'eliminació." },
                createPackActionDescription = "Afegeix un pack nou a la carpeta actual.",
                packCreatedToast = "Mazo creat",
                packDeletedToast = "Mazo eliminat",
                importUQuizAction = "Importar .uquiz",
                exportUQuizAction = "Exportar .uquiz",
                importUQuizActionDescription = "Importa un fitxer .uquiz en aquesta ubicació.",
                exportUQuizActionDescription = "Exporta el contingut actual com a fitxer .uquiz.",
                importUQuizSuccess = { name -> "\"$name\" s'ha importat correctament." },
                exportUQuizSuccess = { name -> "\"$name\" s'ha exportat correctament." },
            ),
    )
}
