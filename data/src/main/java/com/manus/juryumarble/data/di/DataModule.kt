package com.manus.juryumarble.data.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.manus.juryumarble.data.local.db.CardDao
import com.manus.juryumarble.data.local.db.CardPackDao
import com.manus.juryumarble.data.local.db.GameStateDao
import com.manus.juryumarble.data.local.db.JuryumarbleDatabase
import com.manus.juryumarble.data.repository.CardRepositoryImpl
import com.manus.juryumarble.data.repository.GameRepositoryImpl
import com.manus.juryumarble.data.repository.GameStateRepositoryImpl
import com.manus.juryumarble.domain.repository.CardRepository
import com.manus.juryumarble.domain.repository.GameRepository
import com.manus.juryumarble.domain.repository.GameStateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Data Layer DI Module
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): JuryumarbleDatabase {
        return Room.databaseBuilder(
            context,
            JuryumarbleDatabase::class.java,
            JuryumarbleDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideCardDao(database: JuryumarbleDatabase): CardDao {
        return database.cardDao()
    }

    @Provides
    @Singleton
    fun provideCardPackDao(database: JuryumarbleDatabase): CardPackDao {
        return database.cardPackDao()
    }

    @Provides
    @Singleton
    fun provideGameStateDao(database: JuryumarbleDatabase): GameStateDao {
        return database.gameStateDao()
    }

    @Provides
    @Singleton
    fun provideCardRepository(cardDao: CardDao, cardPackDao: CardPackDao): CardRepository {
        return CardRepositoryImpl(cardDao, cardPackDao)
    }

    @Provides
    @Singleton
    fun provideGameRepository(gson: Gson): GameRepository {
        return GameRepositoryImpl(gson)
    }

    @Provides
    @Singleton
    fun provideGameStateRepository(
        gameStateDao: GameStateDao,
        gson: Gson
    ): GameStateRepository {
        return GameStateRepositoryImpl(gameStateDao, gson)
    }
}
