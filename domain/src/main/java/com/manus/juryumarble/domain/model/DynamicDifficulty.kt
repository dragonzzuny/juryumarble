package com.manus.juryumarble.domain.model

/**
 * 동적 난이도 조절 (DDA - Dynamic Difficulty Adjustment)
 *
 * 장시간 플레이 시 과도한 벌칙/반복을 방지하고,
 * 플레이어 상태에 따라 이벤트/벌칙 확률을 미세 조정
 */
data class DifficultyState(
    val currentDifficultyLevel: Float = 1.0f,      // 현재 난이도 (0.5 = 쉬움, 1.0 = 보통, 1.5 = 어려움)
    val consecutivePenaltyCount: Int = 0,          // 연속 벌칙 횟수
    val totalPlayTimeMinutes: Int = 0,             // 총 플레이 시간 (분)
    val restCardUsedCount: Int = 0,                // 휴식 카드 사용 횟수
    val playerFatigueLevel: Float = 0.0f,          // 플레이어 피로도 (0.0 ~ 1.0)
    val lastAdjustmentTime: Long = 0L              // 마지막 난이도 조정 시간
)

/**
 * DDA 조정 규칙
 */
data class DDARule(
    val maxConsecutivePenalty: Int = 3,            // 최대 연속 벌칙 (이후 확률 감소)
    val fatigueLimitMinutes: Int = 60,             // 피로도 임계 시간 (분)
    val penaltyReductionRate: Float = 0.2f,        // 피로 시 벌칙 확률 감소율
    val restCardBoostRate: Float = 0.3f,           // 휴식 카드 등장 확률 증가율
    val autoRestAfterMinutes: Int = 90,            // 자동 휴식 제안 시간
    val enableAutoBreakSuggestion: Boolean = true  // 자동 휴식 제안 활성화
)

/**
 * DDA 조정 액션
 */
sealed class DDAAction {
    data class ReducePenaltyProbability(val reduction: Float) : DDAAction()
    data class IncreaseRestProbability(val increase: Float) : DDAAction()
    object SuggestBreak : DDAAction()  // "잠시 쉬어가세요" 제안
    object ResetDifficulty : DDAAction()
}
