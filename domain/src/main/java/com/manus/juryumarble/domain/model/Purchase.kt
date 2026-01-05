package com.manus.juryumarble.domain.model

/**
 * 구매 상태 관리
 */
data class PurchaseStatus(
    val isPremiumUser: Boolean = false,           // 광고 제거 구매 여부
    val ownedCardPacks: List<String> = emptyList(), // 보유 카드팩 ID 목록
    val ownedSkins: List<String> = emptyList(),     // 보유 스킨 ID 목록
    val purchaseHistory: List<Purchase> = emptyList() // 구매 내역
)

/**
 * 개별 구매 기록
 */
data class Purchase(
    val purchaseId: String,
    val productId: String,
    val productType: ProductType,
    val purchaseTime: Long,
    val isRestored: Boolean = false
)

/**
 * 상품 타입
 */
enum class ProductType {
    REMOVE_ADS,      // 광고 제거
    CARD_PACK,       // 카드팩
    THEME_SKIN,      // 테마 스킨
    SUBSCRIPTION     // 구독 (향후 확장용)
}

/**
 * 광고 설정
 */
data class AdConfig(
    val showInterstitialOnGameStart: Boolean = true,  // 게임 시작 시 전면 광고
    val showInterstitialOnGameEnd: Boolean = true,    // 게임 종료 시 전면 광고
    val showBannerAd: Boolean = true,                 // 하단 배너 광고
    val adSkipDelaySeconds: Int = 3,                  // 광고 스킵 가능 시간
    val adLoadFailureMaxRetry: Int = 2                // 광고 로드 실패 시 재시도 횟수
)

/**
 * 테마 보드 스킨
 */
data class ThemeSkin(
    val skinId: String,
    val name: String,
    val description: String,
    val previewImageUrl: String? = null,
    val backgroundImageUrl: String? = null,
    val tileStyle: TileStyleConfig,
    val tokenStyle: TokenStyleConfig,
    val isPremium: Boolean = true,
    val price: Double = 0.0
)

/**
 * 타일 스타일 설정
 */
data class TileStyleConfig(
    val tileShapeType: String = "rounded",  // rounded, square, hexagon 등
    val tileColorScheme: String = "default",
    val glowEffect: Boolean = false
)

/**
 * 말(토큰) 스타일 설정
 */
data class TokenStyleConfig(
    val tokenType: String = "circle",  // circle, emoji, character 등
    val animationType: String = "bounce"
)
