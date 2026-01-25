package com.uquiz.android.data.local.db

import androidx.room.TypeConverter
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
}