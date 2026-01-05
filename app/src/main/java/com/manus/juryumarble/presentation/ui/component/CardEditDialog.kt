package com.manus.juryumarble.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.manus.juryumarble.domain.model.*
import com.manus.juryumarble.presentation.ui.component.GlassCard
import com.manus.juryumarble.presentation.ui.theme.*

@Composable
fun CardEditDialog(
    card: Card?,
    onDismiss: () -> Unit,
    onSave: (String, String, CardType, TargetType, Severity, Float) -> Unit
) {
    var title by remember { mutableStateOf(card?.title ?: "") }
    var description by remember { mutableStateOf(card?.description ?: "") }
    var cardType by remember { mutableStateOf(card?.cardType ?: CardType.MISSION) }
    var targetType by remember { mutableStateOf(card?.targetType ?: TargetType.SELF) }
    var severity by remember { mutableStateOf(card?.severity ?: Severity.MILD) }
    var penaltyScale by remember { mutableStateOf(card?.penaltyScale ?: 1.0f) }

    val cardColor = when (cardType) {
        CardType.MISSION -> NeonCyan
        CardType.PENALTY -> NeonRed
        CardType.RULE -> NeonPurple
        CardType.EVENT -> NeonGold
        CardType.SAFE -> NeonGreen
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        containerColor = Color.Transparent,
        confirmButton = {},
        text = {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f),
                cornerRadius = 24.dp,
                backgroundColor = RichBlack.copy(0.95f),
                borderColor = cardColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Title
                    Text(
                        if (card == null) "새 카드 만들기" else "카드 수정",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(24.dp))

                    // Card Title Input
                    Text("카드 제목", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("예: 원샷", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = cardColor,
                            unfocusedBorderColor = TextSecondary,
                            cursorColor = cardColor
                        ),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // Description Input
                    Text("카드 설명", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("예: 깔끔하게 원샷!", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = cardColor,
                            unfocusedBorderColor = TextSecondary,
                            cursorColor = cardColor
                        ),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(Modifier.height(16.dp))

                    // Card Type Selector
                    Text("카드 타입", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CardType.values().forEach { type ->
                            val isSelected = cardType == type
                            val typeColor = when (type) {
                                CardType.MISSION -> NeonCyan
                                CardType.PENALTY -> NeonRed
                                CardType.RULE -> NeonPurple
                                CardType.EVENT -> NeonGold
                                CardType.SAFE -> NeonGreen
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) typeColor.copy(0.3f) else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) typeColor else TextSecondary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { cardType = type }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    type.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) typeColor else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Target Type Selector
                    Text("대상 타입", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TargetType.values().forEach { type ->
                            val isSelected = targetType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) cardColor.copy(0.3f) else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) cardColor else TextSecondary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { targetType = type }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    type.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) cardColor else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Severity Selector
                    Text("수위", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Severity.values().forEach { sev ->
                            val isSelected = severity == sev
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) cardColor.copy(0.3f) else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) cardColor else TextSecondary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { severity = sev }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    sev.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) cardColor else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Penalty Scale Slider
                    Text("벌칙 배율: ${penaltyScale}x", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                    Slider(
                        value = penaltyScale,
                        onValueChange = { penaltyScale = it },
                        valueRange = 0f..3f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = cardColor,
                            activeTrackColor = cardColor,
                            inactiveTrackColor = TextSecondary
                        )
                    )

                    Spacer(Modifier.height(32.dp))

                    // Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("취소", color = TextSecondary)
                        }

                        Button(
                            onClick = {
                                if (title.isNotBlank() && description.isNotBlank()) {
                                    onSave(title, description, cardType, targetType, severity, penaltyScale)
                                }
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = cardColor),
                            shape = RoundedCornerShape(16.dp),
                            enabled = title.isNotBlank() && description.isNotBlank()
                        ) {
                            Text(
                                if (card == null) "생성" else "저장",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    )
}
