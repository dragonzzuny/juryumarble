package com.manus.juryumarble.data.local.db

import androidx.room.*
import com.manus.juryumarble.data.local.model.GameStateEntity

/**
 * DAO for saved game states
 */
@Dao
interface GameStateDao {

    @Query("SELECT * FROM saved_games WHERE isCompleted = 0 ORDER BY savedAt DESC LIMIT 1")
    suspend fun getLatestSavedGame(): GameStateEntity?

    @Query("SELECT * FROM saved_games WHERE sessionId = :sessionId")
    suspend fun getGameBySessionId(sessionId: String): GameStateEntity?

    @Query("SELECT * FROM saved_games WHERE isCompleted = 0 ORDER BY savedAt DESC")
    suspend fun getAllActiveSavedGames(): List<GameStateEntity>

    @Query("SELECT * FROM saved_games ORDER BY savedAt DESC LIMIT :limit")
    suspend fun getRecentGames(limit: Int = 10): List<GameStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGameState(gameState: GameStateEntity)

    @Query("UPDATE saved_games SET isCompleted = 1 WHERE sessionId = :sessionId")
    suspend fun markGameAsCompleted(sessionId: String)

    @Query("DELETE FROM saved_games WHERE sessionId = :sessionId")
    suspend fun deleteGameState(sessionId: String)

    @Query("DELETE FROM saved_games WHERE isCompleted = 1 AND savedAt < :cutoffTime")
    suspend fun deleteOldCompletedGames(cutoffTime: Long)

    @Query("SELECT COUNT(*) FROM saved_games WHERE isCompleted = 0")
    suspend fun getActiveSaveCount(): Int
}
