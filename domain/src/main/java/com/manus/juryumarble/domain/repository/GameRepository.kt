package com.manus.juryumarble.domain.repository

import com.manus.juryumarble.domain.model.GameState

/**
 * 게임 세션 Repository 인터페이스
 */
interface GameRepository {
    /**
     * 게임 상태 저장
     */
    suspend fun saveGameState(gameState: GameState)
    
    /**
     * 게임 상태 로드
     */
    suspend fun loadGameState(sessionId: String): GameState?
    
    /**
     * 마지막 게임 상태 로드 (게임 재개용)
     */
    suspend fun getLastGameState(): GameState?
    
    /**
     * 게임 상태 삭제
     */
    suspend fun deleteGameState(sessionId: String)
}
