package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.GameState
import com.manus.juryumarble.domain.repository.GameStateRepository
import javax.inject.Inject

/**
 * Load saved game state from local storage
 *
 * Deserializes the game state from JSON and restores the game
 */
class LoadGameStateUseCase @Inject constructor(
    private val repository: GameStateRepository
) {

    /**
     * Load the most recent active saved game
     */
    suspend operator fun invoke(): GameState? {
        return repository.loadLatestGame()
    }

    /**
     * Load a specific game by session ID
     */
    suspend fun loadBySessionId(sessionId: String): GameState? {
        return repository.loadGameBySessionId(sessionId)
    }

    /**
     * Check if there's a saved game available to resume
     */
    suspend fun hasSavedGame(): Boolean {
        return repository.hasSavedGame()
    }

    /**
     * Delete a saved game
     */
    suspend fun deleteSave(sessionId: String) {
        repository.deleteSavedGame(sessionId)
    }
}
