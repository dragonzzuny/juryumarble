package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Get all custom cards
 */
class GetCustomCardsUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(): List<Card> {
        return cardRepository.getCustomCards()
    }
}
