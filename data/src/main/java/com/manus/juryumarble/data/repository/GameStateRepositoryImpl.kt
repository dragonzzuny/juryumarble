package com.manus.juryumarble.data.repository

import com.google.gson.Gson
import com.manus.juryumarble.data.local.db.GameStateDao
import com.manus.juryumarble.data.local.model.GameStateEntity
import com.manus.juryumarble.domain.model.GameState
import com.manus.juryumarble.domain.repository.GameStateRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GameStateRepository
 *
 * Handles serialization/deserialization of GameState to/from database
 */
@Singleton
class GameStateRepositoryImpl @Inject constructor(
    private val gameStateDao: GameStateDao,
    private val gson: Gson
) : GameStateRepository {

    override suspend fun saveGameState(gameState: GameState) {
        val entity = GameStateEntity(
            sessionId = gameState.sessionId,
            gameStateJson = gson.toJson(gameState),
            playerNames = gameState.players.joinToString(",") { it.nickname },
            currentPlayerIndex = gameState.currentPlayerIndex,
            currentTurn = gameState.currentTurn,
            totalPlayers = gameState.players.size,
            savedAt = System.currentTimeMillis(),
            startedAt = gameState.startTime,
            isCompleted = gameState.status == com.manus.juryumarble.domain.model.GameStatus.ENDED
        )
        gameStateDao.saveGameState(entity)
    }

    override suspend fun loadLatestGame(): GameState? {
        val entity = gameStateDao.getLatestSavedGame() ?: return null
        return deserializeGameState(entity)
    }

    override suspend fun loadGameBySessionId(sessionId: String): GameState? {
        val entity = gameStateDao.getGameBySessionId(sessionId) ?: return null
        return deserializeGameState(entity)
    }

    override suspend fun hasSavedGame(): Boolean {
        return gameStateDao.getActiveSaveCount() > 0
    }

    override suspend fun markGameAsCompleted(sessionId: String) {
        gameStateDao.markGameAsCompleted(sessionId)
    }

    override suspend fun deleteSavedGame(sessionId: String) {
        gameStateDao.deleteGameState(sessionId)
    }

    override suspend fun deleteOldCompletedGames(cutoffTime: Long) {
        gameStateDao.deleteOldCompletedGames(cutoffTime)
    }

    private fun deserializeGameState(entity: GameStateEntity): GameState? {
        return try {
            gson.fromJson(entity.gameStateJson, GameState::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
