package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.model.GameState
import javax.inject.Inject
import kotlin.random.Random

/**
 * Draw a card from the deck
 * Handles deck exhaustion and reshuffling
 */
class DrawCardUseCase @Inject constructor() {

    operator fun invoke(gameState: GameState): Pair<Card?, GameState> {
        // Check if deck is empty
        if (gameState.deck.isEmpty()) {
            // Try to reshuffle discard pile
            if (gameState.discardPile.isEmpty()) {
                // Both deck and discard pile are empty - no card to draw
                return null to gameState
            }

            // Reshuffle discard pile into deck
            val random = Random(gameState.randomSeed + gameState.currentTurn)
            val newDeck = gameState.discardPile.shuffled(random)
            val reshuffledState = gameState.copy(
                deck = newDeck,
                discardPile = emptyList()
            )

            // Draw from reshuffled deck
            return drawFromDeck(reshuffledState)
        }

        return drawFromDeck(gameState)
    }

    private fun drawFromDeck(gameState: GameState): Pair<Card, GameState> {
        val card = gameState.deck.first()
        val newDeck = gameState.deck.drop(1)

        val newState = gameState.copy(deck = newDeck)

        return card to newState
    }

    /**
     * Discard a card to the discard pile
     */
    fun discardCard(gameState: GameState, card: Card): GameState {
        return gameState.copy(
            discardPile = gameState.discardPile + card
        )
    }
}
