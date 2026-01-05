package com.manus.juryumarble.domain.model

/**
 * 턴 진행 상태
 */
enum class TurnPhase {
    START,              // 턴 시작
    DICE_ROLL,          // 주사위 대기
    PLAYER_MOVE,        // 말 이동 중
    TILE_EVENT,         // 칸 이벤트 처리
    ACTION_RESOLUTION,  // 카드 수행/스킵 결정
    END                 // 턴 종료
}

/**
 * 게임 전체 상태
 */
enum class GameStatus {
    SETUP,      // 게임 설정 중
    READY,      // 게임 시작 대기
    IN_PROGRESS,// 게임 진행 중
    ENDED       // 게임 종료
}

/**
 * 게임 진행 방향
 */
enum class GameDirection {
    CLOCKWISE,      // 시계 방향
    COUNTER_CLOCKWISE // 반시계 방향
}

/**
 * 게임 상태 데이터 모델 (불변 객체)
 */
data class GameState(
    val sessionId: String,
    val randomSeed: Long,
    val players: List<Player>,
    val board: List<Tile>,
    val deck: List<Card>,
    val discardPile: List<Card> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val currentTurn: Int = 1,
    val turnPhase: TurnPhase = TurnPhase.START,
    val status: GameStatus = GameStatus.READY,
    val direction: GameDirection = GameDirection.CLOCKWISE,
    val lastDiceResult: Int? = null,
    val currentCard: Card? = null,
    val activeRules: List<Card> = emptyList(),  // 현재 적용 중인 룰 카드들
    val doubleCount: Int = 0,  // 더블 연속 횟수
    val startTime: Long = System.currentTimeMillis()
) {
    val currentPlayer: Player
        get() = players[currentPlayerIndex]
    
    val boardSize: Int
        get() = board.size
}
