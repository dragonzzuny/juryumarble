package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Update an existing custom card
 */
class UpdateCustomCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(card: Card) {
        require(card.isCustom) { "Only custom cards can be updated" }
        require(card.cardId.isNotBlank()) { "Card ID must not be blank" }
        require(card.title.isNotBlank()) { "Card title must not be blank" }

        cardRepository.saveCustomCard(card)
    }
}
