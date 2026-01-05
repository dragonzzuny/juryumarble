package com.manus.juryumarble.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// Premium Neon Palette
val NeonPurple = Color(0xFFBC13FE)
val NeonPink = Color(0xFFFF00AB)
val NeonCyan = Color(0xFF00F3FF)
val NeonBlue = Color(0xFF1B00FF)
val NeonGold = Color(0xFFFFD700)
val NeonGreen = Color(0xFF39FF14)
val NeonRed = Color(0xFFFF3131)

// Rich Backgrounds
val DeepSpace = Color(0xFF0F0C29)
val DeepNavy = Color(0xFF1E1B4B)
val RichBlack = Color(0xFF020617)

// Surface Colors (Glassmorphism base)
val GlassBase = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.1f)
val SurfaceCard = Color(0xFF1E293B)  // Card/Dice surface color
val SurfaceDark = RichBlack  // Dark surface color for theme

// Adaptive Semantic Colors
val GamePrimary = NeonPurple
val GamePrimaryDark = Color(0xFF7B1FA2)
val GameSecondary = NeonCyan
val GameAccent = NeonPink

// Card Colors by Severity (Vibrant)
val CardMild = Color(0xFF22C55E)     // Vibrant Green
val CardNormal = Color(0xFFEAB308)   // Vibrant Yellow/Gold
val CardSpicy = Color(0xFFEF4444)    // Vibrant Red

// Card Type Colors (Neon)
val CardMission = NeonCyan
val CardPenalty = NeonRed
val CardRule = NeonPurple
val CardEvent = NeonGold
val CardSafe = NeonGreen

// Board Colors
val BoardBackground = RichBlack
val TileStart = NeonGreen
val TileCard = NeonBlue
val TileEvent = NeonGold
val TileSafe = Color(0xFF4ADE80)
val TileTrap = NeonRed

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextTitle = Color(0xFFFFFFFF)

// Light theme colors (fallback)
val Purple40 = Color(0xFF9C27B0)
val PurpleGrey40 = Color(0xFF7E57C2)
val Pink40 = Color(0xFFE91E63)
