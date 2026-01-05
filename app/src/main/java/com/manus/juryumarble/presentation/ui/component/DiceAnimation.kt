package com.manus.juryumarble.presentation.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manus.juryumarble.domain.model.DiceResult
import com.manus.juryumarble.presentation.ui.theme.*
import kotlinx.coroutines.delay

/**
 * 주사위 굴리기 애니메이션 컴포넌트
 * 3D 회전 효과와 바운스 애니메이션 포함
 */
@Composable
fun DiceRollingAnimation(
    isRolling: Boolean,
    diceResult: DiceResult?,
    modifier: Modifier = Modifier
) {
    var displayValue1 by remember { mutableIntStateOf(1) }
    var displayValue2 by remember { mutableIntStateOf(1) }
    
    // 굴리는 중 랜덤 숫자 표시
    LaunchedEffect(isRolling) {
        if (isRolling) {
            while (true) {
                displayValue1 = (1..6).random()
                displayValue2 = (1..6).random()
                delay(80)
            }
        }
    }
    
    // 결과값 설정
    LaunchedEffect(diceResult) {
        diceResult?.let {
            displayValue1 = it.dice1
            displayValue2 = it.dice2 ?: it.dice1
        }
    }
    
    // 회전 애니메이션
    val infiniteTransition = rememberInfiniteTransition(label = "dice_spin")
    
    val rotationX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationX"
    )
    
    val rotationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationY"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // 결과 표시 시 바운스 애니메이션
    val resultScale by animateFloatAsState(
        targetValue = if (!isRolling && diceResult != null) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "resultScale"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isRolling) {
            // 굴리는 중 - 2개의 주사위 애니메이션
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 주사위 1
                DiceFace(
                    value = displayValue1,
                    modifier = Modifier
                        .graphicsLayer {
                            this.rotationX = rotationX
                            this.rotationY = rotationY
                            this.scaleX = scale
                            this.scaleY = scale
                        }
                )
                
                // 주사위 2
                DiceFace(
                    value = displayValue2,
                    modifier = Modifier
                        .graphicsLayer {
                            this.rotationX = -rotationX
                            this.rotationY = rotationY + 180f
                            this.scaleX = scale
                            this.scaleY = scale
                        }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "🎲 굴리는 중...",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
        } else if (diceResult != null) {
            // 결과 표시
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.scale(resultScale)
            ) {
                DiceFace(
                    value = diceResult.dice1,
                    isResult = true
                )
                
                diceResult.dice2?.let { dice2 ->
                    DiceFace(
                        value = dice2,
                        isResult = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 합계
            Surface(
                color = GamePrimary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.scale(resultScale)
            ) {
                Text(
                    text = if (diceResult.dice2 != null) {
                        "${diceResult.dice1} + ${diceResult.dice2} = ${diceResult.total}"
                    } else {
                        "합계: ${diceResult.total}"
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = GamePrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // 더블 표시
            if (diceResult.isDouble) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = GameSecondary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.scale(resultScale)
                ) {
                    Text(
                        text = "🎉 더블! 한 번 더!",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = GameSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 개별 주사위 면 표시
 */
@Composable
fun DiceFace(
    value: Int,
    modifier: Modifier = Modifier,
    isResult: Boolean = false
) {
    val backgroundColor = if (isResult) GamePrimary else SurfaceCard
    val dotColor = if (isResult) Color.White else TextPrimary
    val size = if (isResult) 72.dp else 64.dp
    
    Surface(
        modifier = modifier.size(size),
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = if (isResult) 8.dp else 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            when (value) {
                1 -> DiceDots1(dotColor)
                2 -> DiceDots2(dotColor)
                3 -> DiceDots3(dotColor)
                4 -> DiceDots4(dotColor)
                5 -> DiceDots5(dotColor)
                6 -> DiceDots6(dotColor)
            }
        }
    }
}

@Composable
private fun DiceDot(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(10.dp)
            .background(color, shape = androidx.compose.foundation.shape.CircleShape)
    )
}

@Composable
private fun DiceDots1(color: Color) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DiceDot(color)
    }
}

@Composable
private fun DiceDots2(color: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        DiceDot(color, Modifier.align(Alignment.TopEnd))
        DiceDot(color, Modifier.align(Alignment.BottomStart))
    }
}

@Composable
private fun DiceDots3(color: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        DiceDot(color, Modifier.align(Alignment.TopEnd))
        DiceDot(color, Modifier.align(Alignment.Center))
        DiceDot(color, Modifier.align(Alignment.BottomStart))
    }
}

@Composable
private fun DiceDots4(color: Color) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DiceDot(color)
            DiceDot(color)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DiceDot(color)
            DiceDot(color)
        }
    }
}

@Composable
private fun DiceDots5(color: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DiceDot(color)
                DiceDot(color)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DiceDot(color)
                DiceDot(color)
            }
        }
        DiceDot(color, Modifier.align(Alignment.Center))
    }
}

@Composable
private fun DiceDots6(color: Color) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DiceDot(color)
            DiceDot(color)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DiceDot(color)
            DiceDot(color)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DiceDot(color)
            DiceDot(color)
        }
    }
}
