package com.uquiz.android.data.local.db

import androidx.room.TypeConverter
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.enums.AttemptStatus
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.stats.enums.QuestionMasteryLevel
import com.uquiz.android.domain.ranking.enums.UserRank
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val listOfStringSerializer = ListSerializer(String.serializer())

    // Function to convert from a list of string to a json
    // insertable in a column of the SQLite room as a string
    @TypeConverter
    fun stringListToJson(value: List<String>): String =
        json.encodeToString(listOfStringSerializer, value)

    // Function to deserialize a string in db (json) into
    // a list of strings
    @TypeConverter
    fun jsonToStringList(value: String): List<String> =
        if (value.isBlank()) emptyList()
        else json.decodeFromString(listOfStringSerializer, value)

    // DifficultyLevel converters
    @TypeConverter
    fun difficultyLevelToString(value: DifficultyLevel): String = value.name

    @TypeConverter
    fun stringToDifficultyLevel(value: String): DifficultyLevel =
        DifficultyLevel.valueOf(value)

    // AttemptMode converters
    @TypeConverter
    fun attemptModeToString(value: AttemptMode): String = value.name

    @TypeConverter
    fun stringToAttemptMode(value: String): AttemptMode =
        AttemptMode.valueOf(value)

    @TypeConverter
    fun attemptStatusToString(value: AttemptStatus): String = value.name

    @TypeConverter
    fun stringToAttemptStatus(value: String): AttemptStatus =
        AttemptStatus.valueOf(value)

    @TypeConverter
    fun questionMasteryLevelToString(value: QuestionMasteryLevel): String = value.name

    @TypeConverter
    fun stringToQuestionMasteryLevel(value: String): QuestionMasteryLevel =
        QuestionMasteryLevel.valueOf(value)

    @TypeConverter
    fun userRankToString(value: UserRank): String = value.name

    @TypeConverter
    fun stringToUserRank(value: String): UserRank =
        UserRank.valueOf(value)
}
