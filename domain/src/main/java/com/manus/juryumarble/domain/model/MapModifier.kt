package com.manus.juryumarble.domain.model

/**
 * 맵 타일 가중치 변경 데이터
 * 카드팩이 각 타일 타입의 등장 확률을 조정
 */
data class TileWeightModifier(
    val cardWeight: Float = 1.0f,      // 카드 칸 확률 가중치
    val eventWeight: Float = 1.0f,     // 이벤트 칸 확률 가중치
    val safeWeight: Float = 1.0f,      // 휴식 칸 확률 가중치
    val trapWeight: Float = 1.0f,      // 함정 칸 확률 가중치
    val forceIncludeTiles: List<TileTemplate> = emptyList()  // 필수 포함 타일
)

/**
 * 타일 템플릿 (특정 위치에 고정 배치할 타일)
 */
data class TileTemplate(
    val position: Int,           // 보드상 위치 (-1이면 랜덤)
    val type: TileType,
    val title: String,
    val description: String
)

/**
 * 룰 변경 데이터
 */
data class RuleModifier(
    val allowConsecutivePenalty: Boolean = true,    // 연속 벌칙 허용 여부
    val maxConsecutivePenalty: Int = 3,             // 최대 연속 벌칙 횟수
    val penaltyMultiplier: Float = 1.0f,            // 전체 벌칙 강도 배수
    val enableDoubleBonus: Boolean = true,          // 더블 보너스 활성화
    val enableReverseDirection: Boolean = true,     // 방향 전환 활성화
    val winCondition: WinCondition = WinCondition.COMPLETE_ROUNDS  // 승리 조건
)

/**
 * 승리 조건
 */
enum class WinCondition {
    COMPLETE_ROUNDS,    // 정해진 라운드 완료
    REACH_POSITION,     // 특정 위치 도달
    COLLECT_ITEMS,      // 아이템 수집
    LAST_STANDING       // 최후의 생존자
}

/**
 * 이벤트 가중치 변경 데이터
 */
data class EventModifier(
    val specialEventProbability: Float = 0.1f,  // 특별 이벤트 발생 확률
    val miniBossProbability: Float = 0.0f,      // 미니 보스 이벤트 확률
    val bonusRoundProbability: Float = 0.0f,    // 보너스 라운드 확률
    val customEvents: List<String> = emptyList() // 커스텀 이벤트 ID 목록
)

/**
 * Seed Bias - 맵 생성 시드에 영향을 주는 값
 */
data class SeedBias(
    val biasValue: Long = 0L,           // 시드에 더해질 바이어스
    val randomnessLevel: Float = 1.0f   // 랜덤성 정도 (0.0 = 고정, 1.0 = 완전 랜덤)
)

/**
 * 통합 맵 수정자 (카드팩이 적용하는 모든 변경사항)
 */
data class MapModifier(
    val tileWeights: TileWeightModifier = TileWeightModifier(),
    val rules: RuleModifier = RuleModifier(),
    val events: EventModifier = EventModifier(),
    val seedBias: SeedBias = SeedBias()
)
