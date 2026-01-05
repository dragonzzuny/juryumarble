package com.manus.juryumarble.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity for Card
 */
@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey
    val cardId: String,
    val cardPackId: String,
    val cardType: String,
    val targetType: String,
    val title: String,
    val description: String,
    val severity: String,
    val penaltyScale: Float,
    val imageUrl: String? = null,
    val isCustom: Boolean = false
)
