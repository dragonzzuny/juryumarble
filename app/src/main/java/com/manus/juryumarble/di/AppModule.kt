package com.manus.juryumarble.di

import com.manus.juryumarble.domain.repository.CardRepository
import com.manus.juryumarble.domain.repository.GameStateRepository
import com.manus.juryumarble.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * App Layer DI Module - UseCases
 *
 * Note: @Inject constructor가 있는 UseCase는 자동 제공됨
 * 여기서는 constructor injection이 불가능한 UseCase만 제공
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGenerateMapUseCase(): GenerateMapUseCase {
        return GenerateMapUseCase()
    }

    @Provides
    @Singleton
    fun provideInitializeGameUseCase(
        cardRepository: CardRepository,
        generateMapUseCase: GenerateMapUseCase
    ): InitializeGameUseCase {
        return InitializeGameUseCase(cardRepository, generateMapUseCase)
    }

    @Provides
    @Singleton
    fun provideProcessTurnUseCase(): ProcessTurnUseCase {
        return ProcessTurnUseCase()
    }

    @Provides
    @Singleton
    fun provideScalePenaltyUseCase(): ScalePenaltyUseCase {
        return ScalePenaltyUseCase()
    }

    @Provides
    @Singleton
    fun provideDrawCardUseCase(processTurnUseCase: ProcessTurnUseCase): DrawCardUseCase {
        return DrawCardUseCase(processTurnUseCase)
    }

    @Provides
    @Singleton
    fun provideExecuteCardEffectUseCase(): ExecuteCardEffectUseCase {
        return ExecuteCardEffectUseCase()
    }

    @Provides
    @Singleton
    fun provideCheckGameEndConditionUseCase(): CheckGameEndConditionUseCase {
        return CheckGameEndConditionUseCase()
    }

    @Provides
    @Singleton
    fun provideCalculateGameStatisticsUseCase(): CalculateGameStatisticsUseCase {
        return CalculateGameStatisticsUseCase()
    }

    @Provides
    @Singleton
    fun provideApplyDDAUseCase(): ApplyDDAUseCase {
        return ApplyDDAUseCase()
    }

    @Provides
    @Singleton
    fun provideSaveGameStateUseCase(
        gameStateRepository: GameStateRepository
    ): SaveGameStateUseCase {
        return SaveGameStateUseCase(gameStateRepository)
    }

    @Provides
    @Singleton
    fun provideLoadGameStateUseCase(
        gameStateRepository: GameStateRepository
    ): LoadGameStateUseCase {
        return LoadGameStateUseCase(gameStateRepository)
    }
}
