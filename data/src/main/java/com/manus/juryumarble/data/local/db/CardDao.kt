package com.manus.juryumarble.data.local.db

import androidx.room.*
import com.manus.juryumarble.data.local.model.CardEntity

/**
 * Card DAO interface
 */
@Dao
interface CardDao {
    
    @Query("SELECT * FROM cards WHERE cardPackId = :packId")
    suspend fun getCardsByPack(packId: String): List<CardEntity>
    
    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<CardEntity>
    
    @Query("SELECT * FROM cards WHERE severity IN (:severities)")
    suspend fun getCardsBySeverity(severities: List<String>): List<CardEntity>
    
    @Query("SELECT * FROM cards WHERE isCustom = 1")
    suspend fun getCustomCards(): List<CardEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)
    
    @Delete
    suspend fun deleteCard(card: CardEntity)
    
    @Query("DELETE FROM cards WHERE cardId = :cardId")
    suspend fun deleteCardById(cardId: String)
    
    @Query("SELECT COUNT(*) FROM cards")
    suspend fun getCardCount(): Int
}
