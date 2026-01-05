package com.manus.juryumarble.presentation.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.manus.juryumarble.presentation.ui.theme.DeepSpace
import com.manus.juryumarble.presentation.ui.theme.RichBlack
import kotlin.math.sin

/**
 * Dynamic Mesh Gradient Background
 * Creates a flowing, luxury background effect.
 */
@Composable
fun AnimatedMeshBackground(
    primaryColor: Color,
    secondaryColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Base background
        drawRect(RichBlack)

        // Animated gradient blobs
        val blob1X = width * (0.5f + 0.3f * sin(time))
        val blob1Y = height * (0.3f + 0.2f * sin(time * 0.7f))
        
        val blob2X = width * (0.2f + 0.2f * sin(time * 1.2f))
        val blob2Y = height * (0.7f + 0.1f * sin(time * 0.5f))

        val blob3X = width * (0.8f + 0.1f * sin(time * 0.9f))
        val blob3Y = height * (0.6f + 0.3f * sin(time * 1.5f))

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
                center = Offset(blob1X, blob1Y),
                radius = width * 0.8f
            ),
            center = Offset(blob1X, blob1Y),
            radius = width * 0.8f
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(secondaryColor.copy(alpha = 0.2f), Color.Transparent),
                center = Offset(blob2X, blob2Y),
                radius = width * 0.7f
            ),
            center = Offset(blob2X, blob2Y),
            radius = width * 0.7f
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primaryColor.copy(alpha = 0.15f), Color.Transparent),
                center = Offset(blob3X, blob3Y),
                radius = width * 0.6f
            ),
            center = Offset(blob3X, blob3Y),
            radius = width * 0.6f
        )
    }
}
