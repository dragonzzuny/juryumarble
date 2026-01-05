package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.*
import com.manus.juryumarble.domain.repository.CardRepository

/**
 * 게임 초기화 UseCase (DMMS 통합 버전)
 *
 * 주요 기능:
 * 1. 선택된 카드팩에 따라 동적 맵 생성 (DMMS)
 * 2. 플레이어 초기화
 * 3. 덱 구성 (수위 필터링 적용)
 * 4. DDA 초기화
 * 5. 안전 설정 적용
 */
class InitializeGameUseCase(
    private val cardRepository: CardRepository,
    private val generateMapUseCase: GenerateMapUseCase
) {

    /**
     * 게임 초기화
     */
    suspend operator fun invoke(config: SessionConfig): GameState {
        // 1. 카드팩 로드 및 검증
        val activatedCardPacks = loadActivatedCardPacks(config.activatedCardPackIds)

        // 2. DMMS를 통한 동적 맵 생성
        val seed = System.currentTimeMillis()
        val baseTemplate = createBaseMapTemplate()
        val generatedMap = generateMapUseCase.execute(baseTemplate, activatedCardPacks, seed)

        // 3. 플레이어 초기화
        val players = config.playerNames.mapIndexed { index, name ->
            Player(
                id = "player_$index",
                nickname = name,
                position = 0,
                penaltyCount = 0,
                consecutivePenalties = 0,
                isActive = true
            )
        }

        // 4. 덱 구성 (수위 필터링 + 카드팩 조합)
        val deck = buildDeck(activatedCardPacks, config)

        // 5. GameState 생성
        return GameState(
            sessionId = "session_$seed",
            randomSeed = seed,
            players = players,
            board = generatedMap.tiles,
            deck = deck.shuffled(),
            currentPlayerIndex = 0,
            currentTurn = 1,
            turnPhase = TurnPhase.START,
            status = GameStatus.IN_PROGRESS,
            direction = GameDirection.CLOCKWISE,
            startTime = System.currentTimeMillis()
        )
    }

    /**
     * 활성화된 카드팩 로드
     */
    private suspend fun loadActivatedCardPacks(packIds: List<String>): List<CardPack> {
        return try {
            cardRepository.getEnabledCardPacks().filter { it.packId in packIds }
        } catch (e: Exception) {
            // 카드팩 로드 실패 시 기본 카드팩 사용
            emptyList()
        }
    }

    /**
     * 기본 맵 템플릿 생성
     */
    private fun createBaseMapTemplate(): MapTemplate {
        val fixedTiles = listOf(
            Tile(0, TileType.START, "출발", "게임 시작!")
        )

        return MapTemplate(
            templateId = "default",
            name = "기본 맵",
            description = "주류마블 기본 게임 보드",
            boardSize = 16,
            fixedTiles = fixedTiles,
            variableSlots = 15,
            defaultTileWeights = TileWeightModifier(
                cardWeight = 0.4f,
                eventWeight = 0.2f,
                safeWeight = 0.2f,
                trapWeight = 0.2f
            )
        )
    }

    /**
     * 덱 구성 (카드팩 + 수위 필터)
     */
    private suspend fun buildDeck(cardPacks: List<CardPack>, config: SessionConfig): List<Card> {
        val allCards = mutableListOf<Card>()

        // 활성화된 카드팩의 카드 로드
        cardPacks.forEach { pack ->
            val packCards = cardRepository.getCardsByPack(pack.packId)
            allCards.addAll(packCards)
        }

        // 기본 카드팩이 없으면 샘플 카드 생성
        if (allCards.isEmpty()) {
            allCards.addAll(createSampleCards())
        }

        // 수위 필터링
        val filteredCards = allCards.filter { card ->
            when (config.severityFilter) {
                Severity.MILD -> card.severity == Severity.MILD
                Severity.NORMAL -> card.severity in listOf(Severity.MILD, Severity.NORMAL)
                Severity.SPICY -> true
            }
        }

        // 커스텀 카드 추가
        val finalDeck = filteredCards + config.customCards

        return if (finalDeck.isEmpty()) createSampleCards() else finalDeck
    }

    /**
     * 샘플 카드 생성 (카드팩 없을 때)
     */
    private fun createSampleCards(): List<Card> {
        return listOf(
            Card("default_penalty_normal_001", "default", CardType.PENALTY, TargetType.SELF, "원샷", "깔끔하게 원샷!", Severity.NORMAL, 1.0f),
            Card("default_mission_mild_001", "default", CardType.MISSION, TargetType.SELF, "자기소개 타임", "자신을 3가지 키워드로 소개하세요.", Severity.MILD, 1.0f),
            Card("default_safe_mild_001", "default", CardType.SAFE, TargetType.SELF, "벌칙 면제권", "이번 턴의 모든 벌칙에서 해방!", Severity.MILD, 0.0f),
            Card("default_mission_normal_001", "default", CardType.MISSION, TargetType.SELF, "밸런스 게임", "선택 후 이유 설명하기", Severity.NORMAL, 1.0f),
            Card("default_rule_mild_001", "default", CardType.RULE, TargetType.ALL, "금지어 게임", "다음 턴까지 '나' 사용 금지!", Severity.MILD, 1.0f),
            Card("default_penalty_mild_001", "default", CardType.PENALTY, TargetType.SELF, "물 한잔 원샷", "건강을 위해 물 한잔!", Severity.MILD, 0.0f),
            Card("default_event_mild_001", "default", CardType.EVENT, TargetType.ALL, "방향 전환", "게임 진행 방향이 바뀝니다!", Severity.MILD, 0.0f),
            Card("default_penalty_normal_002", "default", CardType.PENALTY, TargetType.TARGET_ONE, "너, 마셔라", "한 명을 지목해 1샷 선물!", Severity.NORMAL, 1.0f),
            Card("default_mission_mild_003", "default", CardType.MISSION, TargetType.ALL, "건배사 제의", "멋진 건배사를 외치세요!", Severity.MILD, 1.0f),
            Card("default_penalty_normal_003", "default", CardType.PENALTY, TargetType.SELF, "러브샷", "왼쪽 사람과 러브샷!", Severity.NORMAL, 1.0f)
        )
    }
}
