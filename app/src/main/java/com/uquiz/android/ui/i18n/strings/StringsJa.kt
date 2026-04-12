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

val JaErrors =
    AppErrors(
        folderNameTooLongMessage = "フォルダー名は100文字以内にしてください。",
        packTitleTooLongMessage = "パックのタイトルは100文字以内にしてください。",
        packDescriptionTooLongMessage = "パックの説明は200文字以内にしてください。",
        invalidContentColorMessage = "利用可能なパレットから有効な色を選択してください。",
        invalidContentIconMessage = "利用可能なセットから有効なアイコンを選択してください。",
        duplicateFolderNameMessage = { name -> "この階層には \"$name\" というフォルダーが既に存在します。" },
        duplicatePackTitleMessage = { title -> "このフォルダーには \"$title\" というパックが既に存在します。" },
        invalidImportExtensionMessage = ".uquiz または .uqz ファイルを選択してください。",
        invalidUQuizFormatMessage = "選択したファイルは有効な UQuiz ドキュメントではありません。",
        missingRootFolderImportMessage = "ライブラリからのインポートには少なくとも 1 つのルートフォルダーが必要です。",
        invalidImportRootShapeMessage =
            "インポートするファイルにはルートフォルダー 1 つ、またはルートパック " +
                "1 つだけを含めてください。",
        blankImportedFolderNameMessage = "インポートされたフォルダー名を空にすることはできません。",
        blankImportedPackTitleMessage = "インポートされたパック名を空にすることはできません。",
        emptyImportedPackMessage = "インポートされたパックには少なくとも 1 問必要です。",
        emptyImportedQuestionTextMessage = { index -> "問題 $index に本文がありません。" },
        importedQuestionNeedsTwoOptionsMessage = { index -> "問題 $index には少なくとも 2 つの選択肢が必要です。" },
        importedQuestionNeedsSingleCorrectOptionMessage = { index -> "問題 $index には正解の選択肢が 1 つだけ必要です。" },
        duplicateImportedFolderNamesMessage = "インポートされたフォルダーは同じ階層で名前を重複できません。",
        duplicateImportedPackTitlesMessage = "インポートされたパックは同じ階層でタイトルを重複できません。",
        duplicateImportedOptionLabelsMessage = { index -> "問題 $index に重複した選択肢ラベルがあります。" },
        importReadErrorMessage = "選択したファイルを読み取れませんでした。",
        exportWriteErrorMessage = "選択した場所にファイルを書き込めませんでした。",
        importFailedMessage = "インポートに失敗しました。",
        exportFailedMessage = "エクスポートに失敗しました。",
    )

val JaStrings: AppStrings by lazy {
    AppStrings(
        errors = JaErrors,
        nav =
            NavStrings(
                navHome = "ホーム",
                navLibrary = "ライブラリ",
                navGame = "ゲーム",
                navStats = "統計",
                personalStats = "個人統計",
            ),
        home =
            HomeStrings(
                homeGreeting = { name -> "こんにちは、$name!" },
                homeReadyPrompt = "挑戦する準備はできていますか？",
                homeDayStreakLabel = "連続日数",
                homeScoreLabel = "スコア",
                homeTotalXpLabel = "合計XP",
                homeContinuePlaying = "プレイを続ける",
                homeContinueStudying = "学習を続ける",
                homeRandomPlay = "ランダムプレイ",
                homeRandomStudy = "ランダム学習",
                homeNextLevelLabel = "次のレベル",
                homePointsToUnlockShort = { points -> "あと $points pt" },
                homePointsToUnlockRank = { points, rank -> "$rank まであと $points ポイント" },
                homeMaxRankUnlocked = "最高ランクに到達",
            ),
        library =
            LibraryStrings(
                libraryBannerLine1 = "カードで",
                libraryBannerLine2 = "楽しく",
                libraryBannerLine3 = "学ぼう :)",
                myLibrary = "マイライブラリ",
                foldersSection = "フォルダー",
                recentPacksSection = "最近のパック",
            ),
        folder =
            FolderStrings(
                subfoldersSection = "サブフォルダー",
                packsInFolderSection = "このフォルダー内のパック",
            ),
        pack =
            PackStrings(
                noQuestionsYet = "まだ質問がありません",
                newQuestion = "新しい質問",
                questionsSection = { n -> "質問 ($n)" },
                filterQuestionsHint = "質問を絞り込む...",
            ),
        question =
            QuestionStrings(
                questionEditorTitle = "質問エディター",
                newQuestionTitle = "新しい質問",
                editQuestionTitle = "質問を編集",
                dangerZoneLabel = "危険な操作",
                questionMarkdownLabel = "質問",
                questionMarkdownHint = "Markdownで質問を入力してください",
                explanationMarkdownLabel = "解説",
                explanationMarkdownHint = "必要に応じてMarkdownで解説を入力してください",
                explanationPreview = "解説のプレビュー",
                optionsSectionTitle = "選択肢",
                addOption = "選択肢を追加",
                optionPlaceholder = { n -> "選択肢${n + 1}" },
                correctOptionLabel = "正解",
                difficultySectionTitle = "難易度",
                deleteQuestionTitle = "質問を削除",
                deleteQuestionConfirmMessage = "この質問を削除してもよろしいですか？ この操作は元に戻せません。",
                deleteQuestionPrimaryMessage = "この質問を削除してもよろしいですか？",
                deleteQuestionSecondaryMessage = "この操作は元に戻せません。",
                deleteQuestionTypeKeywordInstruction = { keyword -> "削除を有効にするには「$keyword」と入力してください。" },
                deleteQuestionKeyword = "質問",
                deleteQuestionActionDescription = "この質問を完全に削除します。",
                createQuestionActionDescription = "このパックに新しい質問を作成します。",
                questionCreatedToast = "質問を作成しました",
                questionDeletedToast = "質問を削除しました",
            ),
        studyIntro =
            StudyIntroStrings(
                studyIntroTitle = "パック学習",
                studyReadyDescription = "このパックを問題ごとに学習し、各回答を確認してすぐに解説を確認できます。",
                studyResumeStudy = "学習を再開",
                studyStartStudy = "学習を開始",
                studyResumeProgress = { current, total -> "保存済みの進捗: $current / $total" },
            ),
        studySession =
            StudySessionStrings(
                studyQuestionCounter = { current, total -> "$total 問中 $current 問目" },
                studyPrevious = "前へ",
                studyNext = "次へ",
                studyFinish = "完了",
                studyCorrectTitle = "正解！",
                studyIncorrectTitle = "不正解",
                studyExplanationLabel = "解説",
                studyExitStudy = "学習を終了",
                studyExitTitle = "学習セッションを終了しますか？",
                studyExitMessage = "進捗は保存されるので、あとでこの学習セッションを再開できます。",
            ),
        gameHome =
            GameHomeStrings(
                gameAllPacksSection = "すべてのパック",
                gameContinueButton = "続ける",
                gameBannerLetsTest = "ためそう",
                gameBannerYour = "きみの",
                gameBannerBrain = "ひらめき :)",
                gameRandomPlay = "ランダムプレイ",
            ),
        gameIntro =
            GameIntroStrings(
                gameStartGame = "ゲーム開始",
                gameResumeGame = "ゲーム再開",
                gameEstimatedTimeLabel = "目安時間",
                gameAnsweredSoFar = { current, total -> "$total 問中 $current 問回答済み" },
            ),
        gameSession =
            GameSessionStrings(
                gameScoreLabel = "スコア",
                gameTimeoutLabel = "時間切れ！",
                gameCorrectFeedback = { pts -> "+${pts}pt" },
                gameIncorrectFeedback = { pts -> "−${pts}pt" },
                gameExitTitle = "ゲームを終了しますか？",
                gameExitMessage = "今終了すると、このゲームの進捗は失われます。",
            ),
        gameSummary =
            GameSummaryStrings(
                gameCompleteTitle = "ゲーム完了！",
                gameTotalScoreLabel = "合計スコア",
                gamePlayAgain = "もう一度プレイ",
                gameRankUpTitle = "ランクアップ！",
                gameXpGained = { xp -> "+$xp XP" },
                gameRankProgressLabel = "ランク進捗",
                gameNextRankLabel = { mmr -> "次のランクまで${mmr}MMR" },
            ),
        statsPack =
            PackStatsStrings(
                packStatsTitle = "パック統計",
                packLastSessionLabel = "最終セッション",
                packMostUsedModeLabel = "最も使われたモード",
                packSeeFullStats = "詳細をすべて見る",
                packGeneralSummary = "全体サマリー",
                packByMode = "モード別",
                packRecentActivity = "最近のアクティビティ",
                packSeeAll = "すべて見る",
                packBestPerformance = "最高パフォーマンス",
                packNoSessionsYet = "まだセッションがありません",
                packNoGameSessionsYet = "まだゲームセッションがありません",
                packNoStudySessionsYet = "まだ学習セッションがありません",
                packQuestionsDominated = { current, total -> "$current / $total 問を習得" },
                packThisWeekLabel = { n -> "今週${n}セッション" },
                packStatsHelpAction = "ヘルプ",
                packStatsHelpTitle = "統計の見方",
                packStatsHelpIntro = "これらの指標は、このパックの完了済みセッションのみを集計しています。",
                packStatsHelpQuestions =
                    "**何を計測するか**" +
                        "\n\n- 現在のパック内の質問数。" +
                        "\n- 質問を追加・削除すると変わる。" +
                        "\n- 過去のセッション時点の内容ではなく、現在のパック内容を反映する。",
                packStatsHelpAccuracy =
                    "**計算方法**" +
                        "\n\n- 完了済みセッションのみを使用。" +
                        "\n- 各完了セッションの正答率が使われる。" +
                        "\n- カードはそれらの平均を表示する。" +
                        "\n- 中断したセッションや進行中の学習は含まれない。",
                packStatsHelpSessions =
                    "**ここに含まれるもの**" +
                        "\n\n- **学習**と**プレイ**両方の完了済みセッション数。" +
                        "\n- 開いたまま終えていないセッションはカウントされない。" +
                        "\n- 暫定的な活動ではなく、確定した実績を計測する。",
                packStatsHelpProgress =
                    "**上がる仕組み**" +
                        "\n\n- 習得済み問題数 ÷ パックの総問題数で計算。" +
                        "\n- 進行中セッションの進み具合は使わない。" +
                        "\n- `MASTERED` に達した問題だけが進捗に貢献する。",
                packStatsHelpAverageTime =
                    "**何を表すか**" +
                        "\n\n- パックの完了済みセッションの平均所要時間。" +
                        "\n- 1回通しでどれくらいかかるかを見るための値。" +
                        "\n- 未完了セッション内の問題ごとの読解時間は含まない。",
                packStatsHelpLastSession =
                    "**何を示すか**" +
                        "\n\n- 最後に完了したセッションの終了時刻。" +
                        "\n- 未完了セッションではこの値は更新されない。",
                packStatsHelpMostUsedMode =
                    "**決め方**" +
                        "\n\n- 完了済み**学習**セッション数と**プレイ**セッション数を比較。" +
                        "\n- セッション完了数の多い方が最多使用モードとして表示される。",
                packStatsHelpBestPerformance =
                    "**何を優先するか**" +
                        "\n\n- 完了済みの**プレイ**履歴があれば、最高スコアを表示。" +
                        "\n- プレイ完了履歴がない場合は、**学習**モードの最高正答率を代わりに使用。" +
                        "\n- 実際の成果として最高の地点を示すことが目的。",
                packStatsHelpMasteryTitle = "問題が学習済みと判定される仕組み",
                packStatsHelpMastery =
                    "**計算式が使う要素**" +
                        "\n\n- 累積正答率。" +
                        "\n- 試行回数による信頼度。" +
                        "\n- **プレイ**で少なくとも1回出題済みの場合の小さなボーナス。" +
                        "\n- 現在の連続正解による中程度のボーナス。" +
                        "\n\n**レベル**" +
                        "\n\n- スコアが `0.20` 未満: `NEW`。" +
                        "\n- `0.20` 〜 `0.49`: `LEARNING`。" +
                        "\n- `0.50` 〜 `0.79`: `PRACTICED`。" +
                        "\n- `0.80` 以上: `MASTERED`。" +
                        "\n\n**`MASTERED` の追加条件**" +
                        "\n\n- 合計 `3` 回以上の試行。" +
                        "\n- `2` 回以上の正解。" +
                        "\n- 最低利用実績: プレイで `1` 回以上、または学習で `2` 回以上。" +
                        "\n\n`MASTERED` の問題だけが習得済みとして数えられ、パック進捗を上げます。",
            ),
        statsUser =
            UserStatsStrings(
                title = "あなたの統計",
                subtitle = "学習とプレイ全体の進捗です。",
                bannerLine1 = "あなたの",
                bannerLine2 = "進捗を",
                bannerLine3 = "見よう",
                allFilter = "すべて",
                last7DaysFilter = "直近7日",
                last30DaysFilter = "直近30日",
                answeredQuestions = "回答した質問",
                totalTime = "合計時間",
                averageAnswerTime = "質問ごとの平均",
                packsProgress = "パック進捗",
                modePerformance = "モード別成績",
                bestGameScore = "最高スコア",
                averageGameScore = "平均スコア",
                masteredQuestions = "習得済み質問",
                accuracyTrend = "正答率の推移",
                packBars = "パック別成績",
                answerSplit = "正解とミス",
                difficultyPerformance = "難易度別",
                questionInsights = "質問インサイト",
                fastestQuestion = "最速の質問",
                mostFailedQuestion = "ミスが多い質問",
                noData = "まだデータがありません",
                packBarsDescription = "セッション別精度 · パック進捗",
            ),
        preferences =
            PreferencesStrings(
                preferencesTitle = "設定",
                profileSectionTitle = "プロフィール",
                profileSectionSubtitle = "表示名とアバター",
                displayNameLabel = "表示名",
                displayNameHint = "あなたの名前",
                avatarLabel = "アバター",
                avatarChoosePhoto = "写真を選ぶ",
                avatarDeletePhoto = "写真を削除",
                appearanceSectionTitle = "外観",
                appearanceSectionSubtitle = "テーマを選択",
                languageSectionTitle = "言語",
                languageSectionSubtitle = "言語を選択",
                langEnglish = "英語",
                langSpanish = "スペイン語",
                langCatalan = "カタルーニャ語",
                langItalian = "イタリア語",
                langJapanese = "日本語",
                langFrench = "フランス語",
                langGerman = "ドイツ語",
                reminderSectionTitle = "毎日のリマインダー",
                reminderSectionSubtitle = "毎日練習の通知を受け取る",
                reminderEnabledLabel = "毎日のリマインダーを有効にする",
                reminderDaysLabel = "繰り返し",
                reminderTimeLabel = "リマインダー時刻",
                reminderTimePickerTitle = "時刻を設定",
                savePreferencesActionDescription = "この画面で変更した設定を保存します。",
                discardPreferencesActionDescription = "この画面の未保存の変更を破棄します。",
                dayShortMon = "月",
                dayShortTue = "火",
                dayShortWed = "水",
                dayShortThu = "木",
                dayShortFri = "金",
                dayShortSat = "土",
                dayShortSun = "日",
                confirm = "確認",
            ),
        common =
            CommonStrings(
                back = "戻る",
                cancel = "キャンセル",
                create = "作成",
                save = "保存",
                edit = "編集",
                delete = "削除",
                toggleTheme = "テーマを切り替え",
                seeMore = "もっと見る",
                seeLess = "閉じる",
                comingSoon = "近日公開",
                reorderLabel = "並べ替え",
                filterLabel = "フィルター",
                actionsLabel = "操作",
                searchPlaceholder = "フォルダーとパックを検索...",
                nothingHereYet = "まだ何もありません",
                noSearchResults = "結果が見つかりませんでした",
                newFolder = "新しいフォルダー",
                newPack = "新しいパック",
                questionsStatLabel = "質問数",
                accuracyStatLabel = "正答率",
                sessionsStatLabel = "セッション",
                averageTimeStatLabel = "平均時間",
                progressLabel = "進捗",
                pointsShortLabel = "pt",
                subfoldersLabel = { n -> "${n}個のフォルダー" },
                packsLabel = { n -> "${n}個のパック" },
                questionsLabel = { n -> "${n}問" },
                difficultyEasy = "簡単",
                difficultyMedium = "普通",
                difficultyHard = "難しい",
                difficultyExpert = "上級",
                studyMode = "学習モード",
                studyModeShort = "学習",
                studyAction = "学習",
                playMode = "プレイ",
                playModeShort = "ゲーム",
                studyModeTitle = "学習モード",
                gameModeTitle = "ゲームモード",
                gameSummaryTitle = "ゲームサマリー",
                studySummaryTitle = "学習サマリー",
                studyCorrectAnswersLabel = "正解数",
                studyIncorrectAnswersLabel = "不正解数",
                studyEffectiveTimeLabel = "実学習時間",
                studyBackToPack = "パックに戻る",
                studyContinueStudy = "学習を続ける",
                studyAverageDifficultyLabel = "平均難易度",
                studyVerifyAnswer = "回答を確認",
                previewLabel = "プレビュー",
                questionPreview = "質問のプレビュー",
                cancelActionDescription = "変更を保存せずにこのエディターを閉じます。",
                saveActionDescription = "この質問への現在の変更を保存します。",
                deleteFolderKeyword = "フォルダー",
                deletePackKeyword = "パック",
                deleteKeywordHint = "指定された単語を入力",
                createFolderDescription = "作成するフォルダー名を入力し、色とアイコンを選択してください。",
                editFolderDescription = "フォルダー名、色、アイコンを更新してください。",
                folderNameLabel = "フォルダー名",
                folderNameHint = "フォルダー名",
                folderColorLabel = "カラー",
                folderIconLabel = "アイコン",
                editFolder = "フォルダーを編集",
                deleteFolder = "フォルダーを削除",
                deleteFolderConfirmMessage =
                    "このフォルダーとその中のすべてを削除してもよろしいですか？ サブフォルダー、" +
                        "問題パック、および関連する進捗も含まれます。",
                deleteFolderPrimaryMessage = "このフォルダーを削除してもよろしいですか？",
                deleteFolderSecondaryMessage = "サブフォルダー、問題パック、および関連する進捗も含まれます。",
                deleteFolderTypeKeywordInstruction = { keyword -> "削除を有効にするには「$keyword」と入力してください。" },
                createFolderActionDescription = "現在の階層に新しいフォルダーを追加します。",
                deleteFolderActionDescription = "このフォルダーとその中のすべての内容を削除します。",
                folderCreatedToast = "フォルダを作成しました",
                folderDeletedToast = "フォルダを削除しました",
                createPackDescription = "作成したい問題パックの名前を入力し、色とアイコンを選択してください。",
                editPackDescription = "問題パックの名前、色、アイコンを更新してください。",
                packTitleLabel = "パック名",
                packTitleHint = "パック名",
                packDescriptionLabel = "説明",
                packDescriptionHint = "パックの説明を任意で追加",
                packColorLabel = "カラー",
                packIconLabel = "アイコン",
                editPack = "パックを編集",
                deletePack = "パックを削除",
                deletePackConfirmMessage = "この問題パックと関連する進捗をすべて削除してもよろしいですか？",
                deletePackPrimaryMessage = "このパックを削除してもよろしいですか？",
                deletePackSecondaryMessage = "その問題と関連する進捗もすべて削除されます。",
                deletePackTypeKeywordInstruction = { keyword -> "削除を有効にするには「$keyword」と入力してください。" },
                createPackActionDescription = "現在のフォルダーに新しいパックを追加します。",
                packCreatedToast = "パックを作成しました",
                packDeletedToast = "パックを削除しました",
                importUQuizAction = ".uquiz をインポート",
                exportUQuizAction = ".uquiz をエクスポート",
                importUQuizActionDescription = "この場所に .uquiz ファイルをインポートします。",
                exportUQuizActionDescription = "現在の内容を .uquiz ファイルとして書き出します。",
                importUQuizSuccess = { name -> "\"$name\" を正常にインポートしました。" },
                exportUQuizSuccess = { name -> "\"$name\" を正常にエクスポートしました。" },
            ),
    )
}
