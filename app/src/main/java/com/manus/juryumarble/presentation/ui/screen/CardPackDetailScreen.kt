package com.manus.juryumarble.presentation.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.model.CardType
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.CardPackViewModel

/**
 * Card Pack Detail Screen
 * Shows detailed information about a specific card pack including all its cards
 */
@Composable
fun CardPackDetailScreen(
    packId: String,
    onBack: () -> Unit,
    viewModel: CardPackViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(packId) {
        viewModel.loadCardPackDetails(packId)
    }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    "카드팩 상세",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // Loading or Content
            if (uiState.isLoadingDetails) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            } else {
                uiState.selectedPackDetails?.let { details ->
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Pack Info Card
                        item {
                            PackInfoCard(details)
                        }

                        // Statistics Card
                        item {
                            StatisticsCard(details)
                        }

                        // Card List Header
                        item {
                            Text(
                                "카드 목록 (${details.cards.size}장)",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Card List
                        items(details.cards) { card ->
                            CardPreviewItem(card)
                        }
                    }
                } ?: run {
                    // Error state - pack not found
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = NeonRed
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "카드팩을 찾을 수 없습니다",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextSecondary
                            )
                        }
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
}

@Composable
private fun PackInfoCard(
    details: com.manus.juryumarble.domain.usecase.GetCardPackDetailsUseCase.CardPackDetails
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (details.cardPack.isPremium) NeonGold else NeonCyan
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    details.cardPack.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                if (details.cardPack.isPremium) {
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Premium",
                        tint = NeonGold,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                details.cardPack.description,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoChip("${details.cardPack.cardCount}장", NeonCyan)
                InfoChip(details.cardPack.theme, NeonPurple)
                if (details.cardPack.isPremium) {
                    InfoChip("₩${details.cardPack.price.toInt()}", NeonGold)
                }
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    details: com.manus.juryumarble.domain.usecase.GetCardPackDetailsUseCase.CardPackDetails
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = NeonBlue
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "카드 구성",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            // Card Type Distribution
            Text(
                "카드 타입",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(8.dp))

            details.cardTypeDistribution.forEach { (type, count) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        type,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Text(
                        "${count}장",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Severity Distribution
            Text(
                "난이도 분포",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(8.dp))

            details.severityDistribution.forEach { (severity, count) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        severity,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Text(
                        "${count}장",
                        style = MaterialTheme.typography.bodyMedium,
                        color = when (severity) {
                            "MILD" -> NeonGreen
                            "NORMAL" -> NeonCyan
                            "SPICY" -> NeonRed
                            else -> TextSecondary
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CardPreviewItem(card: Card) {
    val cardColor = when (card.cardType) {
        CardType.MISSION -> NeonCyan
        CardType.PENALTY -> NeonRed
        CardType.RULE -> NeonPurple
        CardType.EVENT -> NeonGold
        CardType.SAFE -> NeonGreen
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = cardColor.copy(0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Card Type Icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = cardColor.copy(0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        when (card.cardType) {
                            CardType.MISSION -> Icons.Default.AccountBox
                            CardType.PENALTY -> Icons.Default.Warning
                            CardType.RULE -> Icons.Default.Edit
                            CardType.EVENT -> Icons.Default.Star
                            CardType.SAFE -> Icons.Default.Check
                        },
                        contentDescription = null,
                        tint = cardColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Card Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    card.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    card.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }

            Spacer(Modifier.width(8.dp))

            // Severity Badge
            Surface(
                color = when (card.severity.name) {
                    "MILD" -> NeonGreen.copy(0.2f)
                    "NORMAL" -> NeonCyan.copy(0.2f)
                    "SPICY" -> NeonRed.copy(0.2f)
                    else -> TextSecondary.copy(0.2f)
                },
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when (card.severity.name) {
                        "MILD" -> NeonGreen
                        "NORMAL" -> NeonCyan
                        "SPICY" -> NeonRed
                        else -> TextSecondary
                    }
                )
            ) {
                Text(
                    card.severity.name,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = when (card.severity.name) {
                        "MILD" -> NeonGreen
                        "NORMAL" -> NeonCyan
                        "SPICY" -> NeonRed
                        else -> TextSecondary
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InfoChip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
