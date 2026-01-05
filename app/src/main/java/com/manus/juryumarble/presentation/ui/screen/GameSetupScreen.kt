package com.manus.juryumarble.presentation.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manus.juryumarble.domain.model.Severity
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.delay

/**
 * GameSetupScreen - Visual Upgrade
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    onStartGame: () -> Unit,
    onBack: () -> Unit,
    onNavigateToCustomCards: () -> Unit = {},
    onNavigateToCardPacks: () -> Unit = {},
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var newPlayerName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Navigate when game starts successfully
    LaunchedEffect(uiState.isGameStarted) {
        Log.d("GameSetupScreen", "isGameStarted changed to: ${uiState.isGameStarted}")
        if (uiState.isGameStarted) {
            Log.d("GameSetupScreen", "✅ Navigating to game board...")
            delay(50) // 약간의 딜레이로 UI 안정화
            onStartGame()
        }
    }

    val severityOptions = listOf(
        Pair("순한맛 🌱", Severity.MILD),
        Pair("보통 🔥", Severity.NORMAL),
        Pair("매운맛 🌶️", Severity.SPICY)
    )

    val playerColors = listOf(NeonPurple, NeonCyan, NeonPink, NeonGreen, NeonGold, NeonBlue)

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedMeshBackground(primaryColor = DeepNavy, secondaryColor = NeonPink)

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("GAME SETUP", style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Bold)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToCardPacks) {
                            Icon(Icons.Default.AccountBox, "카드팩", tint = NeonPurple)
                        }
                        IconButton(onClick = onNavigateToCustomCards) {
                            Icon(Icons.Default.Edit, "커스텀 카드", tint = NeonCyan)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                
                // Player Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, null, tint = NeonCyan)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "PLAYERS",
                        style = MaterialTheme.typography.titleSmall,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Player List with Glass Effect
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(uiState.players) { index, player ->
                        PremiumPlayerCard(
                            name = player,
                            color = playerColors[index % playerColors.size],
                            onRemove = { viewModel.removePlayer(index) },
                            canRemove = uiState.players.size > 2
                        )
                    }
                    
                    if (uiState.players.size < 6) {
                        item {
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                TextField(
                                    value = newPlayerName,
                                    onValueChange = { newPlayerName = it },
                                    placeholder = { Text("닉네임 입력...", color = Color.White.copy(0.4f)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = NeonCyan,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            if (newPlayerName.isNotBlank()) {
                                                viewModel.addPlayer(newPlayerName)
                                                newPlayerName = ""
                                                focusManager.clearFocus()
                                            }
                                        }) {
                                            Icon(Icons.Default.AddCircle, null, tint = NeonCyan, modifier = Modifier.size(32.dp))
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                         if (newPlayerName.isNotBlank()) {
                                            viewModel.addPlayer(newPlayerName)
                                            newPlayerName = ""
                                            focusManager.clearFocus()
                                        }
                                    })
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Severity Section
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                         Text(
                            "GAME INTENSITY",
                             style = MaterialTheme.typography.titleSmall,
                             color = NeonPink,
                             fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            severityOptions.forEach { (label, severity) ->
                                val isSelected = uiState.selectedSeverity == severity
                                val neonColor = when(severity) {
                                    Severity.MILD -> NeonGreen
                                    Severity.NORMAL -> NeonGold
                                    Severity.SPICY -> NeonRed
                                }
                                
                                Button(
                                    onClick = { viewModel.setSeverity(severity) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) neonColor else Color.White.copy(0.1f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        viewModel.startGame()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(16.dp),
                    enabled = uiState.players.size >= 2
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(8.dp))
                    Text("게임 시작", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PremiumPlayerCard(
    name: String,
    color: Color,
    onRemove: () -> Unit,
    canRemove: Boolean
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(0.2f))
                    .border(2.dp, color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                 Text(name.first().toString(), color = color, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(name, color = Color.White, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            
            if (canRemove) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, null, tint = Color.White.copy(0.5f))
                }
            }
        }
    }
}
