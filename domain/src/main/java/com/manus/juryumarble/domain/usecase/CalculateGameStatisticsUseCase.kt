package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.GameState
import com.manus.juryumarble.domain.model.Player
import javax.inject.Inject

/**
 * 게임 통계 계산 UseCase
 *
 * 게임 종료 시 각종 통계를 계산
 */
class CalculateGameStatisticsUseCase @Inject constructor() {

    data class GameStatistics(
        val totalTurns: Int,
        val totalPlayTime: Long, // 밀리초
        val totalCardsUsed: Int,
        val playerStats: List<PlayerStatistics>,
        val mvp: Player?
    )

    data class PlayerStatistics(
        val player: Player,
        val totalPenalties: Int,
        val consecutivePenaltiesMax: Int,
        val cardsCompleted: Int,
        val cardsSkipped: Int
    )

    operator fun invoke(gameState: GameState): GameStatistics {
        val playTime = System.currentTimeMillis() - gameState.startTime

        // 플레이어별 통계 계산
        val playerStats = gameState.players.map { player ->
            PlayerStatistics(
                player = player,
                totalPenalties = player.penaltyCount,
                consecutivePenaltiesMax = player.consecutivePenalties,
                cardsCompleted = player.penaltyCount, // 간소화
                cardsSkipped = 0 // TODO: 추적 로직 추가
            )
        }

        // MVP 선정 (가장 많이 수행한 플레이어)
        val mvp = playerStats.maxByOrNull { it.totalPenalties }?.player

        return GameStatistics(
            totalTurns = gameState.currentTurn,
            totalPlayTime = playTime,
            totalCardsUsed = gameState.discardPile.size,
            playerStats = playerStats,
            mvp = mvp
        )
    }

    /**
     * 플레이 시간을 분:초 형식으로 변환
     */
    fun formatPlayTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "${minutes}분 ${seconds}초"
    }
}
