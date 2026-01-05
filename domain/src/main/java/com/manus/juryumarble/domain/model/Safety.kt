package com.manus.juryumarble.domain.model

/**
 * 안전 및 책임 음주 관련 모델
 *
 * 스토어 정책 준수 및 사용자 안전을 위한 기능
 */
data class SafetyConfig(
    val ageVerificationRequired: Boolean = true,     // 연령 확인 필수
    val minimumAge: Int = 19,                        // 최소 연령 (한국 기준)
    val showResponsibleDrinkingNotice: Boolean = true, // 책임 음주 안내
    val enableSafetyReminders: Boolean = true,       // 안전 알림 (휴식/물)
    val maxSessionDurationMinutes: Int = 120,        // 권장 최대 세션 시간
    val autoSuggestWaterBreak: Boolean = true        // 물 마시기 자동 제안
)

/**
 * 연령 확인 상태
 */
data class AgeVerification(
    val isVerified: Boolean = false,
    val verificationDate: Long = 0L,
    val birthYear: Int? = null  // 개인정보 수집하지 않으므로 선택적
)

/**
 * 안전 알림
 */
sealed class SafetyAlert {
    object DrinkWater : SafetyAlert()              // "물 마시기"
    object TakeBreak : SafetyAlert()               // "잠시 쉬기"
    object StopPlaying : SafetyAlert()             // "게임 중단 권장"
    data class CustomMessage(val message: String) : SafetyAlert()
}

/**
 * 게임 기록 (로컬 저장)
 *
 * 개인 식별 정보는 수집하지 않음
 */
data class GameSessionLog(
    val sessionId: String,
    val startTime: Long,
    val endTime: Long? = null,
    val playerCount: Int,
    val totalTurns: Int,
    val cardsPlayed: Int,
    val safetyAlertsShown: Int,
    val isNonAlcoholMode: Boolean,
    val activatedCardPacks: List<String>,
    val difficultyAdjustments: Int
)

/**
 * 사용자 설정 (로컬 저장)
 */
data class UserPreferences(
    val enableOneHandMode: Boolean = false,          // 한 손 조작 모드
    val enableQuickRestart: Boolean = true,          // 빠른 재시작
    val lockScreenOrientation: Boolean = true,       // 화면 회전 잠금
    val enableVibration: Boolean = true,
    val soundVolume: Float = 0.7f,
    val preferredLanguage: String = "ko",
    val nonAlcoholModeDefault: Boolean = false       // 기본 논알콜 모드
)
