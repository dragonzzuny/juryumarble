package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Delete a custom card
 */
class DeleteCustomCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(cardId: String) {
        require(cardId.isNotBlank()) { "Card ID must not be blank" }
        cardRepository.deleteCustomCard(cardId)
    }
}
