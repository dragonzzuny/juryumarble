package com.manus.juryumarble.presentation.ui.screen

import androidx.compose.foundation.background
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
import com.manus.juryumarble.domain.model.CardPack
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.CardPackViewModel

/**
 * Card Pack List Screen
 * Allows users to view and enable/disable card packs
 */
@Composable
fun CardPackListScreen(
    onBack: () -> Unit,
    onPackDetails: (String) -> Unit,
    viewModel: CardPackViewModel = hiltViewModel()
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
                    "카드팩 관리",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                // Enabled pack count badge
                Surface(
                    color = NeonCyan.copy(0.2f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                ) {
                    Text(
                        "${viewModel.getEnabledPackCount()} 활성화",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Loading state
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Free Packs Section
                    if (uiState.freePacks.isNotEmpty()) {
                        item {
                            SectionHeader("무료 카드팩")
                        }
                        items(uiState.freePacks) { pack ->
                            CardPackItem(
                                cardPack = pack,
                                onToggle = { viewModel.toggleCardPack(pack.packId, !pack.isEnabled) },
                                onClick = { onPackDetails(pack.packId) }
                            )
                        }
                    }

                    // Premium Packs Section
                    if (uiState.premiumPacks.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            SectionHeader("프리미엄 카드팩")
                        }
                        items(uiState.premiumPacks) { pack ->
                            CardPackItem(
                                cardPack = pack,
                                onToggle = { viewModel.toggleCardPack(pack.packId, !pack.isEnabled) },
                                onClick = { onPackDetails(pack.packId) },
                                isPremium = true
                            )
                        }
                    }

                    // Empty state
                    if (uiState.cardPacks.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 64.dp),
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
                                        "카드팩이 없습니다",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextSecondary
                                    )
                                }
                            }
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
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        color = TextSecondary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun CardPackItem(
    cardPack: CardPack,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    isPremium: Boolean = false
) {
    val borderColor = if (cardPack.isEnabled) NeonCyan else TextSecondary.copy(0.3f)

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = borderColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pack Icon/Image
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(12.dp),
                color = when {
                    isPremium -> NeonGold.copy(0.2f)
                    else -> NeonCyan.copy(0.2f)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        when {
                            isPremium -> Icons.Default.Star
                            else -> Icons.Default.Add
                        },
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = when {
                            isPremium -> NeonGold
                            else -> NeonCyan
                        }
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Pack Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        cardPack.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    if (isPremium) {
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            color = NeonGold.copy(0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonGold)
                        ) {
                            Text(
                                "₩${cardPack.price.toInt()}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    cardPack.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip("${cardPack.cardCount}장", NeonCyan.copy(0.7f))
                    InfoChip(cardPack.theme, NeonPurple.copy(0.7f))
                }
            }

            Spacer(Modifier.width(16.dp))

            // Toggle Switch
            Switch(
                checked = cardPack.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = NeonCyan.copy(0.5f),
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = TextSecondary.copy(0.3f)
                )
            )
        }
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
