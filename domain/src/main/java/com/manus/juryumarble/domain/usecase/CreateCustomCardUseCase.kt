package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Create a new custom card
 */
class CreateCustomCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(card: Card) {
        require(card.isCustom) { "Card must be marked as custom" }
        require(card.cardId.isNotBlank()) { "Card ID must not be blank" }
        require(card.title.isNotBlank()) { "Card title must not be blank" }

        cardRepository.saveCustomCard(card)
    }

    /**
     * Generate a unique card ID for custom cards
     */
    fun generateCardId(): String {
        return "custom_${System.currentTimeMillis()}"
    }
}
