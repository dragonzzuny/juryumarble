package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.model.CardPack
import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Get detailed information about a card pack including its cards
 */
class GetCardPackDetailsUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    data class CardPackDetails(
        val cardPack: CardPack,
        val cards: List<Card>,
        val cardTypeDistribution: Map<String, Int>,
        val severityDistribution: Map<String, Int>
    )

    suspend operator fun invoke(packId: String): CardPackDetails? {
        val allPacks = cardRepository.getAllCardPacks()
        val cardPack = allPacks.find { it.packId == packId } ?: return null
        val cards = cardRepository.getCardsByPack(packId)

        val cardTypeDistribution = cards.groupingBy { it.cardType.name }.eachCount()
        val severityDistribution = cards.groupingBy { it.severity.name }.eachCount()

        return CardPackDetails(
            cardPack = cardPack,
            cards = cards,
            cardTypeDistribution = cardTypeDistribution,
            severityDistribution = severityDistribution
        )
    }
}
