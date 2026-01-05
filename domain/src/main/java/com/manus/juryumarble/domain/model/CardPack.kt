package com.manus.juryumarble.domain.model

/**
 * 카드팩 데이터 모델
 *
 * 핵심 개념:
 * - 카드팩은 단순 카드 묶음이 아니라 "게임 경험을 변경하는 모듈"
 * - 카드팩 선택에 따라 맵이 동적으로 재구성됨
 * - Seed 기반 재현 가능한 랜덤 생성
 */
data class CardPack(
    val packId: String,
    val name: String,
    val description: String,
    val theme: String,
    val imageUrl: String? = null,
    val thumbnailUrl: String? = null,
    val cardCount: Int = 0,
    val isCustom: Boolean = false,
    val isEnabled: Boolean = true,
    val isPremium: Boolean = false,           // 유료 카드팩 여부
    val price: Double = 0.0,                  // 가격 (원화 기준)
    val mapModifier: MapModifier? = null      // 맵 수정자 (동적 맵 생성용)
)
