package com.manus.juryumarble.domain.model

/**
 * 게임 세션 설정
 *
 * 제품화 수준의 설정 시스템
 * - 논알콜 모드 지원
 * - DDA (동적 난이도 조절) 옵션
 * - 카드팩 선택 및 조합
 * - 안전 장치 설정
 */
data class SessionConfig(
    // 기본 설정
    val playerNames: List<String>,
    val severityFilter: Severity = Severity.NORMAL,  // 수위 필터
    val drinkType: DrinkType = DrinkType.SOJU,
    val drinkUnit: String = "샷",

    // 카드팩 설정
    val activatedCardPackIds: List<String> = listOf("default"),
    val customCards: List<Card> = emptyList(),

    // 게임 규칙
    val useTwoDice: Boolean = true,  // 주사위 2개 사용 여부
    val maxRounds: Int? = null,  // 최대 라운드 (null이면 무제한)
    val maxTimeMinutes: Int? = null,  // 최대 시간 (null이면 무제한)

    // DDA (동적 난이도 조절)
    val enableDDA: Boolean = true,
    val ddaRules: DDARule = DDARule(),

    // 안전 설정
    val safetyConfig: SafetyConfig = SafetyConfig(),
    val autoSuggestBreaks: Boolean = true,

    // 리텐션/UX 설정
    val isNonAlcoholMode: Boolean = false,  // 논알콜 모드
    val enableOneHandMode: Boolean = false,  // 한 손 조작 모드
    val enableQuickRestart: Boolean = true,  // 빠른 재시작

    // 테마 설정
    val activatedThemeSkinId: String? = null,  // 선택된 테마 스킨

    // 광고/프리미엄 상태 (런타임에 주입)
    val isPremiumUser: Boolean = false,  // 광고 제거 구매 여부
    val showAds: Boolean = true           // 광고 표시 여부
)

/**
 * 주종 타입
 */
enum class DrinkType {
    SOJU,       // 소주
    BEER,       // 맥주
    COCKTAIL,   // 칵테일
    WINE,       // 와인
    MAKGEOLLI,  // 막걸리
    NON_ALCOHOL // 논알콜
}
