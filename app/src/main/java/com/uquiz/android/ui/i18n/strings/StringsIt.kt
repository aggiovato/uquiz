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

val ItErrors =
    AppErrors(
        folderNameTooLongMessage = "Il nome della cartella non può superare i 100 caratteri.",
        packTitleTooLongMessage = "Il titolo del pack non può superare i 100 caratteri.",
        packDescriptionTooLongMessage = "La descrizione del pack non può superare i 200 caratteri.",
        invalidContentColorMessage = "Seleziona un colore valido dalla palette disponibile.",
        invalidContentIconMessage = "Seleziona un'icona valida dal set disponibile.",
        duplicateFolderNameMessage = { name -> "Esiste già una cartella chiamata \"$name\" a questo livello." },
        duplicatePackTitleMessage = { title -> "Esiste già un pack chiamato \"$title\" in questa cartella." },
        invalidImportExtensionMessage = "Seleziona un file .uquiz o .uqz.",
        invalidUQuizFormatMessage = "Il file selezionato non è un documento UQuiz valido.",
        missingRootFolderImportMessage =
            "Le importazioni dalla Libreria devono includere almeno una " +
                "cartella radice.",
        invalidImportRootShapeMessage =
            "Il file importato deve contenere esattamente una " +
                "cartella radice o un pack radice.",
        blankImportedFolderNameMessage = "Le cartelle importate non possono avere un nome vuoto.",
        blankImportedPackTitleMessage = "I pack importati non possono avere un titolo vuoto.",
        emptyImportedPackMessage = "I pack importati devono includere almeno una domanda.",
        emptyImportedQuestionTextMessage = { index -> "La domanda $index non ha testo." },
        importedQuestionNeedsTwoOptionsMessage = { index -> "La domanda $index deve includere almeno due opzioni." },
        importedQuestionNeedsSingleCorrectOptionMessage = { index ->
            "La domanda $index deve includere " +
                "esattamente una risposta corretta."
        },
        duplicateImportedFolderNamesMessage = "Le cartelle importate non possono ripetere nomi allo stesso livello.",
        duplicateImportedPackTitlesMessage = "I pack importati non possono ripetere titoli allo stesso livello.",
        duplicateImportedOptionLabelsMessage = { index ->
            "La domanda $index contiene etichette di opzione duplicate."
        },
        importReadErrorMessage = "Non è stato possibile leggere il file selezionato.",
        exportWriteErrorMessage = "Non è stato possibile salvare il file nella posizione selezionata.",
        importFailedMessage = "Importazione non riuscita.",
        exportFailedMessage = "Esportazione non riuscita.",
    )

val ItStrings: AppStrings by lazy {
    AppStrings(
        errors = ItErrors,
        nav =
            NavStrings(
                navHome = "Home",
                navLibrary = "Libreria",
                navGame = "Gioco",
                navStats = "Statistiche",
                personalStats = "Statistiche personali",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "Ciao, $name!" },
                homeReadyPrompt = "Pronto a metterti alla prova?",
                homeDayStreakLabel = "Serie giornaliera",
                homeScoreLabel = "Score",
                homeTotalXpLabel = "XP totale",
                homeContinuePlaying = "Continua a giocare",
                homeContinueStudying = "Continua a studiare",
                homeRandomPlay = "Gioca a caso",
                homeRandomStudy = "Studia a caso",
                homeNextLevelLabel = "Livello successivo",
                homePointsToUnlockShort = { points -> "+$points pt per sbloccare" },
                homePointsToUnlockRank = { points, rank -> "+$points punti per sbloccare $rank" },
                homeMaxRankUnlocked = "Rango massimo sbloccato",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "Studia con",
                libraryBannerLine2 = "le tue",
                libraryBannerLine3 = "schede :)",
                myLibrary = "La mia libreria",
                foldersSection = "Cartelle",
                recentPacksSection = "Pack recenti",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "Sottocartelle",
                packsInFolderSection = "Pack in questa cartella",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "Nessuna domanda ancora",
                newQuestion = "Nuova domanda",
                questionsSection = { n -> if (n == 1) "Domanda (1)" else "Domande ($n)" },
                filterQuestionsHint = "Filtra domande...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "Editor domande",
                newQuestionTitle = "Nuova domanda",
                editQuestionTitle = "Modifica domanda",
                dangerZoneLabel = "Zona pericolosa",
                questionMarkdownLabel = "Domanda",
                questionMarkdownHint = "Scrivi la domanda in Markdown",
                explanationMarkdownLabel = "Spiegazione",
                explanationMarkdownHint = "Scrivi una spiegazione facoltativa in Markdown",
                explanationPreview = "Anteprima della spiegazione",
                optionsSectionTitle = "Opzioni",
                addOption = "Aggiungi opzione",
                optionPlaceholder = { n -> "Opzione ${n + 1}" },
                correctOptionLabel = "Corretta",
                difficultySectionTitle = "Difficoltà",
                deleteQuestionTitle = "Elimina domanda",
                deleteQuestionConfirmMessage =
                    "Sei sicuro di voler eliminare questa domanda? " +
                        "Questa azione non può essere annullata.",
                deleteQuestionPrimaryMessage = "Sei sicuro di voler eliminare questa domanda?",
                deleteQuestionSecondaryMessage = "Questa azione non può essere annullata.",
                deleteQuestionTypeKeywordInstruction = { keyword ->
                    "Digita \"$keyword\" per abilitare l'eliminazione."
                },
                deleteQuestionKeyword = "domanda",
                deleteQuestionActionDescription = "Elimina definitivamente questa domanda.",
                createQuestionActionDescription = "Crea una nuova domanda in questo pack.",
                questionCreatedToast = "Domanda creata",
                questionDeletedToast = "Domanda eliminata",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "Studia pack",
                studyReadyDescription =
                    "Ripassa questo pack domanda per domanda e verifica " +
                        "ogni risposta per vedere subito la spiegazione.",
                studyResumeStudy = "Riprendi studio",
                studyStartStudy = "Inizia studio",
                studyResumeProgress = { current, total -> "Progresso salvato: $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "Domanda $current di $total" },
                studyPrevious = "Precedente",
                studyNext = "Successiva",
                studyFinish = "Termina",
                studyCorrectTitle = "Corretto!",
                studyIncorrectTitle = "Non corretto",
                studyExplanationLabel = "Spiegazione",
                studyExitStudy = "Esci dallo studio",
                studyExitTitle = "Vuoi uscire dalla sessione di studio?",
                studyExitMessage =
                    "I tuoi progressi verranno salvati così potrai " +
                        "riprendere questa sessione più tardi.",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "Tutti i pack",
                gameContinueButton = "Continua",
                gameBannerLetsTest = "Sfida",
                gameBannerYour = "la tua",
                gameBannerBrain = "mente :)",
                gameRandomPlay = "A caso",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "Inizia partita",
                gameResumeGame = "Riprendi partita",
                gameEstimatedTimeLabel = "Tempo est.",
                gameAnsweredSoFar = { current, total -> "Hai risposto a $current su $total" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "Punteggio",
                gameTimeoutLabel = "Tempo scaduto!",
                gameCorrectFeedback = { pts -> "+$pts pt" },
                gameIncorrectFeedback = { pts -> "−$pts pt" },
                gameExitTitle = "Abbandonare la partita?",
                gameExitMessage = "Se esci adesso, i progressi della partita andranno persi.",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "Partita completata!",
                gameTotalScoreLabel = "Punteggio totale",
                gamePlayAgain = "Gioca ancora",
                gameRankUpTitle = "Salito di grado!",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "Progresso grado",
                gameNextRankLabel = { mmr -> "$mmr MMR al grado successivo" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "Statistiche del pack",
                packLastSessionLabel = "Ultima sessione",
                packMostUsedModeLabel = "Modalità più usata",
                packSeeFullStats = "Vedi dettagli completi",
                packGeneralSummary = "Riepilogo generale",
                packByMode = "Per modalità",
                packRecentActivity = "Attività recente",
                packSeeAll = "Vedi tutto",
                packBestPerformance = "Migliore prestazione",
                packNoSessionsYet = "Ancora nessuna sessione",
                packNoGameSessionsYet = "Ancora nessuna sessione di gioco",
                packNoStudySessionsYet = "Ancora nessuna sessione di studio",
                packQuestionsDominated = { current, total -> "$current / $total domande padroneggiate" },
                packThisWeekLabel = { n ->
                    if (n ==
                        1
                    ) {
                        "1 sessione questa settimana"
                    } else {
                        "$n sessioni questa settimana"
                    }
                },
                packStatsHelpAction = "Aiuto",
                packStatsHelpTitle = "Come funzionano le statistiche",
                packStatsHelpIntro = "Queste metriche riassumono solo le sessioni completate di questo pack.",
                packStatsHelpQuestions =
                    "**Cosa misura**" +
                        "\n\n- Quante domande ha attualmente il pack." +
                        "\n- Cambia se aggiungi o rimuovi domande." +
                        "\n- Riflette il contenuto attuale, non quello storico delle sessioni precedenti.",
                packStatsHelpAccuracy =
                    "**Come si calcola**" +
                        "\n\n- Solo le sessioni completate contano." +
                        "\n- Ogni sessione terminata contribuisce con la sua percentuale di risposte corrette." +
                        "\n- La scheda mostra la media di quelle percentuali." +
                        "\n- Le sessioni abbandonate o in corso sono escluse.",
                packStatsHelpSessions =
                    "**Cosa conta qui**" +
                        "\n\n- Le sessioni concluse in **Studio** e in **Gioco**." +
                        "\n- Una sessione aperta ma non completata non conta." +
                        "\n- Misura la performance consolidata, non l'attività provvisoria.",
                packStatsHelpProgress =
                    "**Come cresce**" +
                        "\n\n- Si calcola come domande padroneggiate divise per il totale delle domande del pack." +
                        "\n- Non usa il progresso attivo di una sessione in corso." +
                        "\n- Una domanda contribuisce solo quando raggiunge `MASTERED`.",
                packStatsHelpAverageTime =
                    "**Cosa rappresenta**" +
                        "\n\n- Durata media delle sessioni completate del pack." +
                        "\n- Aiuta a stimare quanto dura un giro completo." +
                        "\n- Non misura il tempo per domanda né il tempo di lettura dentro una sessione aperta.",
                packStatsHelpLastSession =
                    "**Cosa indica**" +
                        "\n\n- Quando si è conclusa la sessione completata più recente." +
                        "\n- Le sessioni aperte o abbandonate non aggiornano questo valore.",
                packStatsHelpMostUsedMode =
                    "**Come si decide**" +
                        "\n\n- Confronta le sessioni **Studio** completate con quelle **Gioco** completate." +
                        "\n- La modalità con più sessioni terminate appare come la più usata.",
                packStatsHelpBestPerformance =
                    "**Cosa priorizza**" +
                        "\n\n- Se esiste una cronologia **Gioco** completata, viene mostrato il miglior punteggio." +
                        "\n- Se non ci sono ancora sessioni Gioco concluse, si usa la migliore " +
                        "accuratezza in **Studio**." +
                        "\n- L'obiettivo è evidenziare il punto più alto di performance reale.",
                packStatsHelpMasteryTitle = "Come l'app decide che una domanda è appresa",
                packStatsHelpMastery =
                    "**Cosa usa la formula**" +
                        "\n\n- Accuratezza cumulata." +
                        "\n- Fiducia in base al numero di tentativi." +
                        "\n- Un piccolo bonus se la domanda è già apparsa in **Gioco**." +
                        "\n- Un bonus moderato per la serie attuale di risposte corrette." +
                        "\n\n**Livelli**" +
                        "\n\n- `NEW` se lo score è sotto `0.20`." +
                        "\n- `LEARNING` tra `0.20` e `0.49`." +
                        "\n- `PRACTICED` tra `0.50` e `0.79`." +
                        "\n- `MASTERED` da `0.80` in su." +
                        "\n\n**Condizioni extra per `MASTERED`**" +
                        "\n\n- Almeno `3` tentativi totali." +
                        "\n- Almeno `2` risposte corrette." +
                        "\n- Evidenza minima di utilizzo: `1` tentativo in Gioco o `2` in Studio." +
                        "\n\nSolo le domande `MASTERED` contano come padroneggiate e fanno " +
                        "salire il progresso del pack.",
            ),
        statsUser =
            UserStatsStrings(
                title = "Le tue statistiche",
                subtitle = "Progresso globale tra studio e gioco.",
                bannerLine1 = "Guarda",
                bannerLine2 = "il tuo",
                bannerLine3 = "progresso",
                allFilter = "Tutto",
                last7DaysFilter = "Ultimi 7g",
                last30DaysFilter = "Ultimi 30g",
                answeredQuestions = "Domande risposte",
                totalTime = "Tempo totale",
                averageAnswerTime = "Media per domanda",
                packsProgress = "Progresso pack",
                modePerformance = "Rendimento per modalità",
                bestGameScore = "Miglior score",
                averageGameScore = "Score medio",
                masteredQuestions = "Domande padroneggiate",
                accuracyTrend = "Andamento accuracy",
                packBars = "Rendimento per pack",
                answerSplit = "Corrette vs errori",
                difficultyPerformance = "Per difficoltà",
                questionInsights = "Insight domande",
                fastestQuestion = "Domanda più rapida",
                mostFailedQuestion = "Domanda più sbagliata",
                noData = "Ancora nessun dato",
                packBarsDescription = "Precisione per sessione · avanzamento pack",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "Preferenze",
                profileSectionTitle = "Profilo",
                profileSectionSubtitle = "Il tuo nome e avatar",
                displayNameLabel = "Nome visualizzato",
                displayNameHint = "Il tuo nome",
                avatarLabel = "Avatar",
                avatarChoosePhoto = "Scegli foto",
                avatarDeletePhoto = "Elimina foto",
                appearanceSectionTitle = "Aspetto",
                appearanceSectionSubtitle = "Scegli il tema preferito",
                languageSectionTitle = "Lingua",
                languageSectionSubtitle = "Scegli la tua lingua preferita",
                langEnglish = "Inglese",
                langSpanish = "Spagnolo",
                langCatalan = "Catalano",
                langItalian = "Italiano",
                langJapanese = "Giapponese",
                langFrench = "Francese",
                langGerman = "Tedesco",
                reminderSectionTitle = "Promemoria giornaliero",
                reminderSectionSubtitle = "Ricevi una notifica per praticare ogni giorno",
                reminderEnabledLabel = "Attiva promemoria giornaliero",
                reminderDaysLabel = "Ripeti il",
                reminderTimeLabel = "Ora del promemoria",
                reminderTimePickerTitle = "Imposta l'orario",
                savePreferencesActionDescription = "Applica le modifiche alle preferenze.",
                discardPreferencesActionDescription = "Annulla le modifiche non salvate di questa schermata.",
                dayShortMon = "L",
                dayShortTue = "M",
                dayShortWed = "M",
                dayShortThu = "G",
                dayShortFri = "V",
                dayShortSat = "S",
                dayShortSun = "D",
                confirm = "Conferma",
            ),
        common =
            CommonStrings(
                back = "Indietro",
                cancel = "Annulla",
                create = "Crea",
                save = "Salva",
                edit = "Modifica",
                delete = "Elimina",
                toggleTheme = "Cambia tema",
                seeMore = "Vedi di più",
                seeLess = "Vedi meno",
                comingSoon = "Prossimamente",
                reorderLabel = "Riordina",
                filterLabel = "Filtra",
                actionsLabel = "Azioni",
                searchPlaceholder = "Cerca cartelle e pack...",
                nothingHereYet = "Niente qui ancora",
                noSearchResults = "Nessun risultato trovato",
                newFolder = "Nuova cartella",
                newPack = "Nuovo pack",
                questionsStatLabel = "Domande",
                accuracyStatLabel = "Accuratezza",
                sessionsStatLabel = "Sessioni",
                averageTimeStatLabel = "Tempo medio",
                progressLabel = "Progresso",
                pointsShortLabel = "pt",
                subfoldersLabel = { n -> if (n == 1) "1 sottocartella" else "$n sottocartelle" },
                packsLabel = { n -> if (n == 1) "1 pack" else "$n pack" },
                questionsLabel = { n -> if (n == 1) "1 domanda" else "$n domande" },
                difficultyEasy = "Facile",
                difficultyMedium = "Media",
                difficultyHard = "Difficile",
                difficultyExpert = "Esperto",
                studyMode = "Modalità studio",
                studyModeShort = "Studio",
                studyAction = "Studia",
                playMode = "Gioca",
                playModeShort = "Gioco",
                studyModeTitle = "Modalità studio",
                gameModeTitle = "Modalità gioco",
                gameSummaryTitle = "Riepilogo partita",
                studySummaryTitle = "Riepilogo studio",
                studyCorrectAnswersLabel = "Corrette",
                studyIncorrectAnswersLabel = "Errate",
                studyEffectiveTimeLabel = "Tempo effettivo",
                studyBackToPack = "Torna al pack",
                studyContinueStudy = "Continua a studiare",
                studyAverageDifficultyLabel = "Difficoltà media",
                studyVerifyAnswer = "Verifica risposta",
                previewLabel = "Anteprima",
                questionPreview = "Anteprima della domanda",
                cancelActionDescription = "Chiude questo editor senza salvare le modifiche.",
                saveActionDescription = "Salva le modifiche correnti a questa domanda.",
                deleteFolderKeyword = "cartella",
                deletePackKeyword = "pack",
                deleteKeywordHint = "Digita la parola richiesta",
                createFolderDescription = "Inserisci il nome della cartella da creare e seleziona il colore e l'icona.",
                editFolderDescription = "Aggiorna il nome della cartella, il colore e l'icona.",
                folderNameLabel = "Nome cartella",
                folderNameHint = "Nome cartella",
                folderColorLabel = "Colore",
                folderIconLabel = "Icona",
                editFolder = "Modifica cartella",
                deleteFolder = "Elimina cartella",
                deleteFolderConfirmMessage =
                    "Sei sicuro di voler eliminare questa cartella e tutto " +
                        "il suo contenuto? Questo include sottocartelle, pacchetti di domande " +
                        "e tutti i progressi collegati.",
                deleteFolderPrimaryMessage = "Sei sicuro di voler eliminare questa cartella?",
                deleteFolderSecondaryMessage =
                    "Questo include sottocartelle, pacchetti di " +
                        "domande e tutti i progressi collegati.",
                deleteFolderTypeKeywordInstruction = { keyword -> "Digita \"$keyword\" per abilitare l'eliminazione." },
                createFolderActionDescription = "Aggiunge una nuova cartella al livello corrente.",
                deleteFolderActionDescription = "Elimina questa cartella e tutto il contenuto annidato.",
                folderCreatedToast = "Cartella creata",
                folderDeletedToast = "Cartella eliminata",
                createPackDescription =
                    "Inserisci un nome per il pacchetto di domande che vuoi " +
                        "creare e seleziona il colore e l'icona.",
                editPackDescription = "Aggiorna il nome del pacchetto di domande, il colore e l'icona.",
                packTitleLabel = "Titolo del pack",
                packTitleHint = "Titolo del pack",
                packDescriptionLabel = "Descrizione",
                packDescriptionHint = "Aggiungi una descrizione facoltativa del pack",
                packColorLabel = "Colore",
                packIconLabel = "Icona",
                editPack = "Modifica pack",
                deletePack = "Elimina pack",
                deletePackConfirmMessage =
                    "Sei sicuro di voler eliminare questo pacchetto di " +
                        "domande e tutti i progressi collegati?",
                deletePackPrimaryMessage = "Sei sicuro di voler eliminare questo pack?",
                deletePackSecondaryMessage = "Questo include le sue domande e tutti i progressi collegati.",
                deletePackTypeKeywordInstruction = { keyword -> "Digita \"$keyword\" per abilitare l'eliminazione." },
                createPackActionDescription = "Aggiunge un nuovo pack nella cartella corrente.",
                packCreatedToast = "Mazzo creato",
                packDeletedToast = "Mazzo eliminato",
                importUQuizAction = "Importa .uquiz",
                exportUQuizAction = "Esporta .uquiz",
                importUQuizActionDescription = "Importa un file .uquiz in questa posizione.",
                exportUQuizActionDescription = "Esporta il contenuto attuale come file .uquiz.",
                importUQuizSuccess = { name -> "\"$name\" è stato importato correttamente." },
                exportUQuizSuccess = { name -> "\"$name\" è stato esportato correttamente." },
            ),
    )
}
