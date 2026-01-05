package com.manus.juryumarble.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity for CardPack
 */
@Entity(tableName = "card_packs")
data class CardPackEntity(
    @PrimaryKey
    val packId: String,
    val name: String,
    val description: String,
    val theme: String,
    val imageUrl: String? = null,
    val thumbnailUrl: String? = null,
    val cardCount: Int = 0,
    val isCustom: Boolean = false,
    val isEnabled: Boolean = true,
    val isPremium: Boolean = false,
    val price: Double = 0.0,
    val mapModifierJson: String? = null  // MapModifier를 JSON으로 저장
)
