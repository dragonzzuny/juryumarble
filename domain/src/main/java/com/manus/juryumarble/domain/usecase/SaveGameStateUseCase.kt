package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.GameState
import com.manus.juryumarble.domain.repository.GameStateRepository
import javax.inject.Inject

/**
 * Save game state to local storage
 *
 * Serializes the entire GameState to JSON and stores it in the database
 * for resuming later
 */
class SaveGameStateUseCase @Inject constructor(
    private val repository: GameStateRepository
) {

    suspend operator fun invoke(gameState: GameState) {
        repository.saveGameState(gameState)
    }

    /**
     * Mark a game as completed (won't appear in resume list)
     */
    suspend fun markCompleted(sessionId: String) {
        repository.markGameAsCompleted(sessionId)
    }

    /**
     * Delete old completed games (older than 7 days)
     */
    suspend fun cleanupOldGames() {
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
        repository.deleteOldCompletedGames(sevenDaysAgo)
    }
}
