package com.manus.juryumarble.domain.repository

import com.manus.juryumarble.domain.model.Card
import com.manus.juryumarble.domain.model.CardPack
import com.manus.juryumarble.domain.model.Severity

/**
 * 카드 데이터 접근 Repository 인터페이스
 */
interface CardRepository {
    /**
     * 카드팩 ID로 카드 목록 조회
     */
    suspend fun getCardsByPack(packId: String): List<Card>

    /**
     * 모든 카드 조회
     */
    suspend fun getAllCards(): List<Card>

    /**
     * 수위 필터링된 카드 목록 조회
     */
    suspend fun getCardsBySeverity(maxSeverity: Severity): List<Card>

    /**
     * 커스텀 카드 저장
     */
    suspend fun saveCustomCard(card: Card)

    /**
     * 커스텀 카드 삭제
     */
    suspend fun deleteCustomCard(cardId: String)

    /**
     * 커스텀 카드 목록 조회
     */
    suspend fun getCustomCards(): List<Card>

    // CardPack 관련
    /**
     * 모든 카드팩 조회
     */
    suspend fun getAllCardPacks(): List<CardPack>

    /**
     * 활성화된 카드팩만 조회
     */
    suspend fun getEnabledCardPacks(): List<CardPack>

    /**
     * 카드팩 저장
     */
    suspend fun saveCardPack(cardPack: CardPack)

    /**
     * 카드팩 활성화/비활성화
     */
    suspend fun updateCardPackEnabled(packId: String, enabled: Boolean)
}
