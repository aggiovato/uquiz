package com.uquiz.android.data.stats.query

/** Resultado de [com.uquiz.android.data.stats.dao.PackStatsDao.getUserPackCompletion]: conteo de packs completados y en progreso. */
data class UserPackCompletionRow(
    val completedPacks: Int,
    val inProgressPacks: Int,
)
