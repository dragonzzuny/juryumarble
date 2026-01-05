package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.*
import javax.inject.Inject
import kotlin.random.Random

/**
 * 턴 진행 UseCase
 * 게임의 한 턴을 처리합니다.
 */
class ProcessTurnUseCase @Inject constructor() {
    
    /**
     * 주사위 굴리기
     */
    fun rollDice(gameState: GameState, useTwoDice: Boolean = true): DiceResult {
        val random = Random(gameState.randomSeed + gameState.currentTurn)
        
        val dice1 = random.nextInt(1, 7)
        val dice2 = if (useTwoDice) random.nextInt(1, 7) else 0
        
        val isDouble = useTwoDice && dice1 == dice2
        val total = dice1 + dice2
        
        return DiceResult(
            dice1 = dice1,
            dice2 = if (useTwoDice) dice2 else null,
            total = if (useTwoDice) total else dice1,
            isDouble = isDouble
        )
    }
    
    /**
     * 플레이어 이동
     */
    fun movePlayer(gameState: GameState, diceResult: DiceResult): GameState {
        val currentPlayer = gameState.currentPlayer
        val direction = if (gameState.direction == GameDirection.CLOCKWISE) 1 else -1
        
        var newPosition = (currentPlayer.position + diceResult.total * direction) % gameState.boardSize
        if (newPosition < 0) {
            newPosition += gameState.boardSize
        }
        
        val updatedPlayer = currentPlayer.copy(position = newPosition)
        val updatedPlayers = gameState.players.toMutableList().apply {
            this[gameState.currentPlayerIndex] = updatedPlayer
        }
        
        val newDoubleCount = if (diceResult.isDouble) gameState.doubleCount + 1 else 0
        
        return gameState.copy(
            players = updatedPlayers,
            lastDiceResult = diceResult.total,
            turnPhase = TurnPhase.TILE_EVENT,
            doubleCount = newDoubleCount
        )
    }
    
    /**
     * 칸 이벤트 처리
     */
    fun processTileEvent(gameState: GameState): GameState {
        val currentTile = gameState.board[gameState.currentPlayer.position]
        
        return when (currentTile.type) {
            TileType.START -> gameState.copy(turnPhase = TurnPhase.END)
            TileType.CARD -> gameState.copy(turnPhase = TurnPhase.ACTION_RESOLUTION)
            TileType.EVENT -> handleEventTile(gameState)
            TileType.SAFE -> gameState.copy(turnPhase = TurnPhase.END)
            TileType.TRAP -> handleTrapTile(gameState)
        }
    }
    
    /**
     * 카드 뽑기 (덱 고갈 시 자동 재섞기)
     */
    fun drawCard(gameState: GameState): GameState {
        // 덱이 비었으면 버린 카드를 다시 섞음
        val currentState = if (gameState.deck.isEmpty()) {
            if (gameState.discardPile.isEmpty()) {
                // 덱과 버린 카드 더미가 모두 비어있으면 게임 종료
                return gameState.copy(
                    status = GameStatus.ENDED,
                    turnPhase = TurnPhase.END
                )
            }
            reshuffleDeck(gameState)
        } else {
            gameState
        }

        val drawnCard = currentState.deck.first()
        val remainingDeck = currentState.deck.drop(1)

        return currentState.copy(
            deck = remainingDeck,
            currentCard = drawnCard,
            turnPhase = TurnPhase.ACTION_RESOLUTION
        )
    }

    /**
     * 덱 재섞기
     */
    private fun reshuffleDeck(gameState: GameState): GameState {
        val random = Random(gameState.randomSeed + gameState.currentTurn)
        val reshuffledDeck = gameState.discardPile.shuffled(random)

        return gameState.copy(
            deck = reshuffledDeck,
            discardPile = emptyList()
        )
    }
    
    /**
     * 카드 수행 완료
     */
    fun completeCard(gameState: GameState, completed: Boolean): GameState {
        val currentCard = gameState.currentCard ?: return gameState
        val currentPlayer = gameState.currentPlayer
        
        val updatedPlayer = if (completed) {
            currentPlayer.copy(
                penaltyCount = currentPlayer.penaltyCount + 1,
                consecutivePenalties = currentPlayer.consecutivePenalties + 1
            )
        } else {
            currentPlayer.copy(consecutivePenalties = 0)
        }
        
        val updatedPlayers = gameState.players.toMutableList().apply {
            this[gameState.currentPlayerIndex] = updatedPlayer
        }
        
        val updatedDiscardPile = gameState.discardPile + currentCard
        
        return gameState.copy(
            players = updatedPlayers,
            discardPile = updatedDiscardPile,
            currentCard = null,
            turnPhase = TurnPhase.END
        )
    }
    
    /**
     * 턴 종료 및 다음 플레이어로 이동
     */
    fun endTurn(gameState: GameState): GameState {
        // 더블이면 같은 플레이어가 계속 (3회 초과 시 제외)
        val samePlayer = gameState.doubleCount in 1..2

        val nextPlayerIndex = if (samePlayer) {
            gameState.currentPlayerIndex
        } else {
            getNextActivePlayerIndex(gameState)
        }

        val newTurn = if (!samePlayer) gameState.currentTurn + 1 else gameState.currentTurn

        return gameState.copy(
            currentPlayerIndex = nextPlayerIndex,
            currentTurn = newTurn,
            turnPhase = TurnPhase.START,
            doubleCount = if (samePlayer) gameState.doubleCount else 0,
            lastDiceResult = null,
            currentCard = null
        )
    }

    /**
     * 다음 활성 플레이어 인덱스 찾기
     */
    private fun getNextActivePlayerIndex(gameState: GameState): Int {
        val direction = if (gameState.direction == GameDirection.CLOCKWISE) 1 else -1
        var next = (gameState.currentPlayerIndex + direction) % gameState.players.size
        if (next < 0) next += gameState.players.size

        // 비활성 플레이어 건너뛰기
        var iterations = 0
        while (!gameState.players[next].isActive && iterations < gameState.players.size) {
            next = (next + direction) % gameState.players.size
            if (next < 0) next += gameState.players.size
            iterations++
        }

        return next
    }

    /**
     * 상태 전이 검증
     */
    fun validateStateTransition(from: TurnPhase, to: TurnPhase): Boolean {
        return when (from) {
            TurnPhase.START -> to == TurnPhase.DICE_ROLL
            TurnPhase.DICE_ROLL -> to == TurnPhase.PLAYER_MOVE
            TurnPhase.PLAYER_MOVE -> to == TurnPhase.TILE_EVENT
            TurnPhase.TILE_EVENT -> to == TurnPhase.ACTION_RESOLUTION || to == TurnPhase.END
            TurnPhase.ACTION_RESOLUTION -> to == TurnPhase.END
            TurnPhase.END -> to == TurnPhase.START
        }
    }
    
    private fun handleEventTile(gameState: GameState): GameState {
        // 방향 전환 이벤트
        val newDirection = if (gameState.direction == GameDirection.CLOCKWISE) {
            GameDirection.COUNTER_CLOCKWISE
        } else {
            GameDirection.CLOCKWISE
        }
        return gameState.copy(
            direction = newDirection,
            turnPhase = TurnPhase.END
        )
    }
    
    private fun handleTrapTile(gameState: GameState): GameState {
        // 함정 칸: 바로 카드 뽑기로 이동 (2배 벌칙)
        return gameState.copy(turnPhase = TurnPhase.ACTION_RESOLUTION)
    }
}
