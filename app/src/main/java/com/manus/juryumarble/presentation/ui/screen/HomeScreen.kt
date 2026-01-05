package com.manus.juryumarble.presentation.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manus.juryumarble.presentation.ui.component.AnimatedMeshBackground
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*
import com.manus.juryumarble.presentation.viewmodel.GameViewModel

/**
 * HomeScreen - Visual Upgrade
 */
@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onLoadGame: () -> Unit = {},
    onSettings: () -> Unit = {},
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showComingSoon by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "home")
    
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Dynamic Mesh Background
        AnimatedMeshBackground(
            primaryColor = NeonPurple,
            secondaryColor = NeonBlue
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Logo Section
            Box(
                modifier = Modifier
                    .offset(y = floatAnim.dp)
                    .size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scaleAnim * 1.5f)
                        .background(NeonPurple.copy(alpha = 0.2f), CircleShape)
                        .blur(40.dp)
                )
                
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scaleAnim),
                    tint = NeonPurple
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Title with Shader/Gradient looks
            Text(
                text = "주류마블",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 8.sp,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, NeonPurple)
                    )
                )
            )
            
            Text(
                text = "PREMIUM PARTY GAME",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Light
                ),
                color = NeonCyan
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            // Main Glass Action UI
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Continue Game button (only shown if saved game exists)
                    if (uiState.hasSavedGame) {
                        Button(
                            onClick = {
                                viewModel.loadSavedGame()
                                onLoadGame()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .shadow(24.dp, RoundedCornerShape(20.dp), spotColor = NeonCyan),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(Icons.Default.Restore, null)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "저장된 게임 이어하기",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Button(
                        onClick = onStartGame,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(24.dp, RoundedCornerShape(20.dp), spotColor = NeonPurple),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonPurple
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, null)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "새 게임 시작",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { showComingSoon = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                             brush = Brush.linearGradient(listOf(NeonCyan, NeonPink))
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Settings, null)
                        Spacer(Modifier.width(8.dp))
                        Text("설정 및 커스텀")
                    }
                }
            }
        }

        // Coming Soon Dialog
        if (showComingSoon) {
            AlertDialog(
                onDismissRequest = { showComingSoon = false },
                title = { Text("Coming Soon") },
                text = { Text("설정 및 커스텀 기능은 곧 추가될 예정입니다!") },
                confirmButton = {
                    Button(onClick = { showComingSoon = false }) {
                        Text("확인")
                    }
                }
            )
        }

        Text(
            text = "DESIGNED BY ANTIGRAVITY v2.0",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
            color = Color.White.copy(alpha = 0.3f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        )
    }
}
