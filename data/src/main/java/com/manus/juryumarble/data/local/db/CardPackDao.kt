package com.manus.juryumarble.data.local.db

import androidx.room.*
import com.manus.juryumarble.data.local.model.CardPackEntity

/**
 * CardPack DAO interface
 */
@Dao
interface CardPackDao {

    @Query("SELECT * FROM card_packs")
    suspend fun getAllCardPacks(): List<CardPackEntity>

    @Query("SELECT * FROM card_packs WHERE isEnabled = 1")
    suspend fun getEnabledCardPacks(): List<CardPackEntity>

    @Query("SELECT * FROM card_packs WHERE packId = :packId")
    suspend fun getCardPackById(packId: String): CardPackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardPack(cardPack: CardPackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardPacks(cardPacks: List<CardPackEntity>)

    @Query("UPDATE card_packs SET isEnabled = :enabled WHERE packId = :packId")
    suspend fun updateEnabled(packId: String, enabled: Boolean)

    @Delete
    suspend fun deleteCardPack(cardPack: CardPackEntity)

    @Query("DELETE FROM card_packs WHERE packId = :packId")
    suspend fun deleteCardPackById(packId: String)
}
