package com.manus.juryumarble.domain.model

/**
 * 맵 템플릿
 * 기본 맵 구조를 정의하며, 카드팩에 따라 동적으로 변형됨
 */
data class MapTemplate(
    val templateId: String,
    val name: String,
    val description: String,
    val boardSize: Int = 16,                    // 전체 타일 개수
    val fixedTiles: List<Tile>,                 // 고정 타일 (시작점 등)
    val variableSlots: Int,                     // 가변 타일 슬롯 개수
    val defaultTileWeights: TileWeightModifier, // 기본 타일 가중치
    val eventHooks: List<EventHook> = emptyList() // 이벤트 훅
)

/**
 * 이벤트 훅
 * 특정 조건에서 발동되는 이벤트
 */
data class EventHook(
    val hookId: String,
    val trigger: EventTrigger,      // 발동 조건
    val effect: EventEffect,        // 효과
    val description: String
)

/**
 * 이벤트 발동 조건
 */
sealed class EventTrigger {
    data class OnPosition(val position: Int) : EventTrigger()  // 특정 위치 도달
    data class OnTurnCount(val turn: Int) : EventTrigger()     // 특정 턴 수 도달
    data class OnPenaltyCount(val count: Int) : EventTrigger() // 벌칙 횟수 도달
    object OnDouble : EventTrigger()                           // 더블 발생 시
}

/**
 * 이벤트 효과
 */
sealed class EventEffect {
    object ReverseDirection : EventEffect()              // 방향 전환
    data class TeleportPlayer(val targetPosition: Int) : EventEffect()  // 플레이어 이동
    data class ModifyPenalty(val multiplier: Float) : EventEffect()     // 벌칙 강도 변경
    data class AddCard(val cardId: String) : EventEffect()              // 특정 카드 추가
    data class ShowMessage(val message: String) : EventEffect()         // 메시지 표시
}

/**
 * 생성된 게임 맵 (최종 결과물)
 */
data class GeneratedMap(
    val sessionId: String,
    val seed: Long,                         // 재현 가능한 시드
    val activatedCardPacks: List<String>,   // 적용된 카드팩 ID 목록
    val tiles: List<Tile>,                  // 최종 타일 배치
    val appliedModifiers: MapModifier,      // 적용된 수정자
    val specialRules: List<String> = emptyList()  // 특별 규칙 설명
)
