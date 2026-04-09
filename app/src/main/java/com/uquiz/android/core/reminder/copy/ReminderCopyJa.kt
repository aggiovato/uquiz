package com.uquiz.android.core.reminder.copy

import com.uquiz.android.domain.ranking.enums.UserRank

val ReminderCopyJa = ReminderCopySet(
    genericTitles = listOf(
        "そろそろ練習の時間です",
        "次のセッションが待っています",
        "短いラウンドを始めますか？"
    ),
    genericBodies = listOf(
        ReminderBodyTemplate { "数分の練習でもしっかり前に進めます。" },
        ReminderBodyTemplate { "次の問題セットはいつでも始められます。" },
        ReminderBodyTemplate { "今日は短いセッションで流れを保ちましょう。" }
    ),
    streakTitles = listOf(
        "連続記録を守りましょう",
        "いい流れが続いています"
    ),
    streakBodies = listOf(
        ReminderBodyTemplate { "${it.streak}日連続です。次は${it.streak + 1}日を目指しましょう。" },
        ReminderBodyTemplate { "短い1回で${it.streak}日連続の記録をキープできます。" }
    ),
    progressTitles = listOf(
        "進捗をもう一歩進めましょう",
        "次のランクを目指しましょう",
        "XPが待っています"
    ),
    progressBodies = listOf(
        ReminderBodyTemplate { "現在 ${it.points} pt。あと1セッションでさらに伸ばせます。" },
        ReminderBodyTemplate { "ランク ${reminderCopyJaRankLabel(it.rank)} から、もう一段上を狙いませんか？" }
    ),
    resumePackTitles = listOf(
        "続きから再開しましょう",
        "未完了のパックが待っています"
    ),
    resumePackBodies = listOf(
        ReminderBodyTemplate { "「${it.unfinishedPackTitle}」を再開しましょう。残りは ${it.remainingQuestions ?: 0} 問です。" },
        ReminderBodyTemplate { "「${it.unfinishedPackTitle}」は ${(it.nextQuestionIndex ?: 0) + 1} 問目から続けられます。" }
    ),
    rankLabel = ::reminderCopyJaRankLabel
)

private fun reminderCopyJaRankLabel(rank: UserRank): String = when (rank) {
    UserRank.INITIATE -> "イニシエイト"
    UserRank.NEOPHYTE -> "ネオファイト"
    UserRank.ACOLYTE -> "アコライト"
    UserRank.DISCIPLE -> "ディサイプル"
    UserRank.ADEPT -> "アデプト"
    UserRank.VIRTUOSO -> "ヴィルトゥオーソ"
    UserRank.ARCHON -> "アーコン"
    UserRank.PARAGON -> "パラゴン"
}
