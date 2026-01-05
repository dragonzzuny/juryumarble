package com.manus.juryumarble.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.manus.juryumarble.domain.model.MapModifier

/**
 * Room TypeConverters
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMapModifier(value: MapModifier?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMapModifier(value: String?): MapModifier? {
        return value?.let { gson.fromJson(it, MapModifier::class.java) }
    }
}
