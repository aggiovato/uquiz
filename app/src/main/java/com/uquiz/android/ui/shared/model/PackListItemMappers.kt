package com.uquiz.android.ui.shared.model

import com.uquiz.android.domain.content.projection.PackWithQuestionCount
import com.uquiz.android.domain.stats.projection.PackStudyProgress

fun List<PackWithQuestionCount>.toPackListItems(activeProgress: List<PackStudyProgress>): List<PackListItemUiModel> {
    val progressByPackId = activeProgress.associateBy { it.packId }
    return map { item ->
        val answeredCount = progressByPackId[item.pack.id]?.answeredCount ?: 0
        PackListItemUiModel(
            pack = item.pack,
            questionCount = item.questionCount,
            answeredCount = answeredCount.coerceAtMost(item.questionCount),
            progress =
                if (answeredCount > 0 && item.questionCount > 0) {
                    (answeredCount.toFloat() / item.questionCount.toFloat()).coerceIn(0f, 1f)
                } else {
                    null
                },
        )
    }
}
