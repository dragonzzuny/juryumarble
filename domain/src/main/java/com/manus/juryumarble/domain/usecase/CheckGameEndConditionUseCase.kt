package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.GameState
import com.manus.juryumarble.domain.model.SessionConfig
import javax.inject.Inject

/**
 * 게임 종료 조건 체크 UseCase
 *
 * 다양한 게임 종료 조건을 체크하여 게임을 종료할지 결정
 */
class CheckGameEndConditionUseCase @Inject constructor() {

    /**
     * 게임 종료 여부 확인
     *
     * @return Pair<shouldEnd, reason>
     */
    operator fun invoke(
        gameState: GameState,
        config: SessionConfig
    ): Pair<Boolean, String?> {
        // 1. 최대 라운드 체크
        config.maxRounds?.let { maxRounds ->
            if (gameState.currentTurn >= maxRounds) {
                return true to "최대 라운드 도달 (${maxRounds}라운드)"
            }
        }

        // 2. 최대 시간 체크
        config.maxTimeMinutes?.let { maxTimeMinutes ->
            val elapsedMinutes = (System.currentTimeMillis() - gameState.startTime) / 60000
            if (elapsedMinutes >= maxTimeMinutes) {
                return true to "최대 시간 도달 (${maxTimeMinutes}분)"
            }
        }

        // 3. 덱 완전 고갈 체크
        if (gameState.deck.isEmpty() && gameState.discardPile.isEmpty()) {
            return true to "모든 카드 소진"
        }

        // 4. 활성 플레이어 부족 체크
        val activePlayers = gameState.players.filter { it.isActive }
        if (activePlayers.size < 2) {
            return true to "활성 플레이어 부족"
        }

        return false to null
    }

    /**
     * 게임을 강제로 종료
     */
    fun forceEnd(gameState: GameState, reason: String = "수동 종료"): GameState {
        return gameState.copy(
            status = com.manus.juryumarble.domain.model.GameStatus.ENDED
        )
    }
}
