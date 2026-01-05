package com.manus.juryumarble.domain.repository

import com.manus.juryumarble.domain.model.GameState

/**
 * Repository interface for game state persistence
 *
 * Follows Clean Architecture - domain layer defines the interface,
 * data layer implements it
 */
interface GameStateRepository {

    /**
     * Save current game state
     */
    suspend fun saveGameState(gameState: GameState)

    /**
     * Load the most recent active saved game
     */
    suspend fun loadLatestGame(): GameState?

    /**
     * Load a specific game by session ID
     */
    suspend fun loadGameBySessionId(sessionId: String): GameState?

    /**
     * Check if there's a saved game available
     */
    suspend fun hasSavedGame(): Boolean

    /**
     * Mark a game as completed
     */
    suspend fun markGameAsCompleted(sessionId: String)

    /**
     * Delete a saved game
     */
    suspend fun deleteSavedGame(sessionId: String)

    /**
     * Delete old completed games
     */
    suspend fun deleteOldCompletedGames(cutoffTime: Long)
}
