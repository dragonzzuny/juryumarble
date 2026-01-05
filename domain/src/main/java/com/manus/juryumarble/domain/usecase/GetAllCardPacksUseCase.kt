package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.CardPack
import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Get all available card packs
 */
class GetAllCardPacksUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(): List<CardPack> {
        return cardRepository.getAllCardPacks()
    }

    /**
     * Get only enabled card packs
     */
    suspend fun getEnabledOnly(): List<CardPack> {
        return cardRepository.getEnabledCardPacks()
    }

    /**
     * Get card packs grouped by premium status
     */
    suspend fun getGroupedByPremium(): Map<Boolean, List<CardPack>> {
        val allPacks = cardRepository.getAllCardPacks()
        return allPacks.groupBy { it.isPremium }
    }
}
