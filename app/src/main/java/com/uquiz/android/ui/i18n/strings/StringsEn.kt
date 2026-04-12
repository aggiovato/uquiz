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

val EnErrors =
    AppErrors(
        folderNameTooLongMessage = "Folder name cannot exceed 100 characters.",
        packTitleTooLongMessage = "Pack title cannot exceed 100 characters.",
        packDescriptionTooLongMessage = "Pack description cannot exceed 200 characters.",
        invalidContentColorMessage = "Select a valid color from the available palette.",
        invalidContentIconMessage = "Select a valid icon from the available icon set.",
        duplicateFolderNameMessage = { name -> "A folder named \"$name\" already exists at this level." },
        duplicatePackTitleMessage = { title -> "A pack titled \"$title\" already exists in this folder." },
        invalidImportExtensionMessage = "Select a .uquiz or .uqz file.",
        invalidUQuizFormatMessage = "The selected file is not a valid UQuiz document.",
        missingRootFolderImportMessage = "Imports from Library must include at least one root folder.",
        invalidImportRootShapeMessage = "The imported file must contain exactly one root folder or one root pack.",
        blankImportedFolderNameMessage = "Imported folders cannot have an empty name.",
        blankImportedPackTitleMessage = "Imported packs cannot have an empty title.",
        emptyImportedPackMessage = "Imported packs must include at least one question.",
        emptyImportedQuestionTextMessage = { index -> "Question $index has no text." },
        importedQuestionNeedsTwoOptionsMessage = { index -> "Question $index must include at least two options." },
        importedQuestionNeedsSingleCorrectOptionMessage = { index ->
            "Question $index must " +
                "include exactly one correct option."
        },
        duplicateImportedFolderNamesMessage = "Imported folders cannot repeat names at the same level.",
        duplicateImportedPackTitlesMessage = "Imported packs cannot repeat titles at the same level.",
        duplicateImportedOptionLabelsMessage = { index -> "Question $index contains duplicate option labels." },
        importReadErrorMessage = "The selected file could not be read.",
        exportWriteErrorMessage = "The file could not be written to the selected location.",
        importFailedMessage = "Import failed.",
        exportFailedMessage = "Export failed.",
    )

val EnStrings: AppStrings by lazy {
    AppStrings(
        errors = EnErrors,
        nav =
            NavStrings(
                navHome = "Home",
                navLibrary = "Library",
                navGame = "Game",
                navStats = "Stats",
                personalStats = "Personal stats",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "Hello, $name!" },
                homeReadyPrompt = "Ready to challenge yourself?",
                homeDayStreakLabel = "Day streak",
                homeScoreLabel = "Score",
                homeTotalXpLabel = "Total XP",
                homeContinuePlaying = "Continue playing",
                homeContinueStudying = "Continue studying",
                homeRandomPlay = "Random Play",
                homeRandomStudy = "Random Study",
                homeNextLevelLabel = "Next level",
                homePointsToUnlockShort = { points -> "+$points pts to unlock" },
                homePointsToUnlockRank = { points, rank -> "+$points points to unlock $rank" },
                homeMaxRankUnlocked = "Max rank unlocked",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "Flashcard",
                libraryBannerLine2 = "your",
                libraryBannerLine3 = "study :)",
                myLibrary = "My library",
                foldersSection = "Folders",
                recentPacksSection = "Recent packs",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "Sub-folders",
                packsInFolderSection = "Packs in this folder",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "No questions yet",
                newQuestion = "New question",
                questionsSection = { n -> if (n == 1) "Question (1)" else "Questions ($n)" },
                filterQuestionsHint = "Filter questions...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "Question editor",
                newQuestionTitle = "New question",
                editQuestionTitle = "Edit question",
                dangerZoneLabel = "Danger zone",
                questionMarkdownLabel = "Question",
                questionMarkdownHint = "Write the question in Markdown",
                explanationMarkdownLabel = "Explanation",
                explanationMarkdownHint = "Write an optional explanation in Markdown",
                explanationPreview = "Explanation preview",
                optionsSectionTitle = "Options",
                addOption = "Add option",
                optionPlaceholder = { n -> "Option ${n + 1}" },
                correctOptionLabel = "Correct",
                difficultySectionTitle = "Difficulty",
                deleteQuestionTitle = "Delete question",
                deleteQuestionConfirmMessage =
                    "Are you sure you want to delete this question? " +
                        "This action cannot be undone.",
                deleteQuestionPrimaryMessage = "Are you sure you want to delete this question?",
                deleteQuestionSecondaryMessage = "This action cannot be undone.",
                deleteQuestionTypeKeywordInstruction = { keyword -> "Type \"$keyword\" to enable deletion." },
                deleteQuestionKeyword = "question",
                deleteQuestionActionDescription = "Delete this question permanently.",
                createQuestionActionDescription = "Create a new question in this pack.",
                questionCreatedToast = "Question created",
                questionDeletedToast = "Question deleted",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "Study pack",
                studyReadyDescription =
                    "Review this pack question by question and verify " +
                        "each answer to see the explanation immediately.",
                studyResumeStudy = "Resume study",
                studyStartStudy = "Start study",
                studyResumeProgress = { current, total -> "Progress saved: $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "Question $current of $total" },
                studyPrevious = "Previous",
                studyNext = "Next",
                studyFinish = "Finish",
                studyCorrectTitle = "Correct!",
                studyIncorrectTitle = "Incorrect",
                studyExplanationLabel = "Explanation",
                studyExitStudy = "Exit study",
                studyExitTitle = "Leave study session?",
                studyExitMessage =
                    "Your progress will be saved so you can " +
                        "continue this study session later.",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "All Packs",
                gameContinueButton = "Continue",
                gameBannerLetsTest = "Let's test",
                gameBannerYour = "your",
                gameBannerBrain = "brain :)",
                gameRandomPlay = "Random play",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "Start game",
                gameResumeGame = "Resume Game",
                gameEstimatedTimeLabel = "Est. Time",
                gameAnsweredSoFar = { current, total -> "You answered $current of $total" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "Score",
                gameTimeoutLabel = "Time's up!",
                gameCorrectFeedback = { pts -> "+$pts pts" },
                gameIncorrectFeedback = { pts -> "−$pts pts" },
                gameExitTitle = "Leave game?",
                gameExitMessage = "If you leave now, your game progress will be lost.",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "Game Complete!",
                gameTotalScoreLabel = "Total Score",
                gamePlayAgain = "Play again",
                gameRankUpTitle = "Rank Up!",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "Rank Progress",
                gameNextRankLabel = { mmr -> "$mmr MMR to next rank" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "Pack stats",
                packLastSessionLabel = "Last session",
                packMostUsedModeLabel = "Most used mode",
                packSeeFullStats = "See full stats",
                packGeneralSummary = "General summary",
                packByMode = "By mode",
                packRecentActivity = "Recent activity",
                packSeeAll = "See all",
                packBestPerformance = "Best performance",
                packNoSessionsYet = "No sessions yet",
                packNoGameSessionsYet = "No game sessions yet",
                packNoStudySessionsYet = "No study sessions yet",
                packQuestionsDominated = { current, total -> "$current / $total questions mastered" },
                packThisWeekLabel = { n -> if (n == 1) "1 session this week" else "$n sessions this week" },
                packStatsHelpAction = "Help",
                packStatsHelpTitle = "How pack stats work",
                packStatsHelpIntro = "These metrics summarize only completed sessions for this pack.",
                packStatsHelpQuestions =
                    "**What it measures**" +
                        "\n\n- How many questions the pack currently has." +
                        "\n- Changes when you add or remove questions." +
                        "\n- Reflects current content, not the historical content used in older sessions.",
                packStatsHelpAccuracy =
                    "**How it's calculated**" +
                        "\n\n- Only completed sessions count." +
                        "\n- Each finished session contributes its correct-answer ratio." +
                        "\n- The card shows the average of those ratios." +
                        "\n- Abandoned sessions and in-progress runs are excluded.",
                packStatsHelpSessions =
                    "**What counts here**" +
                        "\n\n- Finished sessions in both **Study** and **Play**." +
                        "\n- If you opened a session but left it unfinished, it doesn't count." +
                        "\n- Designed to measure consolidated performance, not provisional activity.",
                packStatsHelpProgress =
                    "**How it grows**" +
                        "\n\n- Calculated as mastered questions divided by the total questions in the pack." +
                        "\n- Does not use active session progress." +
                        "\n- A question only contributes once it reaches `MASTERED`.",
                packStatsHelpAverageTime =
                    "**What it represents**" +
                        "\n\n- Mean duration of the pack's completed sessions." +
                        "\n- Useful for estimating how long a full run takes." +
                        "\n- Does not measure per-question time or reading time inside an open session.",
                packStatsHelpLastSession =
                    "**What it shows**" +
                        "\n\n- When the most recent completed session ended." +
                        "\n- Opened or abandoned sessions do not update this value.",
                packStatsHelpMostUsedMode =
                    "**How it's decided**" +
                        "\n\n- Compares completed **Study** sessions with completed **Play** sessions." +
                        "\n- The mode with more finished sessions appears as the most used.",
                packStatsHelpBestPerformance =
                    "**What it prioritizes**" +
                        "\n\n- If there is **Play** history, the best game score is shown." +
                        "\n- If no Play sessions are completed yet, the best **Study** accuracy is used instead." +
                        "\n- The goal is to highlight the highest point of real performance.",
                packStatsHelpMasteryTitle = "How the app decides a question is learned",
                packStatsHelpMastery =
                    "**What the formula uses**" +
                        "\n\n- Cumulative accuracy." +
                        "\n- Confidence based on number of attempts." +
                        "\n- A small bonus if the question has already appeared in **Play**." +
                        "\n- A moderate bonus from the current correct-answer streak." +
                        "\n\n**Levels**\n\n- `NEW` if score is below `0.20`." +
                        "\n- `LEARNING` between `0.20` and `0.49`." +
                        "\n- `PRACTICED` between `0.50` and `0.79`." +
                        "\n- `MASTERED` from `0.80` upward." +
                        "\n\n**Extra conditions for `MASTERED`**" +
                        "\n\n- At least `3` total attempts." +
                        "\n- At least `2` correct answers." +
                        "\n- Minimum usage evidence: `1` Play attempt or `2` Study attempts." +
                        "\n\nOnly questions in `MASTERED` count as dominated and raise pack progress.",
            ),
        statsUser =
            UserStatsStrings(
                title = "Your stats",
                subtitle = "Global progress across study and play.",
                bannerLine1 = "Check",
                bannerLine2 = "your",
                bannerLine3 = "progress",
                allFilter = "All",
                last7DaysFilter = "Last 7d",
                last30DaysFilter = "Last 30d",
                answeredQuestions = "Questions answered",
                totalTime = "Total time",
                averageAnswerTime = "Avg. per question",
                packsProgress = "Packs progress",
                modePerformance = "Mode performance",
                bestGameScore = "Best game score",
                averageGameScore = "Average game score",
                masteredQuestions = "Mastered questions",
                accuracyTrend = "Accuracy trend",
                packBars = "Performance by pack",
                answerSplit = "Correct vs errors",
                difficultyPerformance = "By difficulty",
                questionInsights = "Question insights",
                fastestQuestion = "Fastest question",
                mostFailedQuestion = "Most failed question",
                noData = "No data yet",
                packBarsDescription = "Accuracy across sessions · pack completion",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "Preferences",
                profileSectionTitle = "Profile",
                profileSectionSubtitle = "Your display name and avatar",
                displayNameLabel = "Display name",
                displayNameHint = "Your name",
                avatarLabel = "Avatar",
                avatarChoosePhoto = "Choose photo",
                avatarDeletePhoto = "Delete photo",
                appearanceSectionTitle = "Appearance",
                appearanceSectionSubtitle = "Choose your preferred theme",
                languageSectionTitle = "Language",
                languageSectionSubtitle = "Choose your preferred language",
                langEnglish = "English",
                langSpanish = "Spanish",
                langCatalan = "Catalan",
                langItalian = "Italian",
                langJapanese = "Japanese",
                langFrench = "French",
                langGerman = "German",
                reminderSectionTitle = "Daily reminder",
                reminderSectionSubtitle = "Get a notification to practice every day",
                reminderEnabledLabel = "Enable daily reminder",
                reminderDaysLabel = "Repeat on",
                reminderTimeLabel = "Reminder time",
                reminderTimePickerTitle = "Set reminder time",
                savePreferencesActionDescription = "Apply the changes you made in preferences.",
                discardPreferencesActionDescription = "Discard the unsaved changes on this screen.",
                dayShortMon = "M",
                dayShortTue = "T",
                dayShortWed = "W",
                dayShortThu = "T",
                dayShortFri = "F",
                dayShortSat = "S",
                dayShortSun = "S",
                confirm = "Confirm",
            ),
        common =
            CommonStrings(
                back = "Back",
                cancel = "Cancel",
                create = "Create",
                save = "Save",
                edit = "Edit",
                delete = "Delete",
                toggleTheme = "Toggle theme",
                seeMore = "See more",
                seeLess = "See less",
                comingSoon = "Coming soon",
                reorderLabel = "Reorder",
                filterLabel = "Filter",
                actionsLabel = "Actions",
                searchPlaceholder = "Search folders & packs...",
                nothingHereYet = "Nothing here yet",
                noSearchResults = "No results found",
                newFolder = "New folder",
                newPack = "New pack",
                questionsStatLabel = "Questions",
                accuracyStatLabel = "Accuracy",
                sessionsStatLabel = "Sessions",
                averageTimeStatLabel = "Average time",
                progressLabel = "Progress",
                pointsShortLabel = "pts",
                subfoldersLabel = { n -> if (n == 1) "1 subfolder" else "$n subfolders" },
                packsLabel = { n -> if (n == 1) "1 pack" else "$n packs" },
                questionsLabel = { n -> if (n == 1) "1 question" else "$n questions" },
                difficultyEasy = "Easy",
                difficultyMedium = "Medium",
                difficultyHard = "Hard",
                difficultyExpert = "Expert",
                studyMode = "Study mode",
                studyModeShort = "Study",
                studyAction = "Study",
                playMode = "Play",
                playModeShort = "Game",
                studyModeTitle = "Study mode",
                gameModeTitle = "Game mode",
                gameSummaryTitle = "Game summary",
                studySummaryTitle = "Study summary",
                studyCorrectAnswersLabel = "Correct",
                studyIncorrectAnswersLabel = "Incorrect",
                studyEffectiveTimeLabel = "Effective time",
                studyBackToPack = "Back to pack",
                studyContinueStudy = "Continue studying",
                studyAverageDifficultyLabel = "Average difficulty",
                studyVerifyAnswer = "Verify answer",
                previewLabel = "Preview",
                questionPreview = "Question preview",
                cancelActionDescription = "Close this editor without saving changes.",
                saveActionDescription = "Save the current changes to this question.",
                deleteFolderKeyword = "folder",
                deletePackKeyword = "pack",
                deleteKeywordHint = "Type the required word",
                createFolderDescription =
                    "Enter the name of the folder you want to create, " +
                        "then choose a color and an icon.",
                editFolderDescription = "Update the folder name, color, and icon.",
                folderNameLabel = "Folder name",
                folderNameHint = "Folder name",
                folderColorLabel = "Color",
                folderIconLabel = "Icon",
                editFolder = "Edit folder",
                deleteFolder = "Delete folder",
                deleteFolderConfirmMessage =
                    "Are you sure you want to delete this folder and all its contents? " +
                        "This includes subfolders, question packs, and all progress linked to them.",
                deleteFolderPrimaryMessage = "Are you sure you want to delete this folder?",
                deleteFolderSecondaryMessage =
                    "This includes subfolders, question packs, " +
                        "and all progress linked to them.",
                deleteFolderTypeKeywordInstruction = { keyword -> "Type \"$keyword\" to enable deletion." },
                createFolderActionDescription = "Add a new folder at the current level.",
                deleteFolderActionDescription = "Delete this folder and all nested content.",
                folderCreatedToast = "Folder created",
                folderDeletedToast = "Folder deleted",
                createPackDescription =
                    "Enter a name for the question pack you want to create, " +
                        "then choose a color and an icon.",
                editPackDescription = "Update the question pack name, color, and icon.",
                packTitleLabel = "Pack title",
                packTitleHint = "Pack title",
                packDescriptionLabel = "Description",
                packDescriptionHint = "Add an optional pack description",
                packColorLabel = "Color",
                packIconLabel = "Icon",
                editPack = "Edit pack",
                deletePack = "Delete pack",
                deletePackConfirmMessage =
                    "Are you sure you want to delete this question pack and all " +
                        "progress linked to it?",
                deletePackPrimaryMessage = "Are you sure you want to delete this pack?",
                deletePackSecondaryMessage = "This includes its questions and all progress linked to it.",
                deletePackTypeKeywordInstruction = { keyword -> "Type \"$keyword\" to enable deletion." },
                createPackActionDescription = "Add a new pack in the current folder.",
                packCreatedToast = "Pack created",
                packDeletedToast = "Pack deleted",
                importUQuizAction = "Import .uquiz",
                exportUQuizAction = "Export .uquiz",
                importUQuizActionDescription = "Import a .uquiz file into this location.",
                exportUQuizActionDescription = "Export the current content as a .uquiz file.",
                importUQuizSuccess = { name -> "\"$name\" imported successfully." },
                exportUQuizSuccess = { name -> "\"$name\" exported successfully." },
            ),
    )
}
