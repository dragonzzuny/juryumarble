package com.manus.juryumarble.domain.model

/**
 * 주사위 굴림 결과
 */
data class DiceResult(
    val dice1: Int,
    val dice2: Int? = null,  // 주사위 2개 사용 시
    val total: Int,
    val isDouble: Boolean = false
) {
    companion object {
        fun fromSingleDice(value: Int): DiceResult {
            return DiceResult(
                dice1 = value,
                dice2 = null,
                total = value,
                isDouble = false
            )
        }

        fun fromTwoDice(value1: Int, value2: Int): DiceResult {
            return DiceResult(
                dice1 = value1,
                dice2 = value2,
                total = value1 + value2,
                isDouble = value1 == value2
            )
        }
    }
}
