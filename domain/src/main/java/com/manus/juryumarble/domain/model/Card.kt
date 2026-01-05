package com.manus.juryumarble.domain.model

/**
 * 카드 타입
 */
enum class CardType {
    MISSION,    // 미션 수행 카드
    PENALTY,    // 벌칙 카드
    RULE,       // 룰 변경 카드
    EVENT,      // 이벤트 카드
    SAFE        // 안전 카드 (면제, 휴식 등)
}

/**
 * 카드 적용 대상
 */
enum class TargetType {
    SELF,           // 자신에게 적용
    TARGET_ONE,     // 한 명 지목
    ALL,            // 모든 플레이어
    ALL_EXCEPT_SELF // 자신 제외 모든 플레이어
}

/**
 * 카드의 수위 (강도)
 */
enum class Severity {
    MILD,   // 순한맛
    NORMAL, // 보통
    SPICY   // 매운맛
}

/**
 * 카드 데이터 모델
 */
data class Card(
    val cardId: String,
    val cardPackId: String,
    val cardType: CardType,
    val targetType: TargetType,
    val title: String,
    val description: String,
    val severity: Severity,
    val penaltyScale: Float = 1.0f,
    val imageUrl: String? = null,
    val isCustom: Boolean = false
)
