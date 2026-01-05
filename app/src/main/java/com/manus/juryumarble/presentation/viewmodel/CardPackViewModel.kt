package com.manus.juryumarble.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manus.juryumarble.domain.model.CardPack
import com.manus.juryumarble.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing card packs
 */
@HiltViewModel
class CardPackViewModel @Inject constructor(
    private val getAllCardPacksUseCase: GetAllCardPacksUseCase,
    private val toggleCardPackUseCase: ToggleCardPackUseCase,
    private val getCardPackDetailsUseCase: GetCardPackDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardPackUiState())
    val uiState: StateFlow<CardPackUiState> = _uiState.asStateFlow()

    init {
        loadCardPacks()
    }

    /**
     * Load all card packs
     */
    fun loadCardPacks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val packs = getAllCardPacksUseCase()
                _uiState.update {
                    it.copy(
                        cardPacks = packs,
                        freePacks = packs.filter { pack -> !pack.isPremium },
                        premiumPacks = packs.filter { pack -> pack.isPremium },
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load card packs"
                    )
                }
            }
        }
    }

    /**
     * Toggle a card pack's enabled state
     */
    fun toggleCardPack(packId: String, enabled: Boolean) {
        viewModelScope.launch {
            try {
                toggleCardPackUseCase(packId, enabled)

                // Update local state immediately for better UX
                _uiState.update { state ->
                    state.copy(
                        cardPacks = state.cardPacks.map { pack ->
                            if (pack.packId == packId) pack.copy(isEnabled = enabled) else pack
                        },
                        freePacks = state.freePacks.map { pack ->
                            if (pack.packId == packId) pack.copy(isEnabled = enabled) else pack
                        },
                        premiumPacks = state.premiumPacks.map { pack ->
                            if (pack.packId == packId) pack.copy(isEnabled = enabled) else pack
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to toggle card pack")
                }
            }
        }
    }

    /**
     * Load details for a specific card pack
     */
    fun loadCardPackDetails(packId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDetails = true) }
            try {
                val details = getCardPackDetailsUseCase(packId)
                _uiState.update {
                    it.copy(
                        selectedPackDetails = details,
                        isLoadingDetails = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingDetails = false,
                        error = e.message ?: "Failed to load pack details"
                    )
                }
            }
        }
    }

    /**
     * Clear selected pack details
     */
    fun clearSelectedPack() {
        _uiState.update { it.copy(selectedPackDetails = null) }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Get count of enabled packs
     */
    fun getEnabledPackCount(): Int {
        return _uiState.value.cardPacks.count { it.isEnabled }
    }
}

/**
 * UI State for card pack management
 */
data class CardPackUiState(
    val cardPacks: List<CardPack> = emptyList(),
    val freePacks: List<CardPack> = emptyList(),
    val premiumPacks: List<CardPack> = emptyList(),
    val selectedPackDetails: GetCardPackDetailsUseCase.CardPackDetails? = null,
    val isLoading: Boolean = false,
    val isLoadingDetails: Boolean = false,
    val error: String? = null
)
