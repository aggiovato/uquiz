package com.uquiz.android.data.content.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class PackWithQuestionCountEntity(
    @Embedded val pack: PackEntity,
    @ColumnInfo(name = "questionCount") val questionCount: Int
)
