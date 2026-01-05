package com.manus.juryumarble.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manus.juryumarble.data.local.model.CardEntity
import com.manus.juryumarble.data.local.model.CardPackEntity
import com.manus.juryumarble.data.local.model.GameStateEntity

/**
 * Room Database for Juryumarble
 */
@Database(
    entities = [CardEntity::class, CardPackEntity::class, GameStateEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JuryumarbleDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun cardPackDao(): CardPackDao
    abstract fun gameStateDao(): GameStateDao

    companion object {
        const val DATABASE_NAME = "juryumarble_db"
    }
}
