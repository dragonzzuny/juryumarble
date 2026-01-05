package com.manus.juryumarble.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.model.CardType
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.CardEditDialog
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.CustomCardViewModel

/**
 * Custom Card List Screen
 * Allows users to view, create, edit, and delete custom cards
 */
@Composable
fun CustomCardListScreen(
    onBack: () -> Unit,
    viewModel: CustomCardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedMeshBackground(primaryColor = RichBlack, secondaryColor = NeonPurple)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }

                Text(
                    "커스텀 카드",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { viewModel.showCreateDialog() }) {
                    Icon(Icons.Default.Add, "Add Card", tint = NeonCyan)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Card List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            } else if (uiState.customCards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = TextSecondary
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "커스텀 카드가 없습니다",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "+ 버튼을 눌러 새 카드를 만드세요",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.customCards) { card ->
                        CustomCardItem(
                            card = card,
                            onEdit = { viewModel.showEditDialog(card) },
                            onDelete = { viewModel.deleteCard(card.cardId) }
                        )
                    }
                }
            }
        }

        // Error Snackbar
        uiState.error?.let { error ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Snackbar(
                    containerColor = NeonRed.copy(0.9f),
                    contentColor = Color.White
                ) {
                    Text(error)
                }
            }

            LaunchedEffect(error) {
                kotlinx.coroutines.delay(3000)
                viewModel.clearError()
            }
        }
    }

    // Create/Edit Dialog
    if (uiState.showCardDialog) {
        CardEditDialog(
            card = uiState.editingCard,
            onDismiss = { viewModel.hideDialog() },
            onSave = { title, description, cardType, targetType, severity, penaltyScale ->
                if (uiState.editingCard != null) {
                    viewModel.updateCard(
                        uiState.editingCard!!.copy(
                            title = title,
                            description = description,
                            cardType = cardType,
                            targetType = targetType,
                            severity = severity,
                            penaltyScale = penaltyScale
                        )
                    )
                } else {
                    viewModel.createCard(
                        title, description, cardType, targetType, severity, penaltyScale
                    )
                }
            }
        )
    }
}

@Composable
private fun CustomCardItem(
    card: Card,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val cardColor = when (card.cardType) {
        CardType.MISSION -> NeonCyan
        CardType.PENALTY -> NeonRed
        CardType.RULE -> NeonPurple
        CardType.EVENT -> NeonGold
        CardType.SAFE -> NeonGreen
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = cardColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card Type Badge
                Surface(
                    color = cardColor.copy(0.2f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardColor)
                ) {
                    Text(
                        card.cardType.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = cardColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.weight(1f))

                // Edit Button
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit", tint = NeonCyan)
                }

                // Delete Button
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, "Delete", tint = NeonRed)
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                card.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                card.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoChip("${card.severity.name}", cardColor.copy(0.7f))
                InfoChip("${card.targetType.name}", NeonBlue.copy(0.7f))
                if (card.penaltyScale > 0) {
                    InfoChip("벌칙 x${card.penaltyScale}", NeonRed.copy(0.7f))
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("삭제", color = NeonRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("취소", color = TextSecondary)
                }
            },
            title = {
                Text("카드 삭제")
            },
            text = {
                Text("'${card.title}' 카드를 삭제하시겠습니까?")
            }
        )
    }
}

@Composable
private fun InfoChip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
