package com.manus.juryumarble.presentation.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.manus.juryumarble.domain.model.CardType
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.DiceRollingAnimation
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.GameViewModel

/**
 * GameBoardScreen - Visual Overhaul
 */
@Composable
fun GameBoardScreen(
    onGameEnd: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val playerColors = listOf(NeonPurple, NeonCyan, NeonPink, NeonGreen, NeonGold, NeonBlue)

    // Menu state
    var showMenu by remember { mutableStateOf(false) }

    // SnackbarHost for DDA messages
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // DDA message display
    LaunchedEffect(uiState.ddaMessage) {
        uiState.ddaMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.consumeDdaMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedMeshBackground(primaryColor = RichBlack, secondaryColor = NeonBlue)

        // Show loading if game is not started yet
        if (!uiState.isGameStarted) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = NeonCyan)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "게임을 준비 중입니다...",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Player Status Glass Panel with turn transition animation
            val currentPlayerColor = playerColors[uiState.currentPlayerIndex % playerColors.size]

            // Turn transition animation
            val playerChangeScale = remember { Animatable(1f) }
            val playerChangeAlpha = remember { Animatable(0.3f) }

            LaunchedEffect(uiState.currentPlayerIndex) {
                // Pulse effect when player changes
                launch {
                    playerChangeScale.animateTo(1.15f, animationSpec = tween(200))
                    playerChangeScale.animateTo(1f, animationSpec = tween(300))
                }
                launch {
                    playerChangeAlpha.animateTo(1f, animationSpec = tween(150))
                    playerChangeAlpha.animateTo(0.3f, animationSpec = tween(2000))
                }
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val color = currentPlayerColor

                    // Animated player avatar
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .scale(playerChangeScale.value)
                            .clip(CircleShape)
                            .background(color.copy(0.1f))
                            .border(2.dp, color, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Pulsing glow effect
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .scale(1.2f)
                                .clip(CircleShape)
                                .background(color.copy(playerChangeAlpha.value * 0.3f))
                                .blur(8.dp)
                        )

                        Text(
                            uiState.currentPlayerName.firstOrNull()?.toString() ?: "?",
                            style = MaterialTheme.typography.headlineSmall,
                            color = color,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            uiState.currentPlayerName,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "위치: ${(uiState.playerPositions["player_${uiState.currentPlayerIndex}"] ?: 0) + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = NeonCyan
                            )
                            Text(
                                "턴: ${uiState.totalTurns + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = NeonGold
                            )
                            // 벌칙 카운트 표시
                            val currentPlayer = viewModel.currentPlayers.getOrNull(uiState.currentPlayerIndex)
                            currentPlayer?.let {
                                Text(
                                    "벌칙: ${it.penaltyCount}잔",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (it.penaltyCount > 5) NeonRed else NeonPink,
                                    fontWeight = if (it.penaltyCount > 5) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Turn Badge with pulsing animation
                    val badgePulse = rememberInfiniteTransition(label = "badge_pulse")
                    val badgeGlow by badgePulse.animateFloat(
                        initialValue = 0.2f,
                        targetValue = 0.5f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "badge_glow"
                    )

                    Surface(
                        color = NeonPurple.copy(alpha = badgeGlow),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple)
                    ) {
                        Text(
                            "당신 차례!",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonPurple,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Modern Board Visual
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cornerRadius = 32.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val targetPos = uiState.playerPositions["player_${uiState.currentPlayerIndex}"] ?: 0
                        var animatedPos by remember { mutableStateOf(targetPos) }

                        // Piece movement animation - sequential tile-by-tile
                        LaunchedEffect(targetPos, uiState.isRolling) {
                            if (!uiState.isRolling && targetPos != animatedPos) {
                                val start = animatedPos
                                val steps = if (targetPos > start) {
                                    (start + 1..targetPos).toList()
                                } else if (targetPos < start) {
                                    // Handle wrap-around (going past tile 15 back to 0)
                                    ((start + 1..15).toList() + (0..targetPos).toList())
                                } else {
                                    emptyList()
                                }

                                steps.forEach { step ->
                                    kotlinx.coroutines.delay(200) // 200ms per tile
                                    animatedPos = step
                                }
                            } else if (uiState.isRolling) {
                                // Reset animation position when new roll starts
                                animatedPos = targetPos
                            }
                        }

                        // Board Rows
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (i in 0..3) PremiumTile(i, animatedPos == i)
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                for (i in listOf(15, 14, 13)) PremiumTile(i, animatedPos == i)
                            }

                            // Enhanced Interactive Dice Center
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                DiceRollingAnimation(
                                    isRolling = uiState.isRolling,
                                    diceResult = uiState.lastDiceResult
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                for (i in 4..6) PremiumTile(i, animatedPos == i)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (i in listOf(12, 11, 10, 9, 8, 7)) PremiumTile(i, animatedPos == i)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Action UI
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Menu button
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.White.copy(0.1f), RoundedCornerShape(20.dp))
                    ) {
                        Icon(Icons.Default.Menu, "Menu", tint = NeonCyan)
                    }

                    // Dropdown menu
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(RichBlack.copy(0.95f))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Save, null, tint = NeonGold)
                                    Text("게임 저장", color = Color.White)
                                }
                            },
                            onClick = {
                                viewModel.saveGame()
                                showMenu = false
                            }
                        )

                        HorizontalDivider(color = Color.White.copy(0.1f))

                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.ExitToApp, null, tint = NeonRed)
                                    Text("게임 종료", color = Color.White)
                                }
                            },
                            onClick = {
                                viewModel.saveGame()
                                viewModel.forceEndGame()
                                showMenu = false
                                onGameEnd()
                            }
                        )
                    }
                }

                Button(
                    onClick = { viewModel.rollDice() },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(20.dp),
                    enabled = !uiState.isRolling && !uiState.showCardDialog && uiState.gameStatus != com.manus.juryumarble.domain.model.GameStatus.ENDED
                ) {
                    Icon(Icons.Default.Casino, null)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        if (uiState.isRolling) "굴리는 중..." else "주사위 굴리기",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Game Status Info
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoChip("플레이어: ${uiState.players.size}", NeonCyan)
                InfoChip("사용 카드: ${uiState.cardsUsed}", NeonPink)
            }

            Spacer(Modifier.height(8.dp))
        }

        // Card Dialog - Full Visual Upgrade
        if (uiState.showCardDialog && uiState.currentCard != null && !uiState.requiresPlayerSelection) {
            PremiumCardDialog(
                card = uiState.currentCard!!,
                penaltyText = uiState.scaledPenaltyText,
                onComplete = { viewModel.executeCard() },
                onSkip = { viewModel.dismissCardDialog() }
            )
        }

        // Player Selection Dialog
        if (uiState.requiresPlayerSelection) {
            PlayerSelectionDialog(
                players = uiState.players,
                allowedPlayerIndices = uiState.allowedPlayerIndices,
                onPlayerSelected = { selectedIndex ->
                    viewModel.executeCard(targetPlayerIndex = selectedIndex)
                },
                onDismiss = {
                    viewModel.dismissCardDialog()
                }
            )
        }

        // DDA Break Suggestion Dialog
        if (uiState.showDdaBreakSuggestion) {
            DDABreakSuggestionDialog(
                onTakeBreak = {
                    viewModel.dismissBreakSuggestion()
                    // Optional: Could add actual break timer or pause game
                },
                onContinue = {
                    viewModel.dismissBreakSuggestion()
                }
            )
        }

        // SnackbarHost for DDA messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) { data ->
            GlassCard(
                backgroundColor = NeonPurple.copy(0.9f),
                borderColor = NeonCyan,
                cornerRadius = 16.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumTile(index: Int, hasPlayer: Boolean) {
    val baseColor = when {
        index == 0 -> NeonGreen
        index % 5 == 0 -> NeonCyan
        index % 4 == 0 -> NeonGold
        index % 7 == 0 -> NeonRed
        else -> NeonBlue
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "tile")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (hasPlayer) baseColor.copy(0.3f) else Color.White.copy(0.05f))
            .border(
                width = if (hasPlayer) 3.dp else 1.dp,
                color = if (hasPlayer) baseColor else baseColor.copy(0.3f),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (hasPlayer) {
            // Player Token Glowing
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(baseColor, CircleShape)
                    .blur(if (hasPlayer) 8.dp else 0.dp)
            )
            Box(
                 modifier = Modifier
                    .size(16.dp)
                    .background(Color.White, CircleShape)
            )
        } else {
            Text(
                (index + 1).toString(),
                style = MaterialTheme.typography.labelLarge,
                color = baseColor.copy(alpha = 0.5f)
            )
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
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PremiumCardDialog(
    card: com.manus.juryumarble.domain.model.Card,
    penaltyText: String?,
    onComplete: () -> Unit,
    onSkip: () -> Unit
) {
    val cardColor = when (card.cardType) {
        CardType.MISSION -> NeonCyan
        CardType.PENALTY -> NeonRed
        CardType.RULE -> NeonPurple
        CardType.EVENT -> NeonGold
        CardType.SAFE -> NeonGreen
    }

    // Card flip and entrance animation
    val animatedRotation = remember { Animatable(90f) }
    val animatedAlpha = remember { Animatable(0f) }
    val animatedScale = remember { Animatable(0.7f) }

    LaunchedEffect(card.cardId) {
        launch {
            animatedRotation.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }
        launch {
            animatedAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
        }
        launch {
            animatedScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
    }

    AlertDialog(
        onDismissRequest = onSkip,
        containerColor = Color.Transparent, // We use our own GlassCard
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {},
        text = {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .graphicsLayer {
                        rotationY = animatedRotation.value
                        alpha = animatedAlpha.value
                        scaleX = animatedScale.value
                        scaleY = animatedScale.value
                        cameraDistance = 12f * density
                    },
                cornerRadius = 40.dp,
                blurIntensity = 24.dp,
                backgroundColor = RichBlack.copy(0.8f),
                borderColor = cardColor
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Surface(
                        color = cardColor.copy(0.2f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardColor)
                    ) {
                        Text(
                            card.cardType.name,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = cardColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    
                    Text(
                        card.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            shadow = Shadow(color = cardColor, offset = Offset(0f, 0f), blurRadius = 20f)
                        ),
                        color = Color.White
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Text(
                        card.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    
                    if (penaltyText != null) {
                         Spacer(Modifier.height(32.dp))
                         GlassCard(
                             backgroundColor = NeonRed.copy(0.1f),
                             borderColor = NeonRed,
                             cornerRadius = 20.dp
                         ) {
                             Text(
                                 "💀 $penaltyText",
                                 modifier = Modifier.padding(16.dp),
                                 style = MaterialTheme.typography.titleLarge,
                                 color = NeonRed,
                                 fontWeight = FontWeight.ExtraBold
                             )
                         }
                    }

                    Spacer(Modifier.height(40.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedButton(
                            onClick = onSkip,
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("SKIP", color = TextSecondary)
                        }
                        
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = cardColor),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("DONE", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PlayerSelectionDialog(
    players: List<String>,
    allowedPlayerIndices: List<Int>,
    onPlayerSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val playerColors = listOf(NeonPurple, NeonCyan, NeonPink, NeonGreen, NeonGold, NeonBlue)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {},
        text = {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                cornerRadius = 32.dp,
                backgroundColor = RichBlack.copy(0.95f),
                borderColor = NeonPurple
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "플레이어 선택",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "벌칙을 받을 플레이어를 선택하세요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(24.dp))

                    // 플레이어 버튼 리스트
                    allowedPlayerIndices.forEach { index ->
                        val color = playerColors[index % playerColors.size]

                        Button(
                            onClick = { onPlayerSelected(index) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = color.copy(0.3f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(0.2f))
                                    .border(2.dp, color, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    players.getOrNull(index)?.firstOrNull()?.toString() ?: "?",
                                    color = color,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Text(
                                players.getOrNull(index) ?: "Unknown",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("취소", color = TextSecondary)
                    }
                }
            }
        }
    )
}

/**
 * DDA Break Suggestion Dialog
 * 플레이어에게 휴식을 제안하는 다이얼로그
 */
@Composable
fun DDABreakSuggestionDialog(
    onTakeBreak: () -> Unit,
    onContinue: () -> Unit
) {
    // Entrance animation
    val animatedScale = remember { Animatable(0.8f) }
    val animatedAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            animatedScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            animatedAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )
        }
    }

    AlertDialog(
        onDismissRequest = onContinue,
        containerColor = Color.Transparent,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {},
        text = {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .graphicsLayer {
                        scaleX = animatedScale.value
                        scaleY = animatedScale.value
                        alpha = animatedAlpha.value
                    },
                cornerRadius = 32.dp,
                backgroundColor = RichBlack.copy(0.95f),
                borderColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(0.2f))
                            .border(3.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocalCafe,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        "잠시 휴식 어때요?",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "게임을 잠시 멈추고 물 한 잔 마시며 휴식을 취하세요.\n건강하게 즐기는 게임이 좋은 게임입니다!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(32.dp))

                    // Buttons
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onTakeBreak,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Check, null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "네, 휴식할게요",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onContinue,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("계속 플레이", color = TextSecondary)
                        }
                    }
                }
            }
        }
    )
}
