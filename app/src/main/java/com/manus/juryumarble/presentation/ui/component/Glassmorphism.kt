package com.manus.juryumarble.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manus.juryumarble.presentation.ui.theme.GlassBase
import com.manus.juryumarble.presentation.ui.theme.GlassBorder

/**
 * Glassmorphism Container Component
 * Provides a frosted glass effect with a subtle border.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    blurIntensity: Dp = 12.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = GlassBorder,
    backgroundColor: Color = GlassBase,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = backgroundColor.alpha * 0.5f)
                    )
                )
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor,
                        borderColor.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        // Blur background (Note: In Compose, real-time background blur is limited on some versions/platforms)
        // This provides the visual container; actual blur might require specific RenderEffect on Android 12+
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurIntensity)
        )
        
        Box(modifier = Modifier.padding(1.dp)) {
            content()
        }
    }
}
