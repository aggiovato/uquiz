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

val DeErrors =
    AppErrors(
        folderNameTooLongMessage = "Der Ordnername darf nicht länger als 100 Zeichen sein.",
        packTitleTooLongMessage = "Der Pack-Titel darf nicht länger als 100 Zeichen sein.",
        packDescriptionTooLongMessage = "Die Pack-Beschreibung darf nicht länger als 200 Zeichen sein.",
        duplicateFolderNameMessage = { name -> "Auf dieser Ebene existiert bereits ein Ordner namens \"$name\"." },
        duplicatePackTitleMessage = { title -> "In diesem Ordner existiert bereits ein Pack namens \"$title\"." },
        invalidImportExtensionMessage = "Wählen Sie eine .uquiz- oder .uqz-Datei aus.",
        invalidUQuizFormatMessage = "Die ausgewählte Datei ist kein gültiges UQuiz-Dokument.",
        missingRootFolderImportMessage =
            "Importe aus der Bibliothek müssen mindestens einen " +
                "Stammordner enthalten.",
        invalidImportRootShapeMessage =
            "Die importierte Datei muss genau einen Stammordner oder ein " +
                "Stamm-Pack enthalten.",
        blankImportedFolderNameMessage = "Importierte Ordner dürfen keinen leeren Namen haben.",
        blankImportedPackTitleMessage = "Importierte Packs dürfen keinen leeren Titel haben.",
        emptyImportedPackMessage = "Importierte Packs müssen mindestens eine Frage enthalten.",
        emptyImportedQuestionTextMessage = { index -> "Frage $index hat keinen Text." },
        importedQuestionNeedsTwoOptionsMessage = { index -> "Frage $index muss mindestens zwei Optionen enthalten." },
        importedQuestionNeedsSingleCorrectOptionMessage = { index ->
            "Frage $index muss genau eine " +
                "richtige Option enthalten."
        },
        duplicateImportedFolderNamesMessage =
            "Importierte Ordner dürfen auf derselben Ebene keine doppelten " +
                "Namen haben.",
        duplicateImportedPackTitlesMessage =
            "Importierte Packs dürfen auf derselben Ebene keine doppelten " +
                "Titel haben.",
        duplicateImportedOptionLabelsMessage = { index ->
            "Frage $index enthält doppelte Optionsbezeichnungen."
        },
        importReadErrorMessage = "Die ausgewählte Datei konnte nicht gelesen werden.",
        exportWriteErrorMessage = "Die Datei konnte nicht am gewählten Ort gespeichert werden.",
        importFailedMessage = "Import fehlgeschlagen.",
        exportFailedMessage = "Export fehlgeschlagen.",
    )

val DeStrings: AppStrings by lazy {
    AppStrings(
        errors = DeErrors,
        nav =
            NavStrings(
                navHome = "Startseite",
                navLibrary = "Bibliothek",
                navGame = "Spiel",
                navStats = "Statistiken",
                personalStats = "Persönliche Statistiken",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "Hallo, $name!" },
                homeReadyPrompt = "Bereit für die nächste Herausforderung?",
                homeDayStreakLabel = "Tagesserie",
                homeScoreLabel = "Score",
                homeTotalXpLabel = "Gesamt-XP",
                homeContinuePlaying = "Weiterspielen",
                homeContinueStudying = "Weiterlernen",
                homeRandomPlay = "Zufällig spielen",
                homeRandomStudy = "Zufällig lernen",
                homeNextLevelLabel = "Nächstes Level",
                homePointsToUnlockShort = { points -> "+$points Pkt bis" },
                homePointsToUnlockRank = { points, rank -> "+$points Punkte bis $rank" },
                homeMaxRankUnlocked = "Maximaler Rang freigeschaltet",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "Lerne mit",
                libraryBannerLine2 = "deinen",
                libraryBannerLine3 = "Karten :)",
                myLibrary = "Meine Bibliothek",
                foldersSection = "Ordner",
                recentPacksSection = "Neueste Packs",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "Unterordner",
                packsInFolderSection = "Packs in diesem Ordner",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "Noch keine Fragen",
                newQuestion = "Neue Frage",
                questionsSection = { n -> if (n == 1) "Frage (1)" else "Fragen ($n)" },
                filterQuestionsHint = "Fragen filtern...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "Frageneditor",
                newQuestionTitle = "Neue Frage",
                editQuestionTitle = "Frage bearbeiten",
                dangerZoneLabel = "Gefahrenzone",
                questionMarkdownLabel = "Frage",
                questionMarkdownHint = "Schreibe die Frage in Markdown",
                explanationMarkdownLabel = "Erklärung",
                explanationMarkdownHint = "Schreibe optional eine Erklärung in Markdown",
                explanationPreview = "Erklärungsvorschau",
                optionsSectionTitle = "Optionen",
                addOption = "Option hinzufügen",
                optionPlaceholder = { n -> "Option ${n + 1}" },
                correctOptionLabel = "Richtig",
                difficultySectionTitle = "Schwierigkeit",
                deleteQuestionTitle = "Frage löschen",
                deleteQuestionConfirmMessage =
                    "Möchtest du diese Frage wirklich löschen? " +
                        "Diese Aktion kann nicht rückgängig gemacht werden.",
                deleteQuestionPrimaryMessage = "Möchtest du diese Frage wirklich löschen?",
                deleteQuestionSecondaryMessage = "Diese Aktion kann nicht rückgängig gemacht werden.",
                deleteQuestionTypeKeywordInstruction = { keyword ->
                    "Gib \"$keyword\" ein, um das Löschen zu aktivieren."
                },
                deleteQuestionKeyword = "Frage",
                deleteQuestionActionDescription = "Löscht diese Frage dauerhaft.",
                createQuestionActionDescription = "Erstellt eine neue Frage in diesem Pack.",
                questionCreatedToast = "Frage erstellt",
                questionDeletedToast = "Frage gelöscht",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "Pack lernen",
                studyReadyDescription =
                    "Gehe dieses Pack Frage für Frage durch und prüfe jede Antwort, " +
                        "um die Erklärung sofort zu sehen.",
                studyResumeStudy = "Lernen fortsetzen",
                studyStartStudy = "Lernen starten",
                studyResumeProgress = { current, total -> "Gespeicherter Fortschritt: $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "Frage $current von $total" },
                studyPrevious = "Zurück",
                studyNext = "Weiter",
                studyFinish = "Beenden",
                studyCorrectTitle = "Richtig!",
                studyIncorrectTitle = "Falsch",
                studyExplanationLabel = "Erklärung",
                studyExitStudy = "Lernen verlassen",
                studyExitTitle = "Lernsitzung verlassen?",
                studyExitMessage =
                    "Dein Fortschritt wird gespeichert, damit du diese Sitzung später " +
                        "fortsetzen kannst.",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "Alle Packs",
                gameContinueButton = "Fortsetzen",
                gameBannerLetsTest = "Testen wir",
                gameBannerYour = "dein",
                gameBannerBrain = "Gehirn :)",
                gameRandomPlay = "Zufälliges Spiel",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "Spiel starten",
                gameResumeGame = "Spiel fortsetzen",
                gameEstimatedTimeLabel = "Gesch. Zeit",
                gameAnsweredSoFar = { current, total -> "$current von $total beantwortet" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "Punkte",
                gameTimeoutLabel = "Zeit abgelaufen!",
                gameCorrectFeedback = { pts -> "+$pts Pkt." },
                gameIncorrectFeedback = { pts -> "−$pts Pkt." },
                gameExitTitle = "Spiel verlassen?",
                gameExitMessage = "Wenn du jetzt gehst, geht dein Spielfortschritt verloren.",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "Spiel beendet!",
                gameTotalScoreLabel = "Gesamtpunktzahl",
                gamePlayAgain = "Nochmal spielen",
                gameRankUpTitle = "Aufgestiegen!",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "Rangfortschritt",
                gameNextRankLabel = { mmr -> "$mmr MMR zum nächsten Rang" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "Pack-Statistiken",
                packLastSessionLabel = "Letzte Sitzung",
                packMostUsedModeLabel = "Meistgenutzter Modus",
                packSeeFullStats = "Vollständige Details anzeigen",
                packGeneralSummary = "Allgemeine Übersicht",
                packByMode = "Nach Modus",
                packRecentActivity = "Letzte Aktivität",
                packSeeAll = "Alle anzeigen",
                packBestPerformance = "Beste Leistung",
                packNoSessionsYet = "Noch keine Sitzungen",
                packNoGameSessionsYet = "Noch keine Spiel-Sitzungen",
                packNoStudySessionsYet = "Noch keine Lern-Sitzungen",
                packQuestionsDominated = { current, total -> "$current / $total gemeisterte Fragen" },
                packThisWeekLabel = { n -> if (n == 1) "1 Sitzung diese Woche" else "$n Sitzungen diese Woche" },
                packStatsHelpAction = "Hilfe",
                packStatsHelpTitle = "So funktionieren die Statistiken",
                packStatsHelpIntro = "Diese Kennzahlen fassen nur abgeschlossene Sitzungen dieses Packs zusammen.",
                packStatsHelpQuestions =
                    "**Was sie misst**" +
                        "\n\n- Wie viele Fragen das Pack aktuell enthält." +
                        "\n- Ändert sich, wenn Fragen hinzugefügt oder gelöscht werden." +
                        "\n- Zeigt den aktuellen Inhalt, nicht den historischen Inhalt alter Sitzungen.",
                packStatsHelpAccuracy =
                    "**Wie sie berechnet wird**" +
                        "\n\n- Nur abgeschlossene Sitzungen zählen." +
                        "\n- Jede beendete Sitzung liefert ihre Quote korrekter Antworten." +
                        "\n- Die Karte zeigt den Durchschnitt dieser Quoten." +
                        "\n- Abgebrochene oder laufende Sitzungen sind ausgeschlossen.",
                packStatsHelpSessions =
                    "**Was hier zählt**" +
                        "\n\n- Abgeschlossene Sitzungen in **Lernen** und **Spielen**." +
                        "\n- Eine geöffnete, aber nicht abgeschlossene Sitzung zählt nicht." +
                        "\n- Soll konsolidierte Leistung messen, keine vorläufige Aktivität.",
                packStatsHelpProgress =
                    "**Wie er steigt**" +
                        "\n\n- Berechnet als beherrschte Fragen geteilt durch Gesamtfragen des Packs." +
                        "\n- Nutzt nicht den aktiven Fortschritt einer laufenden Sitzung." +
                        "\n- Eine Frage trägt erst bei, wenn sie `MASTERED` erreicht.",
                packStatsHelpAverageTime =
                    "**Was sie darstellt**" +
                        "\n\n- Durchschnittliche Dauer der abgeschlossenen Sitzungen dieses Packs." +
                        "\n- Hilft einzuschätzen, wie lange ein kompletter Durchlauf dauert." +
                        "\n- Misst nicht die Zeit pro Frage oder Lesezeit in einer offenen Sitzung.",
                packStatsHelpLastSession =
                    "**Was sie zeigt**" +
                        "\n\n- Wann die letzte abgeschlossene Sitzung endete." +
                        "\n- Geöffnete oder abgebrochene Sitzungen aktualisieren diesen Wert nicht.",
                packStatsHelpMostUsedMode =
                    "**Wie er bestimmt wird**" +
                        "\n\n- Vergleicht abgeschlossene **Lern**-Sitzungen mit abgeschlossenen **Spiel**-Sitzungen." +
                        "\n- Der Modus mit mehr abgeschlossenen Sitzungen gilt als meistgenutzter.",
                packStatsHelpBestPerformance =
                    "**Was priorisiert wird**" +
                        "\n\n- Gibt es abgeschlossene **Spiel**-Historie, wird der beste Spiel-Score angezeigt." +
                        "\n- Falls noch keine Spiel-Sitzungen abgeschlossen wurden, wird die beste " +
                        "**Lern**-Trefferquote verwendet." +
                        "\n- Ziel ist, den höchsten echten Leistungspunkt hervorzuheben.",
                packStatsHelpMasteryTitle = "Wie die App entscheidet, dass eine Frage gelernt ist",
                packStatsHelpMastery =
                    "**Was die Formel verwendet**" +
                        "\n\n- Kumulative Genauigkeit." +
                        "\n- Vertrauen durch die Anzahl der Versuche." +
                        "\n- Kleiner Bonus, wenn die Frage bereits im **Spielen** auftauchte." +
                        "\n- Moderater Bonus für die aktuelle Serie richtiger Antworten." +
                        "\n\n**Stufen**\n\n- `NEW` wenn Score unter `0.20`." +
                        "\n- `LEARNING` zwischen `0.20` und `0.49`." +
                        "\n- `PRACTICED` zwischen `0.50` und `0.79`." +
                        "\n- `MASTERED` ab `0.80`." +
                        "\n\n**Zusatzbedingungen für `MASTERED`**" +
                        "\n\n- Mindestens `3` Gesamtversuche." +
                        "\n- Mindestens `2` korrekte Antworten." +
                        "\n- Mindestnachweis: `1` Spiel-Versuch oder `2` Lern-Versuche." +
                        "\n\nNur `MASTERED`-Fragen gelten als beherrscht und erhöhen den Pack-Fortschritt.",
            ),
        statsUser =
            UserStatsStrings(
                title = "Deine Statistiken",
                subtitle = "Globaler Fortschritt in Lernen und Spiel.",
                bannerLine1 = "Entdecke",
                bannerLine2 = "deinen",
                bannerLine3 = "Fortschritt",
                allFilter = "Alle",
                last7DaysFilter = "Letzte 7 T.",
                last30DaysFilter = "Letzte 30 T.",
                answeredQuestions = "Beantwortete Fragen",
                totalTime = "Gesamtzeit",
                averageAnswerTime = "Ø pro Frage",
                packsProgress = "Pack-Fortschritt",
                modePerformance = "Leistung nach Modus",
                bestGameScore = "Bester Score",
                averageGameScore = "Ø Game-Score",
                masteredQuestions = "Gemeisterte Fragen",
                accuracyTrend = "Genauigkeitstrend",
                packBars = "Leistung nach Pack",
                answerSplit = "Richtig vs Fehler",
                difficultyPerformance = "Nach Schwierigkeit",
                questionInsights = "Fragen-Insights",
                fastestQuestion = "Schnellste Frage",
                mostFailedQuestion = "Häufigster Fehler",
                noData = "Noch keine Daten",
                packBarsDescription = "Genauigkeit pro Sitzung · Pack-Fortschritt",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "Einstellungen",
                profileSectionTitle = "Profil",
                profileSectionSubtitle = "Dein Anzeigename und Avatar",
                displayNameLabel = "Anzeigename",
                displayNameHint = "Dein Name",
                avatarLabel = "Avatar",
                avatarChoosePhoto = "Foto auswählen",
                avatarDeletePhoto = "Foto löschen",
                appearanceSectionTitle = "Darstellung",
                appearanceSectionSubtitle = "Wähle dein bevorzugtes Design",
                languageSectionTitle = "Sprache",
                languageSectionSubtitle = "Wähle deine bevorzugte Sprache",
                langEnglish = "Englisch",
                langSpanish = "Spanisch",
                langCatalan = "Katalanisch",
                langItalian = "Italienisch",
                langJapanese = "Japanisch",
                langFrench = "Französisch",
                langGerman = "Deutsch",
                reminderSectionTitle = "Tägliche Erinnerung",
                reminderSectionSubtitle = "Erhalte täglich eine Benachrichtigung zum Üben",
                reminderEnabledLabel = "Tägliche Erinnerung aktivieren",
                reminderDaysLabel = "Wiederholen am",
                reminderTimeLabel = "Erinnerungszeit",
                reminderTimePickerTitle = "Uhrzeit festlegen",
                savePreferencesActionDescription = "Die in den Einstellungen vorgenommenen Änderungen übernehmen.",
                discardPreferencesActionDescription =
                    "Die nicht gespeicherten Änderungen auf diesem " +
                        "Bildschirm verwerfen.",
                dayShortMon = "Mo",
                dayShortTue = "Di",
                dayShortWed = "Mi",
                dayShortThu = "Do",
                dayShortFri = "Fr",
                dayShortSat = "Sa",
                dayShortSun = "So",
                confirm = "Bestätigen",
            ),
        common =
            CommonStrings(
                back = "Zurück",
                cancel = "Abbrechen",
                create = "Erstellen",
                save = "Speichern",
                edit = "Bearbeiten",
                delete = "Löschen",
                toggleTheme = "Design wechseln",
                seeMore = "Mehr sehen",
                seeLess = "Weniger sehen",
                comingSoon = "Kommt bald",
                reorderLabel = "Neu anordnen",
                filterLabel = "Filtern",
                actionsLabel = "Aktionen",
                searchPlaceholder = "Ordner & Packs suchen...",
                nothingHereYet = "Noch nichts hier",
                noSearchResults = "Keine Ergebnisse gefunden",
                newFolder = "Neuer Ordner",
                newPack = "Neues Pack",
                questionsStatLabel = "Fragen",
                accuracyStatLabel = "Trefferquote",
                sessionsStatLabel = "Sitzungen",
                averageTimeStatLabel = "Durchschnittszeit",
                progressLabel = "Fortschritt",
                pointsShortLabel = "Pkt.",
                subfoldersLabel = { n -> if (n == 1) "1 Unterordner" else "$n Unterordner" },
                packsLabel = { n -> if (n == 1) "1 Pack" else "$n Packs" },
                questionsLabel = { n -> if (n == 1) "1 Frage" else "$n Fragen" },
                difficultyEasy = "Leicht",
                difficultyMedium = "Mittel",
                difficultyHard = "Schwer",
                difficultyExpert = "Experte",
                studyMode = "Lernmodus",
                studyModeShort = "Lernen",
                studyAction = "Lernen",
                playMode = "Spielen",
                playModeShort = "Spiel",
                studyModeTitle = "Lernmodus",
                gameModeTitle = "Spielmodus",
                gameSummaryTitle = "Spielübersicht",
                studySummaryTitle = "Lernübersicht",
                studyCorrectAnswersLabel = "Richtig",
                studyIncorrectAnswersLabel = "Falsch",
                studyEffectiveTimeLabel = "Effektive Zeit",
                studyBackToPack = "Zurück zum Pack",
                studyContinueStudy = "Weiterlernen",
                studyAverageDifficultyLabel = "Durchschnittliche Schwierigkeit",
                studyVerifyAnswer = "Antwort prüfen",
                previewLabel = "Vorschau",
                questionPreview = "Fragenvorschau",
                cancelActionDescription = "Schließt diesen Editor ohne die Änderungen zu speichern.",
                saveActionDescription = "Speichert die aktuellen Änderungen an dieser Frage.",
                deleteFolderKeyword = "Ordner",
                deletePackKeyword = "Pack",
                deleteKeywordHint = "Erforderliches Wort eingeben",
                createFolderDescription =
                    "Geben Sie den Namen des Ordners ein und wählen Sie eine " +
                        "Farbe und ein Symbol aus.",
                editFolderDescription = "Aktualisieren Sie den Ordnernamen, die Farbe und das Symbol.",
                folderNameLabel = "Ordnername",
                folderNameHint = "Ordnername",
                folderColorLabel = "Farbe",
                folderIconLabel = "Symbol",
                editFolder = "Ordner bearbeiten",
                deleteFolder = "Ordner löschen",
                deleteFolderConfirmMessage =
                    "Möchten Sie diesen Ordner und seinen gesamten Inhalt wirklich löschen? " +
                        "Dazu gehören Unterordner, Fragenpakete und der gesamte zugehörige Fortschritt.",
                deleteFolderPrimaryMessage = "Möchten Sie diesen Ordner wirklich löschen?",
                deleteFolderSecondaryMessage =
                    "Dazu gehören Unterordner, Fragenpakete und der gesamte zugehörige " +
                        "Fortschritt.",
                deleteFolderTypeKeywordInstruction = { keyword ->
                    "Gib \"$keyword\" ein, um das Löschen zu aktivieren."
                },
                createFolderActionDescription = "Erstellt einen neuen Ordner auf der aktuellen Ebene.",
                deleteFolderActionDescription = "Löscht diesen Ordner und alle darin enthaltenen Inhalte.",
                folderCreatedToast = "Ordner erstellt",
                folderDeletedToast = "Ordner gelöscht",
                createPackDescription =
                    "Geben Sie einen Namen für das Fragenpaket ein, das Sie erstellen möchten, " +
                        "und wählen Sie eine Farbe und ein Symbol aus.",
                editPackDescription = "Aktualisieren Sie den Namen des Fragenpakets, die Farbe und das Symbol.",
                packTitleLabel = "Pack-Titel",
                packTitleHint = "Pack-Titel",
                packDescriptionLabel = "Beschreibung",
                packDescriptionHint = "Optionale Pack-Beschreibung hinzufügen",
                packColorLabel = "Farbe",
                packIconLabel = "Symbol",
                editPack = "Pack bearbeiten",
                deletePack = "Pack löschen",
                deletePackConfirmMessage =
                    "Möchten Sie dieses Fragenpaket und den gesamten zugehörigen " +
                        "Fortschritt wirklich löschen?",
                deletePackPrimaryMessage = "Möchten Sie dieses Pack wirklich löschen?",
                deletePackSecondaryMessage = "Dazu gehören seine Fragen und der gesamte zugehörige Fortschritt.",
                deletePackTypeKeywordInstruction = { keyword -> "Gib \"$keyword\" ein, um das Löschen zu aktivieren." },
                createPackActionDescription = "Erstellt ein neues Pack im aktuellen Ordner.",
                packCreatedToast = "Paket erstellt",
                packDeletedToast = "Paket gelöscht",
                importUQuizAction = "Import .uquiz",
                exportUQuizAction = "Export .uquiz",
                importUQuizActionDescription = "Importiert eine .uquiz-Datei an diesen Ort.",
                exportUQuizActionDescription = "Exportiert den aktuellen Inhalt als .uquiz-Datei.",
                importUQuizSuccess = { name -> "\"$name\" wurde erfolgreich importiert." },
                exportUQuizSuccess = { name -> "\"$name\" wurde erfolgreich exportiert." },
            ),
    )
}
