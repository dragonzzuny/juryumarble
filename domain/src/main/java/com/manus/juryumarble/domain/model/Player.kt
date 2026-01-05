package com.manus.juryumarble.domain.model

/**
 * 플레이어의 주량 레벨
 */
enum class ToleranceLevel {
    LIGHT,  // 라이트 - 벌칙 50%
    NORMAL, // 노말 - 벌칙 100%
    HARD    // 하드 - 벌칙 150%
}

/**
 * 플레이어 데이터 모델
 */
data class Player(
    val id: String,
    val nickname: String,
    val position: Int = 0,
    val toleranceLevel: ToleranceLevel = ToleranceLevel.NORMAL,
    val penaltyCount: Int = 0,
    val consecutivePenalties: Int = 0,
    val isActive: Boolean = true,
    val hasShield: Boolean = false  // 벌칙 면제권 보유 여부
)
