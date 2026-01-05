package com.manus.juryumarble.presentation.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.GameViewModel

/**
 * GameResultScreen - Premium Overhaul
 */
@Composable
fun GameResultScreen(
    onPlayAgain: () -> Unit,
    onGoHome: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val infiniteTransition = rememberInfiniteTransition(label = "result")
    val trophyScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "trophy"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedMeshBackground(primaryColor = NeonPurple, secondaryColor = NeonGold)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Trophy Neon Effect
            Box(contentAlignment = Alignment.Center) {
                 Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(trophyScale * 1.5f)
                        .background(NeonGold.copy(alpha = 0.15f), CircleShape)
                        .blur(30.dp)
                )
                Text(
                    text = "🏆",
                    fontSize = 100.sp,
                    modifier = Modifier.scale(trophyScale)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "GAME OVER",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 12.sp,
                ),
                color = Color.White
            )
            
            Text(
                text = "WELL PLAYED, EVERYONE!",
                style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 2.sp),
                color = NeonCyan
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Statistics Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = RichBlack.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SESSION STATS",
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                        color = NeonPurple,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(Icons.Default.People, "${uiState.players.size}", "PLAYERS", NeonCyan)
                        StatItem(Icons.Default.Style, "${uiState.cardsUsed}", "CARDS", NeonPink)
                        StatItem(Icons.Default.Casino, "${uiState.totalTurns}", "TURNS", NeonGold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Actions
            Button(
                onClick = {
                    viewModel.resetGame()
                    onPlayAgain()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.Replay, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("PLAY AGAIN", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = {
                    viewModel.resetGame()
                    onGoHome()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(NeonCyan, NeonBlue))),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("HOME", color = Color.White)
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Black
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}
