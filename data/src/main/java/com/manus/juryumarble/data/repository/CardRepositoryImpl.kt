package com.manus.juryumarble.data.repository

import com.google.gson.Gson
import com.manus.juryumarble.data.local.db.CardDao
import com.manus.juryumarble.data.local.db.CardPackDao
import com.manus.juryumarble.data.local.model.CardEntity
import com.manus.juryumarble.data.local.model.CardPackEntity
import com.manus.juryumarble.domain.model.*
import com.manus.juryumarble.domain.repository.CardRepository
import javax.inject.Inject

/**
 * CardRepository 구현체
 */
class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
    private val cardPackDao: CardPackDao
) : CardRepository {

    private val gson = Gson()
    
    override suspend fun getCardsByPack(packId: String): List<Card> {
        return cardDao.getCardsByPack(packId).map { it.toDomain() }
    }
    
    override suspend fun getAllCards(): List<Card> {
        return cardDao.getAllCards().map { it.toDomain() }
    }
    
    override suspend fun getCardsBySeverity(maxSeverity: Severity): List<Card> {
        val severities = when (maxSeverity) {
            Severity.MILD -> listOf("MILD")
            Severity.NORMAL -> listOf("MILD", "NORMAL")
            Severity.SPICY -> listOf("MILD", "NORMAL", "SPICY")
        }
        return cardDao.getCardsBySeverity(severities).map { it.toDomain() }
    }
    
    override suspend fun saveCustomCard(card: Card) {
        cardDao.insertCard(card.toEntity(isCustom = true))
    }
    
    override suspend fun deleteCustomCard(cardId: String) {
        cardDao.deleteCardById(cardId)
    }
    
    override suspend fun getCustomCards(): List<Card> {
        return cardDao.getCustomCards().map { it.toDomain() }
    }

    // CardPack 관련
    override suspend fun getAllCardPacks(): List<CardPack> {
        return cardPackDao.getAllCardPacks().map { it.toDomain() }
    }

    override suspend fun getEnabledCardPacks(): List<CardPack> {
        return cardPackDao.getEnabledCardPacks().map { it.toDomain() }
    }

    override suspend fun saveCardPack(cardPack: CardPack) {
        cardPackDao.insertCardPack(cardPack.toEntity())
    }

    override suspend fun updateCardPackEnabled(packId: String, enabled: Boolean) {
        cardPackDao.updateEnabled(packId, enabled)
    }

    // Entity <-> Domain 변환
    private fun CardEntity.toDomain(): Card {
        return Card(
            cardId = cardId,
            cardPackId = cardPackId,
            cardType = CardType.valueOf(cardType),
            targetType = TargetType.valueOf(targetType),
            title = title,
            description = description,
            severity = Severity.valueOf(severity),
            penaltyScale = penaltyScale,
            imageUrl = imageUrl,
            isCustom = isCustom
        )
    }

    private fun Card.toEntity(isCustom: Boolean = this.isCustom): CardEntity {
        return CardEntity(
            cardId = cardId,
            cardPackId = cardPackId,
            cardType = cardType.name,
            targetType = targetType.name,
            title = title,
            description = description,
            severity = severity.name,
            penaltyScale = penaltyScale,
            imageUrl = imageUrl,
            isCustom = isCustom
        )
    }

    private fun CardPackEntity.toDomain(): CardPack {
        val mapModifier = mapModifierJson?.let {
            try {
                gson.fromJson(it, MapModifier::class.java)
            } catch (e: Exception) {
                null
            }
        }

        return CardPack(
            packId = packId,
            name = name,
            description = description,
            theme = theme,
            imageUrl = imageUrl,
            thumbnailUrl = thumbnailUrl,
            cardCount = cardCount,
            isCustom = isCustom,
            isEnabled = isEnabled,
            isPremium = isPremium,
            price = price,
            mapModifier = mapModifier
        )
    }

    private fun CardPack.toEntity(): CardPackEntity {
        val mapModifierJson = mapModifier?.let {
            gson.toJson(it)
        }

        return CardPackEntity(
            packId = packId,
            name = name,
            description = description,
            theme = theme,
            imageUrl = imageUrl,
            thumbnailUrl = thumbnailUrl,
            cardCount = cardCount,
            isCustom = isCustom,
            isEnabled = isEnabled,
            isPremium = isPremium,
            price = price,
            mapModifierJson = mapModifierJson
        )
    }
}
