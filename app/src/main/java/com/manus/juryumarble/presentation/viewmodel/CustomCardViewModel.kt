package com.manus.juryumarble.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manus.juryumarble.domain.model.*
import com.manus.juryumarble.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing custom cards
 */
@HiltViewModel
class CustomCardViewModel @Inject constructor(
    private val getCustomCardsUseCase: GetCustomCardsUseCase,
    private val createCustomCardUseCase: CreateCustomCardUseCase,
    private val updateCustomCardUseCase: UpdateCustomCardUseCase,
    private val deleteCustomCardUseCase: DeleteCustomCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomCardUiState())
    val uiState: StateFlow<CustomCardUiState> = _uiState.asStateFlow()

    init {
        loadCustomCards()
    }

    /**
     * Load all custom cards
     */
    fun loadCustomCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val cards = getCustomCardsUseCase()
                _uiState.update {
                    it.copy(
                        customCards = cards,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load cards"
                    )
                }
            }
        }
    }

    /**
     * Create a new custom card
     */
    fun createCard(
        title: String,
        description: String,
        cardType: CardType,
        targetType: TargetType,
        severity: Severity,
        penaltyScale: Float
    ) {
        viewModelScope.launch {
            try {
                val cardId = createCustomCardUseCase.generateCardId()
                val card = Card(
                    cardId = cardId,
                    cardPackId = "custom",
                    cardType = cardType,
                    targetType = targetType,
                    title = title,
                    description = description,
                    severity = severity,
                    penaltyScale = penaltyScale,
                    isCustom = true
                )

                createCustomCardUseCase(card)
                loadCustomCards()
                _uiState.update { it.copy(showCardDialog = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to create card")
                }
            }
        }
    }

    /**
     * Update an existing custom card
     */
    fun updateCard(card: Card) {
        viewModelScope.launch {
            try {
                updateCustomCardUseCase(card)
                loadCustomCards()
                _uiState.update { it.copy(showCardDialog = false, editingCard = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to update card")
                }
            }
        }
    }

    /**
     * Delete a custom card
     */
    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            try {
                deleteCustomCardUseCase(cardId)
                loadCustomCards()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to delete card")
                }
            }
        }
    }

    /**
     * Show card creation dialog
     */
    fun showCreateDialog() {
        _uiState.update {
            it.copy(
                showCardDialog = true,
                editingCard = null
            )
        }
    }

    /**
     * Show card edit dialog
     */
    fun showEditDialog(card: Card) {
        _uiState.update {
            it.copy(
                showCardDialog = true,
                editingCard = card
            )
        }
    }

    /**
     * Hide dialog
     */
    fun hideDialog() {
        _uiState.update {
            it.copy(
                showCardDialog = false,
                editingCard = null
            )
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI State for custom card management
 */
data class CustomCardUiState(
    val customCards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val showCardDialog: Boolean = false,
    val editingCard: Card? = null,
    val error: String? = null
)
