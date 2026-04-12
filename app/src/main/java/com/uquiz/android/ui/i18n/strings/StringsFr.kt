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

val FrErrors =
    AppErrors(
        folderNameTooLongMessage = "Le nom du dossier ne peut pas dépasser 100 caractères.",
        packTitleTooLongMessage = "Le titre du pack ne peut pas dépasser 100 caractères.",
        packDescriptionTooLongMessage = "La description du pack ne peut pas dépasser 200 caractères.",
        duplicateFolderNameMessage = { name -> "Un dossier nommé \"$name\" existe déjà à ce niveau." },
        duplicatePackTitleMessage = { title -> "Un pack nommé \"$title\" existe déjà dans ce dossier." },
        invalidImportExtensionMessage = "Sélectionnez un fichier .uquiz ou .uqz.",
        invalidUQuizFormatMessage = "Le fichier sélectionné n'est pas un document UQuiz valide.",
        missingRootFolderImportMessage =
            "Les imports depuis la bibliothèque doivent " +
                "inclure au moins un dossier racine.",
        invalidImportRootShapeMessage =
            "Le fichier importé doit contenir exactement un dossier " +
                "racine ou un pack racine.",
        blankImportedFolderNameMessage = "Les dossiers importés ne peuvent pas avoir un nom vide.",
        blankImportedPackTitleMessage = "Les packs importés ne peuvent pas avoir un titre vide.",
        emptyImportedPackMessage = "Les packs importés doivent contenir au moins une question.",
        emptyImportedQuestionTextMessage = { index -> "La question $index n'a pas de texte." },
        importedQuestionNeedsTwoOptionsMessage = { index -> "La question $index doit contenir au moins deux options." },
        importedQuestionNeedsSingleCorrectOptionMessage = { index ->
            "La question $index doit contenir " +
                "exactement une bonne réponse."
        },
        duplicateImportedFolderNamesMessage = "Les dossiers importés ne peuvent pas répéter des noms au même niveau.",
        duplicateImportedPackTitlesMessage = "Les packs importés ne peuvent pas répéter des titres au même niveau.",
        duplicateImportedOptionLabelsMessage = { index ->
            "La question $index contient des libellés d'option dupliqués."
        },
        importReadErrorMessage = "Le fichier sélectionné n'a pas pu être lu.",
        exportWriteErrorMessage = "Le fichier n'a pas pu être enregistré à l'emplacement sélectionné.",
        importFailedMessage = "L'import a échoué.",
        exportFailedMessage = "L'export a échoué.",
    )

val FrStrings: AppStrings by lazy {
    AppStrings(
        errors = FrErrors,
        nav =
            NavStrings(
                navHome = "Accueil",
                navLibrary = "Bibliothèque",
                navGame = "Jeu",
                navStats = "Statistiques",
                personalStats = "Statistiques personnelles",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "Bonjour, $name !" },
                homeReadyPrompt = "Prêt à te lancer un défi ?",
                homeDayStreakLabel = "Série quotidienne",
                homeScoreLabel = "Score",
                homeTotalXpLabel = "XP total",
                homeContinuePlaying = "Continuer à jouer",
                homeContinueStudying = "Continuer à étudier",
                homeRandomPlay = "Jouer au hasard",
                homeRandomStudy = "Étudier au hasard",
                homeNextLevelLabel = "Niveau suivant",
                homePointsToUnlockShort = { points -> "+$points pts pour débloquer" },
                homePointsToUnlockRank = { points, rank -> "+$points points pour débloquer $rank" },
                homeMaxRankUnlocked = "Rang maximal débloqué",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "Révisez avec",
                libraryBannerLine2 = "vos",
                libraryBannerLine3 = "fiches :)",
                myLibrary = "Ma bibliothèque",
                foldersSection = "Dossiers",
                recentPacksSection = "Packs récents",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "Sous-dossiers",
                packsInFolderSection = "Packs dans ce dossier",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "Pas encore de questions",
                newQuestion = "Nouvelle question",
                questionsSection = { n -> if (n == 1) "Question (1)" else "Questions ($n)" },
                filterQuestionsHint = "Filtrer les questions...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "Éditeur de questions",
                newQuestionTitle = "Nouvelle question",
                editQuestionTitle = "Modifier la question",
                dangerZoneLabel = "Zone dangereuse",
                questionMarkdownLabel = "Question",
                questionMarkdownHint = "Saisissez la question en Markdown",
                explanationMarkdownLabel = "Explication",
                explanationMarkdownHint = "Saisissez une explication facultative en Markdown",
                explanationPreview = "Aperçu de l'explication",
                optionsSectionTitle = "Options",
                addOption = "Ajouter une option",
                optionPlaceholder = { n -> "Option ${n + 1}" },
                correctOptionLabel = "Correcte",
                difficultySectionTitle = "Difficulté",
                deleteQuestionTitle = "Supprimer la question",
                deleteQuestionConfirmMessage =
                    "Voulez-vous vraiment supprimer cette question ? " +
                        "Cette action est irréversible.",
                deleteQuestionPrimaryMessage = "Voulez-vous vraiment supprimer cette question ?",
                deleteQuestionSecondaryMessage = "Cette action est irréversible.",
                deleteQuestionTypeKeywordInstruction = { keyword ->
                    "Saisissez \"$keyword\" pour activer la suppression."
                },
                deleteQuestionKeyword = "question",
                deleteQuestionActionDescription = "Supprime définitivement cette question.",
                createQuestionActionDescription = "Crée une nouvelle question dans ce pack.",
                questionCreatedToast = "Question créée",
                questionDeletedToast = "Question supprimée",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "Étudier le pack",
                studyReadyDescription =
                    "Révisez ce pack question par question et vérifiez chaque " +
                        "réponse pour voir immédiatement l'explication.",
                studyResumeStudy = "Reprendre l'étude",
                studyStartStudy = "Commencer l'étude",
                studyResumeProgress = { current, total -> "Progression enregistrée : $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "Question $current sur $total" },
                studyPrevious = "Précédent",
                studyNext = "Suivant",
                studyFinish = "Terminer",
                studyCorrectTitle = "Correct !",
                studyIncorrectTitle = "Incorrect",
                studyExplanationLabel = "Explication",
                studyExitStudy = "Quitter l'étude",
                studyExitTitle = "Quitter la session d'étude ?",
                studyExitMessage =
                    "Votre progression sera enregistrée pour pouvoir reprendre " +
                        "cette session plus tard.",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "Tous les packs",
                gameContinueButton = "Continuer",
                gameBannerLetsTest = "Mettons à l'épreuve",
                gameBannerYour = "ton",
                gameBannerBrain = "cerveau :)",
                gameRandomPlay = "Partie aléatoire",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "Commencer la partie",
                gameResumeGame = "Reprendre la partie",
                gameEstimatedTimeLabel = "Durée est.",
                gameAnsweredSoFar = { current, total -> "Vous avez répondu à $current sur $total" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "Score",
                gameTimeoutLabel = "Temps écoulé !",
                gameCorrectFeedback = { pts -> "+$pts pts" },
                gameIncorrectFeedback = { pts -> "−$pts pts" },
                gameExitTitle = "Quitter la partie ?",
                gameExitMessage = "Si vous quittez maintenant, votre progression sera perdue.",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "Partie terminée !",
                gameTotalScoreLabel = "Score total",
                gamePlayAgain = "Rejouer",
                gameRankUpTitle = "Rang supérieur !",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "Progression de rang",
                gameNextRankLabel = { mmr -> "$mmr MMR pour le prochain rang" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "Statistiques du pack",
                packLastSessionLabel = "Dernière session",
                packMostUsedModeLabel = "Mode le plus utilisé",
                packSeeFullStats = "Voir les détails complets",
                packGeneralSummary = "Résumé général",
                packByMode = "Par mode",
                packRecentActivity = "Activité récente",
                packSeeAll = "Voir tout",
                packBestPerformance = "Meilleure performance",
                packNoSessionsYet = "Aucune session pour l'instant",
                packNoGameSessionsYet = "Aucune session de jeu pour l'instant",
                packNoStudySessionsYet = "Aucune session d'étude pour l'instant",
                packQuestionsDominated = { current, total -> "$current / $total questions maîtrisées" },
                packThisWeekLabel = { n -> if (n <= 1) "1 session cette semaine" else "$n sessions cette semaine" },
                packStatsHelpAction = "Aide",
                packStatsHelpTitle = "Comment fonctionnent les statistiques",
                packStatsHelpIntro = "Ces métriques résument uniquement les sessions terminées de ce pack.",
                packStatsHelpQuestions =
                    "**Ce qu'elle mesure**" +
                        "\n\n- Combien de questions se trouvent actuellement dans le pack." +
                        "\n- Change si vous ajoutez ou supprimez des questions." +
                        "\n- Reflète le contenu actuel, pas le contenu historique des anciennes sessions.",
                packStatsHelpAccuracy =
                    "**Comment elle est calculée**" +
                        "\n\n- Seulement les sessions terminées comptent." +
                        "\n- Chaque session achevée apporte son taux de bonnes réponses." +
                        "\n- La carte affiche la moyenne de ces taux." +
                        "\n- Les sessions abandonnées ou en cours ne sont pas incluses.",
                packStatsHelpSessions =
                    "**Ce qui compte ici**" +
                        "\n\n- Les sessions terminées en **Étude** et en **Jeu**." +
                        "\n- Si vous avez ouvert une session et l'avez laissée inachevée, elle ne compte pas." +
                        "\n- Conçu pour mesurer une performance consolidée, pas une activité provisoire.",
                packStatsHelpProgress =
                    "**Comment elle augmente**" +
                        "\n\n- Calculée comme les questions maîtrisées divisées par les questions totales du pack." +
                        "\n- N'utilise pas la progression active d'une session en cours." +
                        "\n- Une question ne contribue que lorsqu'elle atteint `MASTERED`.",
                packStatsHelpAverageTime =
                    "**Ce qu'elle représente**" +
                        "\n\n- Durée moyenne des sessions terminées pour ce pack." +
                        "\n- Utile pour estimer combien de temps dure un parcours complet." +
                        "\n- Ne mesure pas le temps par question ni le temps de lecture dans une session ouverte.",
                packStatsHelpLastSession =
                    "**Ce qu'elle indique**" +
                        "\n\n- Quand la session terminée la plus récente s'est achevée." +
                        "\n- Les sessions ouvertes ou abandonnées ne mettent pas à jour cette valeur.",
                packStatsHelpMostUsedMode =
                    "**Comment il est décidé**" +
                        "\n\n- Compare les sessions **Étude** terminées avec les sessions **Jeu** terminées." +
                        "\n- Le mode avec le plus de sessions finies apparaît comme le plus utilisé.",
                packStatsHelpBestPerformance =
                    "**Ce qu'elle priorise**" +
                        "\n\n- S'il existe un historique **Jeu**, le meilleur score obtenu là est affiché." +
                        "\n- S'il n'y a pas encore de sessions Jeu terminées, la meilleure précision " +
                        "en **Étude** est utilisée." +
                        "\n- L'objectif est de mettre en avant le meilleur niveau de performance réelle.",
                packStatsHelpMasteryTitle = "Comment l'application décide qu'une question est apprise",
                packStatsHelpMastery =
                    "**Ce qu'utilise la formule**" +
                        "\n\n- Précision cumulée." +
                        "\n- Confiance selon le nombre d'essais." +
                        "\n- Un petit bonus si la question a déjà été vue en **Jeu**." +
                        "\n- Un bonus modéré lié à la série actuelle de bonnes réponses." +
                        "\n\n**Niveaux**\n\n- `NEW` si le score est inférieur à `0.20`." +
                        "\n- `LEARNING` entre `0.20` et `0.49`." +
                        "\n- `PRACTICED` entre `0.50` et `0.79`." +
                        "\n- `MASTERED` à partir de `0.80`." +
                        "\n\n**Conditions supplémentaires pour `MASTERED`**" +
                        "\n\n- Au moins `3` essais au total." +
                        "\n- Au moins `2` réponses correctes." +
                        "\n- Preuve minimale d'utilisation : `1` essai en Jeu ou `2` essais en Étude." +
                        "\n\nSeules les questions `MASTERED` comptent comme maîtrisées et font monter " +
                        "la progression du pack.",
            ),
        statsUser =
            UserStatsStrings(
                title = "Vos statistiques",
                subtitle = "Progression globale entre étude et jeu.",
                bannerLine1 = "Découvre",
                bannerLine2 = "ta",
                bannerLine3 = "progression",
                allFilter = "Tout",
                last7DaysFilter = "7 derniers j",
                last30DaysFilter = "30 derniers j",
                answeredQuestions = "Questions répondues",
                totalTime = "Temps total",
                averageAnswerTime = "Moy. par question",
                packsProgress = "Progression packs",
                modePerformance = "Performance par mode",
                bestGameScore = "Meilleur score",
                averageGameScore = "Score moyen",
                masteredQuestions = "Questions maîtrisées",
                accuracyTrend = "Évolution accuracy",
                packBars = "Performance par pack",
                answerSplit = "Bonnes vs erreurs",
                difficultyPerformance = "Par difficulté",
                questionInsights = "Insights questions",
                fastestQuestion = "Question la plus rapide",
                mostFailedQuestion = "Question la plus ratée",
                noData = "Pas encore de données",
                packBarsDescription = "Précision par session · avancement du pack",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "Préférences",
                profileSectionTitle = "Profil",
                profileSectionSubtitle = "Votre nom et avatar",
                displayNameLabel = "Nom affiché",
                displayNameHint = "Votre nom",
                avatarLabel = "Avatar",
                avatarChoosePhoto = "Choisir une photo",
                avatarDeletePhoto = "Supprimer la photo",
                appearanceSectionTitle = "Apparence",
                appearanceSectionSubtitle = "Choisissez votre thème préféré",
                languageSectionTitle = "Langue",
                languageSectionSubtitle = "Choisissez votre langue préférée",
                langEnglish = "Anglais",
                langSpanish = "Espagnol",
                langCatalan = "Catalan",
                langItalian = "Italien",
                langJapanese = "Japonais",
                langFrench = "Français",
                langGerman = "Allemand",
                reminderSectionTitle = "Rappel quotidien",
                reminderSectionSubtitle = "Recevez une notification pour pratiquer chaque jour",
                reminderEnabledLabel = "Activer le rappel quotidien",
                reminderDaysLabel = "Répéter le",
                reminderTimeLabel = "Heure du rappel",
                reminderTimePickerTitle = "Définir l'heure",
                savePreferencesActionDescription = "Appliquer les modifications effectuées dans les préférences.",
                discardPreferencesActionDescription = "Ignorer les modifications non enregistrées de cet écran.",
                dayShortMon = "L",
                dayShortTue = "M",
                dayShortWed = "M",
                dayShortThu = "J",
                dayShortFri = "V",
                dayShortSat = "S",
                dayShortSun = "D",
                confirm = "Confirmer",
            ),
        common =
            CommonStrings(
                back = "Retour",
                cancel = "Annuler",
                create = "Créer",
                save = "Enregistrer",
                edit = "Modifier",
                delete = "Supprimer",
                toggleTheme = "Changer le thème",
                seeMore = "Voir plus",
                seeLess = "Voir moins",
                comingSoon = "Bientôt disponible",
                reorderLabel = "Réorganiser",
                filterLabel = "Filtrer",
                actionsLabel = "Actions",
                searchPlaceholder = "Rechercher dossiers et packs...",
                nothingHereYet = "Rien ici pour l'instant",
                noSearchResults = "Aucun résultat trouvé",
                newFolder = "Nouveau dossier",
                newPack = "Nouveau pack",
                questionsStatLabel = "Questions",
                accuracyStatLabel = "Précision",
                sessionsStatLabel = "Sessions",
                averageTimeStatLabel = "Temps moyen",
                progressLabel = "Progression",
                pointsShortLabel = "pts",
                subfoldersLabel = { n -> if (n <= 1) "1 sous-dossier" else "$n sous-dossiers" },
                packsLabel = { n -> if (n <= 1) "1 pack" else "$n packs" },
                questionsLabel = { n -> if (n <= 1) "1 question" else "$n questions" },
                difficultyEasy = "Facile",
                difficultyMedium = "Moyen",
                difficultyHard = "Difficile",
                difficultyExpert = "Expert",
                studyMode = "Mode étude",
                studyModeShort = "Étude",
                studyAction = "Étudier",
                playMode = "Jouer",
                playModeShort = "Jeu",
                studyModeTitle = "Mode étude",
                gameModeTitle = "Mode jeu",
                gameSummaryTitle = "Résumé de la partie",
                studySummaryTitle = "Résumé de l'étude",
                studyCorrectAnswersLabel = "Correctes",
                studyIncorrectAnswersLabel = "Incorrectes",
                studyEffectiveTimeLabel = "Temps effectif",
                studyBackToPack = "Retour au pack",
                studyContinueStudy = "Continuer l'étude",
                studyAverageDifficultyLabel = "Difficulté moyenne",
                studyVerifyAnswer = "Vérifier la réponse",
                previewLabel = "Aperçu",
                questionPreview = "Aperçu de la question",
                cancelActionDescription = "Ferme cet éditeur sans enregistrer les modifications.",
                saveActionDescription = "Enregistre les modifications actuelles de cette question.",
                deleteFolderKeyword = "dossier",
                deletePackKeyword = "pack",
                deleteKeywordHint = "Saisissez le mot requis",
                createFolderDescription =
                    "Saisissez le nom du dossier à créer puis " +
                        "choisissez une couleur et une icône.",
                editFolderDescription = "Mettez à jour le nom du dossier, sa couleur et son icône.",
                folderNameLabel = "Nom du dossier",
                folderNameHint = "Nom du dossier",
                folderColorLabel = "Couleur",
                folderIconLabel = "Icône",
                editFolder = "Modifier le dossier",
                deleteFolder = "Supprimer le dossier",
                deleteFolderConfirmMessage =
                    "Voulez-vous vraiment supprimer ce dossier et tout son contenu ? " +
                        "Cela inclut les sous-dossiers, les packs de questions et toute la progression associée.",
                deleteFolderPrimaryMessage = "Voulez-vous vraiment supprimer ce dossier ?",
                deleteFolderSecondaryMessage =
                    "Cela inclut les sous-dossiers, les packs de questions et " +
                        "toute la progression associée.",
                deleteFolderTypeKeywordInstruction = { keyword ->
                    "Saisissez \"$keyword\" pour activer la suppression."
                },
                createFolderActionDescription = "Ajoute un nouveau dossier à ce niveau.",
                deleteFolderActionDescription = "Supprime ce dossier et tout son contenu imbriqué.",
                folderCreatedToast = "Dossier créé",
                folderDeletedToast = "Dossier supprimé",
                createPackDescription =
                    "Saisissez un nom pour le pack de questions que vous souhaitez " +
                        "créer puis choisissez une couleur et une icône.",
                editPackDescription = "Mettez à jour le nom du pack de questions, la couleur et l'icône.",
                packTitleLabel = "Titre du pack",
                packTitleHint = "Titre du pack",
                packDescriptionLabel = "Description",
                packDescriptionHint = "Ajoutez une description facultative du pack",
                packColorLabel = "Couleur",
                packIconLabel = "Icône",
                editPack = "Modifier le pack",
                deletePack = "Supprimer le pack",
                deletePackConfirmMessage =
                    "Voulez-vous vraiment supprimer ce pack de questions et " +
                        "toute la progression associée ?",
                deletePackPrimaryMessage = "Voulez-vous vraiment supprimer ce pack ?",
                deletePackSecondaryMessage = "Cela inclut ses questions et toute la progression associée.",
                deletePackTypeKeywordInstruction = { keyword -> "Saisissez \"$keyword\" pour activer la suppression." },
                createPackActionDescription = "Ajoute un nouveau pack dans le dossier actuel.",
                packCreatedToast = "Paquet créé",
                packDeletedToast = "Paquet supprimé",
                importUQuizAction = "Importer .uquiz",
                exportUQuizAction = "Exporter .uquiz",
                importUQuizActionDescription = "Importa un fichier .uquiz à cet emplacement.",
                exportUQuizActionDescription = "Exporte le contenu actuel en fichier .uquiz.",
                importUQuizSuccess = { name -> "\"$name\" a été importé avec succès." },
                exportUQuizSuccess = { name -> "\"$name\" a été exporté avec succès." },
            ),
    )
}
