package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.*
import javax.inject.Inject
import kotlin.math.ceil

/**
 * 벌칙 스케일링 UseCase
 * 플레이어의 주량과 설정에 따라 벌칙 강도를 조절합니다.
 */
class ScalePenaltyUseCase @Inject constructor() {
    
    /**
     * 벌칙 스케일링 적용 (간단 버전)
     */
    operator fun invoke(
        penaltyScale: Float,
        gameState: GameState,
        player: Player
    ): Float {
        var baseAmount = penaltyScale

        // 1. 연속 벌칙 완화 (3회 이상 연속 시 50% 감소)
        if (player.consecutivePenalties >= 3) {
            baseAmount *= 0.5f
        }

        return baseAmount.coerceAtLeast(0.5f)
    }

    /**
     * 벌칙 스케일링 적용 (상세 버전)
     */
    fun scaleWithDetails(
        card: Card,
        player: Player,
        drinkType: DrinkType,
        drinkUnit: String
    ): ScaledPenalty {
        var baseAmount = card.penaltyScale

        // 1. 연속 벌칙 완화 (3회 이상 연속 시 50% 감소)
        if (player.consecutivePenalties >= 3) {
            baseAmount *= 0.5f
        }

        // 2. 논알콜 모드 처리
        if (drinkType == DrinkType.NON_ALCOHOL) {
            return ScaledPenalty(
                originalScale = card.penaltyScale,
                scaledAmount = 1,
                unit = "잔",
                displayText = "물 한 잔 마시기",
                isNonAlcohol = true
            )
        }

        // 3. 단위 변환
        val (amount, unit) = convertToUnit(baseAmount, drinkUnit, drinkType)

        // 4. 표시 텍스트 생성
        val displayText = generateDisplayText(amount, unit, card.cardType)

        return ScaledPenalty(
            originalScale = card.penaltyScale,
            scaledAmount = amount,
            unit = unit,
            displayText = displayText,
            isNonAlcohol = false
        )
    }
    
    private fun convertToUnit(
        baseAmount: Float,
        drinkUnit: String,
        drinkType: DrinkType
    ): Pair<Int, String> {
        val finalAmount = ceil(baseAmount).toInt().coerceAtLeast(1)
        
        return when (drinkUnit) {
            "모금" -> {
                // 1샷 = 3모금
                Pair(finalAmount * 3, "모금")
            }
            "잔" -> {
                Pair(finalAmount, "잔")
            }
            else -> {
                Pair(finalAmount, "샷")
            }
        }
    }
    
    private fun generateDisplayText(amount: Int, unit: String, cardType: CardType): String {
        return when (cardType) {
            CardType.PENALTY, CardType.MISSION -> "$amount $unit 마시기"
            else -> "$amount $unit"
        }
    }
}

/**
 * 스케일링된 벌칙 결과
 */
data class ScaledPenalty(
    val originalScale: Float,
    val scaledAmount: Int,
    val unit: String,
    val displayText: String,
    val isNonAlcohol: Boolean
)
