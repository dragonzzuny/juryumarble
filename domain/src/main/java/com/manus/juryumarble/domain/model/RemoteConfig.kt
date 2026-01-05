package com.manus.juryumarble.domain.model

/**
 * 원격 설정 (Remote Configuration)
 *
 * 앱 업데이트 없이 게임 밸런스, 확률, 이벤트를 조정 가능
 * Firebase Remote Config 또는 자체 서버 사용
 */
data class RemoteGameConfig(
    val version: String,                              // 설정 버전
    val lastUpdated: Long,                            // 마지막 업데이트 시간
    val cardProbabilities: CardProbabilityConfig,     // 카드 확률 설정
    val eventProbabilities: EventProbabilityConfig,   // 이벤트 확률 설정
    val ddaRules: DDARule,                            // DDA 규칙
    val featureFlags: FeatureFlags,                   // 기능 플래그
    val seasonalEvents: List<SeasonalEvent> = emptyList() // 시즌 이벤트
)

/**
 * 카드 확률 설정
 */
data class CardProbabilityConfig(
    val basePenaltyWeight: Float = 0.3f,
    val baseMissionWeight: Float = 0.25f,
    val baseRuleWeight: Float = 0.15f,
    val baseEventWeight: Float = 0.2f,
    val baseSafeWeight: Float = 0.1f
)

/**
 * 이벤트 확률 설정
 */
data class EventProbabilityConfig(
    val specialEventChance: Float = 0.1f,
    val reverseDirectionChance: Float = 0.05f,
    val bonusRoundChance: Float = 0.03f
)

/**
 * 기능 플래그
 */
data class FeatureFlags(
    val enableDDA: Boolean = true,                // DDA 활성화
    val enableRemoteCardPacks: Boolean = false,   // 원격 카드팩 다운로드
    val enableSeasonalThemes: Boolean = true,     // 시즌 테마 활성화
    val enableAdvancedAnalytics: Boolean = false, // 상세 분석 (비개인정보)
    val maintenanceMode: Boolean = false          // 점검 모드
)

/**
 * 시즌/이벤트 맵
 */
data class SeasonalEvent(
    val eventId: String,
    val name: String,
    val description: String,
    val startTime: Long,                          // 시작 시간 (Unix timestamp)
    val endTime: Long,                            // 종료 시간
    val isActive: Boolean,
    val specialCardPackId: String? = null,        // 한정 카드팩 ID
    val themeOverride: ThemeOverride? = null,     // 테마 덮어쓰기
    val bonusRewards: List<String> = emptyList()  // 보너스 보상 (IAP ID)
)

/**
 * 테마 덮어쓰기
 */
data class ThemeOverride(
    val backgroundImageUrl: String,
    val musicUrl: String? = null,
    val customColors: Map<String, String> = emptyMap()
)
