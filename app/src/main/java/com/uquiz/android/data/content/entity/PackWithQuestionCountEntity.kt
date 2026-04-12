package com.uquiz.android.data.content.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

/** Resultado de queries de [com.uquiz.android.data.content.dao.PackDao] que incluyen el conteo de preguntas del pack. */
data class PackWithQuestionCountEntity(
    @Embedded val pack: PackEntity,
    @ColumnInfo(name = "questionCount") val questionCount: Int
)
