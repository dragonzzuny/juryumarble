package com.manus.juryumarble.data.repository

import com.google.gson.Gson
import com.manus.juryumarble.domain.model.GameState
import com.manus.juryumarble.domain.repository.GameRepository
import javax.inject.Inject

/**
 * GameRepository 구현체
 * 메모리 기반으로 게임 상태를 저장/로드 (추후 SharedPreferences 또는 Room으로 확장)
 */
class GameRepositoryImpl @Inject constructor(
    private val gson: Gson
) : GameRepository {

    override suspend fun saveGameState(gameState: GameState) {
        // 현재는 메모리에만 저장 (추후 SharedPreferences 또는 Room으로 확장)
        lastGameState = gameState
    }

    override suspend fun loadGameState(sessionId: String): GameState? {
        return if (lastGameState?.sessionId == sessionId) lastGameState else null
    }

    override suspend fun getLastGameState(): GameState? {
        return lastGameState
    }

    override suspend fun deleteGameState(sessionId: String) {
        if (lastGameState?.sessionId == sessionId) {
            lastGameState = null
        }
    }

    companion object {
        private var lastGameState: GameState? = null
    }
}
