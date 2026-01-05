package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * Toggle card pack enabled/disabled state
 */
class ToggleCardPackUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {

    suspend operator fun invoke(packId: String, enabled: Boolean) {
        cardRepository.updateCardPackEnabled(packId, enabled)
    }

    /**
     * Enable a card pack
     */
    suspend fun enable(packId: String) {
        cardRepository.updateCardPackEnabled(packId, true)
    }

    /**
     * Disable a card pack
     */
    suspend fun disable(packId: String) {
        cardRepository.updateCardPackEnabled(packId, false)
    }
}
