package com.manus.juryumarble.domain.usecase

import com.manus.juryumarble.domain.model.*
import javax.inject.Inject
import kotlin.random.Random

/**
 * 맵 생성 UseCase
 *
 * Dynamic Map Modifier System (DMMS)의 핵심 로직
 * - 선택된 카드팩에 따라 맵을 동적으로 생성
 * - Seed 기반 재현 가능한 랜덤 생성
 * - 타일 가중치, 룰 변경, 이벤트 확률을 종합적으로 적용
 */
class GenerateMapUseCase @Inject constructor() {

    /**
     * 게임 맵 생성
     *
     * @param baseTemplate 기본 맵 템플릿
     * @param activatedCardPacks 활성화된 카드팩 목록
     * @param seed 재현 가능한 랜덤 시드
     * @return 생성된 맵
     */
    fun execute(
        baseTemplate: MapTemplate,
        activatedCardPacks: List<CardPack>,
        seed: Long
    ): GeneratedMap {
        // 1. 카드팩 Modifier 합산
        val combinedModifier = combineModifiers(
            baseTemplate.defaultTileWeights,
            activatedCardPacks
        )

        // 2. Seed 생성 (카드팩 영향 반영)
        val finalSeed = calculateFinalSeed(seed, combinedModifier.seedBias, activatedCardPacks)

        // 3. 타일 생성
        val generatedTiles = generateTiles(
            baseTemplate,
            combinedModifier.tileWeights,
            Random(finalSeed)
        )

        // 4. 특별 규칙 생성
        val specialRules = generateSpecialRules(combinedModifier.rules, activatedCardPacks)

        return GeneratedMap(
            sessionId = "session_$finalSeed",
            seed = finalSeed,
            activatedCardPacks = activatedCardPacks.map { it.packId },
            tiles = generatedTiles,
            appliedModifiers = combinedModifier,
            specialRules = specialRules
        )
    }

    /**
     * 카드팩 Modifier 합산
     */
    private fun combineModifiers(
        baseWeights: TileWeightModifier,
        cardPacks: List<CardPack>
    ): MapModifier {
        var tileWeights = baseWeights
        var rules = RuleModifier()
        var events = EventModifier()
        var seedBias = SeedBias()

        cardPacks.forEach { pack ->
            pack.mapModifier?.let { modifier ->
                // 타일 가중치 합산 (곱셈)
                tileWeights = TileWeightModifier(
                    cardWeight = tileWeights.cardWeight * modifier.tileWeights.cardWeight,
                    eventWeight = tileWeights.eventWeight * modifier.tileWeights.eventWeight,
                    safeWeight = tileWeights.safeWeight * modifier.tileWeights.safeWeight,
                    trapWeight = tileWeights.trapWeight * modifier.tileWeights.trapWeight,
                    forceIncludeTiles = tileWeights.forceIncludeTiles + modifier.tileWeights.forceIncludeTiles
                )

                // 룰 합산 (최대값 또는 평균)
                rules = mergeRules(rules, modifier.rules)

                // 이벤트 합산
                events = mergeEvents(events, modifier.events)

                // Seed Bias 합산
                seedBias = SeedBias(
                    biasValue = seedBias.biasValue + modifier.seedBias.biasValue,
                    randomnessLevel = (seedBias.randomnessLevel + modifier.seedBias.randomnessLevel) / 2
                )
            }
        }

        return MapModifier(tileWeights, rules, events, seedBias)
    }

    /**
     * 최종 시드 계산
     */
    private fun calculateFinalSeed(
        baseSeed: Long,
        seedBias: SeedBias,
        cardPacks: List<CardPack>
    ): Long {
        var finalSeed = baseSeed + seedBias.biasValue

        // 카드팩 ID 해시를 시드에 반영 (같은 카드팩 조합은 같은 시드)
        val packHash = cardPacks.joinToString("|") { it.packId }.hashCode().toLong()
        finalSeed += packHash

        // 랜덤성 레벨 적용 (0.0 = 고정, 1.0 = 완전 랜덤)
        if (seedBias.randomnessLevel > 0.0f) {
            val randomComponent = Random(System.currentTimeMillis()).nextLong()
            finalSeed += (randomComponent * seedBias.randomnessLevel).toLong()
        }

        return finalSeed
    }

    /**
     * 타일 생성 (가중치 기반 확률적 배치)
     */
    private fun generateTiles(
        template: MapTemplate,
        weights: TileWeightModifier,
        random: Random
    ): List<Tile> {
        val tiles = mutableListOf<Tile>()

        // 1. 고정 타일 배치
        tiles.addAll(template.fixedTiles)

        // 2. 강제 포함 타일 배치
        weights.forceIncludeTiles.forEach { tileTemplate ->
            val position = if (tileTemplate.position == -1) {
                // 랜덤 위치에 배치
                random.nextInt(template.boardSize)
            } else {
                tileTemplate.position
            }

            tiles.add(
                Tile(
                    position,
                    tileTemplate.type,
                    tileTemplate.title,
                    tileTemplate.description
                )
            )
        }

        // 3. 나머지 가변 슬롯 채우기
        val occupiedPositions = tiles.map { it.position }.toSet()
        val availablePositions = (0 until template.boardSize).filter { it !in occupiedPositions }

        availablePositions.forEach { position ->
            val tileType = selectTileType(weights, random)
            tiles.add(
                Tile(
                    position,
                    tileType,
                    getTileTitle(tileType),
                    getTileDescription(tileType)
                )
            )
        }

        return tiles.sortedBy { it.position }
    }

    /**
     * 가중치 기반 타일 타입 선택
     */
    private fun selectTileType(weights: TileWeightModifier, random: Random): TileType {
        val totalWeight = weights.cardWeight + weights.eventWeight + weights.safeWeight + weights.trapWeight
        val randomValue = random.nextFloat() * totalWeight

        var cumulativeWeight = 0f
        return when {
            randomValue < (cumulativeWeight + weights.cardWeight).also { cumulativeWeight = it } -> TileType.CARD
            randomValue < (cumulativeWeight + weights.eventWeight).also { cumulativeWeight = it } -> TileType.EVENT
            randomValue < (cumulativeWeight + weights.safeWeight).also { cumulativeWeight = it } -> TileType.SAFE
            else -> TileType.TRAP
        }
    }

    /**
     * 특별 규칙 생성
     */
    private fun generateSpecialRules(rules: RuleModifier, cardPacks: List<CardPack>): List<String> {
        val specialRules = mutableListOf<String>()

        if (!rules.allowConsecutivePenalty) {
            specialRules.add("연속 벌칙 제한: 최대 ${rules.maxConsecutivePenalty}회")
        }

        if (rules.penaltyMultiplier != 1.0f) {
            val percentage = (rules.penaltyMultiplier * 100).toInt()
            specialRules.add("벌칙 강도: ${percentage}%")
        }

        if (!rules.enableDoubleBonus) {
            specialRules.add("더블 보너스 비활성화")
        }

        cardPacks.forEach { pack ->
            if (pack.mapModifier != null) {
                specialRules.add("${pack.name} 효과 활성화")
            }
        }

        return specialRules
    }

    private fun mergeRules(rule1: RuleModifier, rule2: RuleModifier): RuleModifier {
        return RuleModifier(
            allowConsecutivePenalty = rule1.allowConsecutivePenalty && rule2.allowConsecutivePenalty,
            maxConsecutivePenalty = minOf(rule1.maxConsecutivePenalty, rule2.maxConsecutivePenalty),
            penaltyMultiplier = rule1.penaltyMultiplier * rule2.penaltyMultiplier,
            enableDoubleBonus = rule1.enableDoubleBonus && rule2.enableDoubleBonus,
            enableReverseDirection = rule1.enableReverseDirection && rule2.enableReverseDirection,
            winCondition = rule2.winCondition // 마지막 카드팩의 승리 조건 우선
        )
    }

    private fun mergeEvents(event1: EventModifier, event2: EventModifier): EventModifier {
        return EventModifier(
            specialEventProbability = (event1.specialEventProbability + event2.specialEventProbability) / 2,
            miniBossProbability = (event1.miniBossProbability + event2.miniBossProbability) / 2,
            bonusRoundProbability = (event1.bonusRoundProbability + event2.bonusRoundProbability) / 2,
            customEvents = event1.customEvents + event2.customEvents
        )
    }

    private fun getTileTitle(type: TileType): String {
        return when (type) {
            TileType.START -> "출발"
            TileType.CARD -> "카드"
            TileType.EVENT -> "이벤트"
            TileType.SAFE -> "휴식"
            TileType.TRAP -> "함정"
        }
    }

    private fun getTileDescription(type: TileType): String {
        return when (type) {
            TileType.START -> "게임 시작!"
            TileType.CARD -> "카드를 뽑으세요"
            TileType.EVENT -> "특별 이벤트!"
            TileType.SAFE -> "잠시 쉬어가세요"
            TileType.TRAP -> "벌칙 2배!"
        }
    }
}
