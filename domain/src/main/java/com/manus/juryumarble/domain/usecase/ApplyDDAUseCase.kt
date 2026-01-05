package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.*
import javax.inject.Inject

/**
 * DDA (Dynamic Difficulty Adjustment) 적용 UseCase
 *
 * 게임 중 플레이어 상태를 모니터링하고,
 * 과도한 벌칙/반복을 방지하기 위해 난이도를 동적으로 조절
 */
class ApplyDDAUseCase @Inject constructor() {

    /**
     * DDA 상태 업데이트
     *
     * @param currentState 현재 DDA 상태
     * @param gameState 현재 게임 상태
     * @param rules DDA 규칙
     * @return 업데이트된 DDA 상태 및 조정 액션
     */
    fun updateDifficultyState(
        currentState: DifficultyState,
        gameState: GameState,
        rules: DDARule
    ): Pair<DifficultyState, List<DDAAction>> {
        val currentPlayer = gameState.currentPlayer
        val playTimeMinutes = ((System.currentTimeMillis() - gameState.startTime) / 60000).toInt()

        // 연속 벌칙 카운트 업데이트
        val consecutivePenalty = currentPlayer.consecutivePenalties

        // 피로도 계산
        val fatigueLevel = calculateFatigue(
            playTimeMinutes,
            consecutivePenalty,
            currentState.restCardUsedCount,
            rules
        )

        // DDA 조정 액션 결정
        val actions = mutableListOf<DDAAction>()

        // 1. 연속 벌칙 제한 체크
        if (consecutivePenalty >= rules.maxConsecutivePenalty) {
            actions.add(DDAAction.ReducePenaltyProbability(rules.penaltyReductionRate))
            actions.add(DDAAction.IncreaseRestProbability(rules.restCardBoostRate))
        }

        // 2. 장시간 플레이 체크
        if (playTimeMinutes >= rules.fatigueLimitMinutes && rules.enableAutoBreakSuggestion) {
            actions.add(DDAAction.SuggestBreak)
        }

        // 3. 극한 피로도 체크
        if (fatigueLevel >= 0.8f) {
            actions.add(DDAAction.ReducePenaltyProbability(0.5f))
            actions.add(DDAAction.SuggestBreak)
        }

        // 난이도 레벨 조정
        val newDifficultyLevel = calculateDifficultyLevel(fatigueLevel, consecutivePenalty)

        val updatedState = currentState.copy(
            currentDifficultyLevel = newDifficultyLevel,
            consecutivePenaltyCount = consecutivePenalty,
            totalPlayTimeMinutes = playTimeMinutes,
            playerFatigueLevel = fatigueLevel,
            lastAdjustmentTime = System.currentTimeMillis()
        )

        return Pair(updatedState, actions)
    }

    /**
     * 피로도 계산
     */
    private fun calculateFatigue(
        playTimeMinutes: Int,
        consecutivePenalty: Int,
        restCardUsed: Int,
        rules: DDARule
    ): Float {
        // 시간 기반 피로도 (0.0 ~ 0.5)
        val timeFatigue = (playTimeMinutes.toFloat() / rules.fatigueLimitMinutes).coerceIn(0f, 0.5f)

        // 연속 벌칙 기반 피로도 (0.0 ~ 0.4)
        val penaltyFatigue = (consecutivePenalty.toFloat() / rules.maxConsecutivePenalty * 0.4f).coerceIn(0f, 0.4f)

        // 휴식 카드 사용으로 피로도 감소 (최대 -0.3)
        val restReduction = (restCardUsed * 0.1f).coerceIn(0f, 0.3f)

        return (timeFatigue + penaltyFatigue - restReduction).coerceIn(0f, 1f)
    }

    /**
     * 난이도 레벨 계산
     */
    private fun calculateDifficultyLevel(fatigueLevel: Float, consecutivePenalty: Int): Float {
        // 피로도가 높을수록 난이도 낮춤
        val fatiguePenalty = fatigueLevel * 0.3f

        // 연속 벌칙이 많을수록 난이도 낮춤
        val penaltyPenalty = if (consecutivePenalty > 2) 0.2f else 0f

        return (1.0f - fatiguePenalty - penaltyPenalty).coerceIn(0.5f, 1.5f)
    }

    /**
     * 휴식 카드 사용 기록
     */
    fun recordRestCardUsed(currentState: DifficultyState): DifficultyState {
        return currentState.copy(
            restCardUsedCount = currentState.restCardUsedCount + 1,
            consecutivePenaltyCount = 0,  // 연속 벌칙 카운트 리셋
            playerFatigueLevel = (currentState.playerFatigueLevel - 0.2f).coerceAtLeast(0f)
        )
    }
}
