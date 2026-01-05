package com.manus.juryumarble.di

import com.manus.juryumarble.domain.repository.CardRepository
import com.manus.juryumarble.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * App Layer DI Module - UseCases
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideInitializeGameUseCase(
        cardRepository: CardRepository,
        generateMapUseCase: GenerateMapUseCase
    ): InitializeGameUseCase {
        return InitializeGameUseCase(cardRepository, generateMapUseCase)
    }
}
