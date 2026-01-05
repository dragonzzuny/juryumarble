package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.*
import javax.inject.Inject

/**
 * Execute the effect of a card on the game state
 * Handles different card types and their effects
 */
class ExecuteCardEffectUseCase @Inject constructor(
    private val scalePenaltyUseCase: ScalePenaltyUseCase
) {

    data class CardExecutionResult(
        val updatedGameState: GameState,
        val updatedPlayers: List<Player>,
        val message: String,
        val requiresPlayerSelection: Boolean = false,
        val allowedPlayerIndices: List<Int> = emptyList()
    )

    operator fun invoke(
        gameState: GameState,
        card: Card,
        targetPlayerIndex: Int? = null
    ): CardExecutionResult {
        return when (card.cardType) {
            CardType.PENALTY -> executePenaltyCard(gameState, card, targetPlayerIndex)
            CardType.MISSION -> executeMissionCard(gameState, card)
            CardType.RULE -> executeRuleCard(gameState, card)
            CardType.EVENT -> executeEventCard(gameState, card)
            CardType.SAFE -> executeSafeCard(gameState, card)
        }
    }

    private fun executePenaltyCard(
        gameState: GameState,
        card: Card,
        targetPlayerIndex: Int?
    ): CardExecutionResult {
        val currentPlayer = gameState.currentPlayer
        val scaledPenalty = scalePenaltyUseCase(card.penaltyScale, gameState, currentPlayer)

        return when (card.targetType) {
            TargetType.SELF -> {
                val updatedPlayer = currentPlayer.copy(
                    penaltyCount = currentPlayer.penaltyCount + scaledPenalty.toInt(),
                    consecutivePenalties = currentPlayer.consecutivePenalties + 1
                )
                val updatedPlayers = gameState.players.map {
                    if (it.id == currentPlayer.id) updatedPlayer else it
                }

                CardExecutionResult(
                    updatedGameState = gameState.copy(players = updatedPlayers),
                    updatedPlayers = updatedPlayers,
                    message = "${currentPlayer.nickname}님, ${card.title}! ${scaledPenalty}샷"
                )
            }

            TargetType.TARGET_ONE -> {
                if (targetPlayerIndex == null) {
                    // Need player selection
                    val allowedIndices = gameState.players.indices.filter { it != gameState.currentPlayerIndex }
                    return CardExecutionResult(
                        updatedGameState = gameState,
                        updatedPlayers = gameState.players,
                        message = "벌칙을 받을 플레이어를 선택하세요",
                        requiresPlayerSelection = true,
                        allowedPlayerIndices = allowedIndices
                    )
                }

                val targetPlayer = gameState.players[targetPlayerIndex]
                val updatedTarget = targetPlayer.copy(
                    penaltyCount = targetPlayer.penaltyCount + scaledPenalty.toInt(),
                    consecutivePenalties = targetPlayer.consecutivePenalties + 1
                )
                val updatedPlayers = gameState.players.mapIndexed { index, player ->
                    if (index == targetPlayerIndex) updatedTarget else player
                }

                CardExecutionResult(
                    updatedGameState = gameState.copy(players = updatedPlayers),
                    updatedPlayers = updatedPlayers,
                    message = "${currentPlayer.nickname}님이 ${targetPlayer.nickname}님을 지목! ${scaledPenalty}샷"
                )
            }

            TargetType.ALL -> {
                val updatedPlayers = gameState.players.map { player ->
                    player.copy(
                        penaltyCount = player.penaltyCount + scaledPenalty.toInt(),
                        consecutivePenalties = if (player.id == currentPlayer.id) player.consecutivePenalties + 1 else 0
                    )
                }

                CardExecutionResult(
                    updatedGameState = gameState.copy(players = updatedPlayers),
                    updatedPlayers = updatedPlayers,
                    message = "모두 함께! ${scaledPenalty}샷"
                )
            }

            TargetType.ALL_EXCEPT_SELF -> {
                val updatedPlayers = gameState.players.map { player ->
                    if (player.id == currentPlayer.id) {
                        player
                    } else {
                        player.copy(
                            penaltyCount = player.penaltyCount + scaledPenalty.toInt()
                        )
                    }
                }

                CardExecutionResult(
                    updatedGameState = gameState.copy(players = updatedPlayers),
                    updatedPlayers = updatedPlayers,
                    message = "${currentPlayer.nickname}님 빼고 모두! ${scaledPenalty}샷"
                )
            }
        }
    }

    private fun executeMissionCard(gameState: GameState, card: Card): CardExecutionResult {
        // Mission cards just show the mission text
        // Actual completion/failure is handled by user action
        return CardExecutionResult(
            updatedGameState = gameState,
            updatedPlayers = gameState.players,
            message = "미션: ${card.description}"
        )
    }

    private fun executeRuleCard(gameState: GameState, card: Card): CardExecutionResult {
        // Rule cards modify game rules
        return CardExecutionResult(
            updatedGameState = gameState,
            updatedPlayers = gameState.players,
            message = "새로운 규칙: ${card.description}"
        )
    }

    private fun executeEventCard(gameState: GameState, card: Card): CardExecutionResult {
        // Event cards can change game direction, skip turns, etc.
        val updatedState = when {
            card.title.contains("방향") -> {
                val newDirection = if (gameState.direction == GameDirection.CLOCKWISE) {
                    GameDirection.COUNTER_CLOCKWISE
                } else {
                    GameDirection.CLOCKWISE
                }
                gameState.copy(direction = newDirection)
            }
            else -> gameState
        }

        return CardExecutionResult(
            updatedGameState = updatedState,
            updatedPlayers = gameState.players,
            message = "이벤트: ${card.description}"
        )
    }

    private fun executeSafeCard(gameState: GameState, card: Card): CardExecutionResult {
        val currentPlayer = gameState.currentPlayer

        // Reset consecutive penalties for the current player
        val updatedPlayer = currentPlayer.copy(consecutivePenalties = 0)
        val updatedPlayers = gameState.players.map {
            if (it.id == currentPlayer.id) updatedPlayer else it
        }

        return CardExecutionResult(
            updatedGameState = gameState.copy(players = updatedPlayers),
            updatedPlayers = updatedPlayers,
            message = "${currentPlayer.nickname}님 축하합니다! ${card.description}"
        )
    }
}
