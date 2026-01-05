package com.manus.juryumarble.domain.model

/**
 * 보드 칸(타일) 타입
 */
enum class TileType {
    START,      // 출발 칸
    CARD,       // 카드 뽑기 칸
    EVENT,      // 이벤트 칸
    SAFE,       // 휴식 칸
    TRAP        // 함정 칸
}

/**
 * 보드의 한 칸을 나타내는 데이터 모델
 */
data class Tile(
    val position: Int,  // 보드상 위치 (0부터 시작)
    val type: TileType,
    val name: String,
    val description: String = ""
)
