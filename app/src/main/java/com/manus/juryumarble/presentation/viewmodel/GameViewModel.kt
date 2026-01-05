package com.manus.juryumarble.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manus.juryumarble.domain.model.*
import com.manus.juryumarble.domain.usecase.ApplyDDAUseCase
import com.manus.juryumarble.domain.usecase.CalculateGameStatisticsUseCase
import com.manus.juryumarble.domain.usecase.CheckGameEndConditionUseCase
import com.manus.juryumarble.domain.usecase.DrawCardUseCase
import com.manus.juryumarble.domain.usecase.ExecuteCardEffectUseCase
import com.manus.juryumarble.domain.usecase.InitializeGameUseCase
import com.manus.juryumarble.domain.usecase.LoadGameStateUseCase
import com.manus.juryumarble.domain.usecase.ProcessTurnUseCase
import com.manus.juryumarble.domain.usecase.SaveGameStateUseCase
import com.manus.juryumarble.domain.usecase.ScalePenaltyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Game ViewModel - 게임 전체 상태 관리
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val initializeGameUseCase: InitializeGameUseCase,
    private val processTurnUseCase: ProcessTurnUseCase,
    private val scalePenaltyUseCase: ScalePenaltyUseCase,
    private val checkGameEndConditionUseCase: CheckGameEndConditionUseCase,
    private val calculateGameStatisticsUseCase: CalculateGameStatisticsUseCase,
    private val applyDDAUseCase: ApplyDDAUseCase,
    private val saveGameStateUseCase: SaveGameStateUseCase,
    private val loadGameStateUseCase: LoadGameStateUseCase,
    private val drawCardUseCase: DrawCardUseCase,
    private val executeCardEffectUseCase: ExecuteCardEffectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameState: GameState? = null
    private var sessionConfig: SessionConfig? = null
    private var difficultyState = DifficultyState()

    // 현재 게임의 모든 플레이어 정보 노출
    val currentPlayers: List<Player>
        get() = gameState?.players ?: emptyList()

    init {
        // Check for saved game on initialization
        checkForSavedGame()
    }
    
    /**
     * 플레이어 추가
     */
    fun addPlayer(name: String) {
        if (_uiState.value.players.size < 6 && name.isNotBlank()) {
            _uiState.update { it.copy(players = it.players + name.trim()) }
        }
    }
    
    /**
     * 플레이어 제거
     */
    fun removePlayer(index: Int) {
        if (_uiState.value.players.size > 2) {
            _uiState.update { 
                it.copy(players = it.players.toMutableList().apply { removeAt(index) })
            }
        }
    }
    
    /**
     * 수위 설정
     */
    fun setSeverity(severity: Severity) {
        _uiState.update { it.copy(selectedSeverity = severity) }
    }
    
    /**
     * 게임 시작
     */
    fun startGame() {
        Log.d("GameViewModel", "========== startGame() called ==========")
        Log.d("GameViewModel", "Players: ${_uiState.value.players}")

        // 플레이어가 2명 미만이면 에러
        if (_uiState.value.players.size < 2) {
            Log.e("GameViewModel", "Not enough players")
            return
        }

        // 간단한 테스트: 무조건 샘플 게임 시작
        Log.d("GameViewModel", "Starting game with sample data...")
        startGameWithoutCards()

        /* 나중에 DB 연동 시 사용
        viewModelScope.launch {
            val config = SessionConfig(
                playerNames = _uiState.value.players,
                severityFilter = _uiState.value.selectedSeverity,
                activatedCardPackIds = listOf("default")
            )
            sessionConfig = config

            try {
                gameState = initializeGameUseCase(config)
                gameState?.let { state ->
                    _uiState.update {
                        it.copy(
                            isGameStarted = true,
                            currentPlayerIndex = state.currentPlayerIndex,
                            currentPlayerName = state.currentPlayer.nickname,
                            playerPositions = state.players.associate { p -> p.id to p.position },
                            turnPhase = state.turnPhase,
                            gameStatus = state.status,
                            players = _uiState.value.players
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error: ${e.message}", e)
                startGameWithoutCards()
            }
        }
        */
    }
    
    /**
     * 카드 없이 기본 게임 시작 (테스트용)
     */
    private fun startGameWithoutCards() {
        Log.d("GameViewModel", "Starting game without cards...")

        val players = _uiState.value.players.mapIndexed { index, name ->
            Player(
                id = "player_$index",
                nickname = name,
                position = 0,
                penaltyCount = 0,
                consecutivePenalties = 0,
                isActive = true
            )
        }

        Log.d("GameViewModel", "Players created: ${players.size}")

        val board = createDefaultBoard()
        Log.d("GameViewModel", "Board created: ${board.size} tiles")

        val deck = createSampleCards()
        Log.d("GameViewModel", "Deck created: ${deck.size} cards")

        gameState = GameState(
            sessionId = "session_${System.currentTimeMillis()}",
            randomSeed = System.currentTimeMillis(),
            players = players,
            board = board,
            deck = deck,
            currentPlayerIndex = 0,
            currentTurn = 1,
            turnPhase = TurnPhase.START,
            status = GameStatus.IN_PROGRESS,
            direction = GameDirection.CLOCKWISE,
            startTime = System.currentTimeMillis()
        )

        Log.d("GameViewModel", "GameState created, updating UI...")

        _uiState.update {
            it.copy(
                isGameStarted = true,
                currentPlayerIndex = 0,
                currentPlayerName = players[0].nickname,
                playerPositions = players.associate { p -> p.id to p.position },
                turnPhase = TurnPhase.START,
                gameStatus = GameStatus.IN_PROGRESS,
                players = _uiState.value.players
            )
        }

        Log.d("GameViewModel", "Game started without cards successfully! isGameStarted=${_uiState.value.isGameStarted}")
    }
    
    /**
     * 주사위 굴리기
     */
    fun rollDice() {
        gameState?.let { state ->
            if (_uiState.value.isRolling) return
            
            _uiState.update { it.copy(isRolling = true) }
            
            viewModelScope.launch {
                // 굴리는 애니메이션 시간
                kotlinx.coroutines.delay(800)
                
                val diceResult = processTurnUseCase.rollDice(state, true)
                val newState = processTurnUseCase.movePlayer(state, diceResult)
                gameState = newState
                
                _uiState.update { 
                    it.copy(
                        isRolling = false,
                        lastDiceResult = diceResult,
                        playerPositions = newState.players.associate { p -> p.id to p.position }
                    )
                }
                
                // 칸 이벤트 처리
                processCurrentTile()
            }
        }
    }
    
    /**
     * 현재 칸 이벤트 처리
     */
    private fun processCurrentTile() {
        gameState?.let { state ->
            val processedState = processTurnUseCase.processTileEvent(state)
            gameState = processedState
            
            when (processedState.turnPhase) {
                TurnPhase.ACTION_RESOLUTION -> {
                    // 카드 뽑기 - 새로운 drawCard 메서드 사용
                    drawCard()
                }
                TurnPhase.END -> {
                    // 턴 종료, 다음 플레이어로
                    endTurn()
                }
                else -> {}
            }
        }
    }

    /**
     * 턴 종료
     */
    private fun endTurn() {
        gameState?.let { state ->
            // 게임 종료 조건 체크
            sessionConfig?.let { config ->
                val (shouldEnd, reason) = checkGameEndConditionUseCase(state, config)
                if (shouldEnd) {
                    endGame(reason)
                    return
                }
            }

            val newState = processTurnUseCase.endTurn(state)
            gameState = newState

            // DDA 업데이트
            updateDDA(newState)

            // 자동 저장
            autoSaveGame()

            _uiState.update {
                it.copy(
                    currentPlayerIndex = newState.currentPlayerIndex,
                    currentPlayerName = newState.currentPlayer.nickname,
                    turnPhase = newState.turnPhase,
                    lastDiceResult = null
                )
            }
        }
    }

    /**
     * DDA 상태 업데이트
     */
    private fun updateDDA(state: GameState) {
        sessionConfig?.let { config ->
            if (config.enableDDA) {
                val (newDDAState, actions) = applyDDAUseCase.updateDifficultyState(
                    difficultyState,
                    state,
                    config.ddaRules
                )
                difficultyState = newDDAState

                // DDA 액션 처리
                actions.forEach { action ->
                    handleDDAAction(action)
                }
            }
        }
    }

    /**
     * DDA 액션 처리
     */
    private fun handleDDAAction(action: DDAAction) {
        when (action) {
            is DDAAction.SuggestBreak -> {
                Log.d("GameViewModel", "DDA suggests a break")
                _uiState.update {
                    it.copy(
                        showDdaBreakSuggestion = true,
                        ddaMessage = "잠시 휴식을 취하는 것은 어떨까요? 물 한 잔 마시고 돌아오세요!"
                    )
                }
            }
            is DDAAction.ReducePenaltyProbability -> {
                Log.d("GameViewModel", "DDA reducing penalty probability by ${action.reduction}")
                gameState?.let { state ->
                    // 덱에서 벌칙 카드 일부를 휴식 카드로 교체
                    val deck = state.deck.toMutableList()
                    val penaltyCards = deck.filter { it.cardType == CardType.PENALTY }
                    val cardsToReplace = (penaltyCards.size * action.reduction).toInt()

                    if (cardsToReplace > 0 && penaltyCards.isNotEmpty()) {
                        // 벌칙 카드 일부 제거
                        val removed = penaltyCards.take(cardsToReplace)
                        deck.removeAll(removed)

                        // 휴식 카드 추가
                        val safeCard = Card(
                            cardId = "dda_safe_${System.currentTimeMillis()}",
                            cardPackId = "dda",
                            cardType = CardType.SAFE,
                            targetType = TargetType.SELF,
                            title = "휴식 타임",
                            description = "잠시 쉬어가세요. 물을 마시며 휴식하세요.",
                            severity = Severity.MILD,
                            penaltyScale = 0.0f
                        )
                        repeat(cardsToReplace) { deck.add(safeCard) }

                        // 덱 셔플
                        val random = kotlin.random.Random(state.randomSeed + state.currentTurn)
                        val shuffledDeck = deck.shuffled(random)

                        gameState = state.copy(deck = shuffledDeck)

                        _uiState.update {
                            it.copy(ddaMessage = "난이도가 조정되었습니다. 조금 더 편하게 즐기세요!")
                        }
                    }
                }
            }
            is DDAAction.IncreaseRestProbability -> {
                Log.d("GameViewModel", "DDA increasing rest card probability")
                gameState?.let { state ->
                    // 휴식 카드를 덱 상단에 추가
                    val safeCard = Card(
                        cardId = "dda_rest_${System.currentTimeMillis()}",
                        cardPackId = "dda",
                        cardType = CardType.SAFE,
                        targetType = TargetType.SELF,
                        title = "DDA 휴식권",
                        description = "게임에서 자동으로 제공된 휴식 기회입니다.",
                        severity = Severity.MILD,
                        penaltyScale = 0.0f
                    )

                    val newDeck = listOf(safeCard) + state.deck
                    gameState = state.copy(deck = newDeck)
                }
            }
            is DDAAction.ResetDifficulty -> {
                Log.d("GameViewModel", "DDA resetting difficulty to normal")
                difficultyState = DifficultyState()
                _uiState.update {
                    it.copy(ddaMessage = "난이도가 기본 상태로 초기화되었습니다.")
                }
            }
        }
    }

    /**
     * DDA 메시지 소비
     */
    fun consumeDdaMessage() {
        _uiState.update { it.copy(ddaMessage = null) }
    }

    /**
     * DDA 휴식 제안 닫기
     */
    fun dismissBreakSuggestion() {
        _uiState.update { it.copy(showDdaBreakSuggestion = false) }
    }

    /**
     * 게임 종료 및 통계 계산
     */
    private fun endGame(reason: String? = null) {
        gameState?.let { state ->
            Log.d("GameViewModel", "Game ending: $reason")

            val statistics = calculateGameStatisticsUseCase(state)

            _uiState.update {
                it.copy(
                    gameStatus = GameStatus.ENDED,
                    totalTurns = statistics.totalTurns,
                    cardsUsed = statistics.totalCardsUsed
                )
            }
        }
    }
    
    /**
     * 수동 게임 종료
     */
    fun forceEndGame() {
        endGame("사용자 요청")
    }
    
    /**
     * 게임 리셋
     */
    fun resetGame() {
        gameState = null
        sessionConfig = null
        _uiState.value = GameUiState()
    }

    /**
     * 저장된 게임 확인
     */
    private fun checkForSavedGame() {
        viewModelScope.launch {
            val hasSave = loadGameStateUseCase.hasSavedGame()
            _uiState.update { it.copy(hasSavedGame = hasSave) }
        }
    }

    /**
     * 게임 상태 저장
     */
    fun saveGame() {
        gameState?.let { state ->
            viewModelScope.launch {
                saveGameStateUseCase(state)
                Log.d("GameViewModel", "Game saved: ${state.sessionId}")
            }
        }
    }

    /**
     * 저장된 게임 로드
     */
    fun loadSavedGame() {
        viewModelScope.launch {
            loadGameStateUseCase()?.let { state ->
                gameState = state
                sessionConfig = SessionConfig(
                    playerNames = state.players.map { it.nickname },
                    severityFilter = Severity.NORMAL,
                    activatedCardPackIds = listOf("default")
                )

                _uiState.update {
                    it.copy(
                        isGameStarted = true,
                        currentPlayerIndex = state.currentPlayerIndex,
                        currentPlayerName = state.currentPlayer.nickname,
                        playerPositions = state.players.associate { p -> p.id to p.position },
                        turnPhase = state.turnPhase,
                        gameStatus = state.status,
                        totalTurns = state.currentTurn,
                        hasSavedGame = false
                    )
                }

                Log.d("GameViewModel", "Game loaded: ${state.sessionId}")
            }
        }
    }

    /**
     * 저장된 게임 삭제
     */
    fun deleteSavedGame() {
        gameState?.let { state ->
            viewModelScope.launch {
                loadGameStateUseCase.deleteSave(state.sessionId)
                _uiState.update { it.copy(hasSavedGame = false) }
            }
        }
    }

    /**
     * 자동 저장 (턴 종료 시 호출)
     */
    private fun autoSaveGame() {
        gameState?.let { state ->
            // 게임이 진행 중일 때만 자동 저장
            if (state.status == GameStatus.IN_PROGRESS) {
                viewModelScope.launch {
                    saveGameStateUseCase(state)
                }
            }
        }
    }

    private fun createDefaultBoard(): List<Tile> {
        return listOf(
            Tile(0, TileType.START, "출발", "게임 시작!"),
            Tile(1, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(2, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(3, TileType.EVENT, "이벤트", "특별 이벤트!"),
            Tile(4, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(5, TileType.SAFE, "휴식", "잠시 쉬어가세요"),
            Tile(6, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(7, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(8, TileType.TRAP, "함정", "벌칙 2배!"),
            Tile(9, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(10, TileType.EVENT, "이벤트", "방향 전환!"),
            Tile(11, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(12, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(13, TileType.SAFE, "휴식", "물 마시기"),
            Tile(14, TileType.CARD, "카드", "카드를 뽑으세요"),
            Tile(15, TileType.CARD, "카드", "카드를 뽑으세요")
        )
    }
    
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

    /**
     * 카드 뽑기
     */
    fun drawCard() {
        gameState?.let { state ->
            val (card, newState) = drawCardUseCase(state)

            if (card == null) {
                Log.w("GameViewModel", "No cards available to draw")
                _uiState.update { it.copy(currentCard = null) }
                return
            }

            gameState = newState

            // 벌칙 카드인 경우 스케일링된 텍스트 생성
            val scaledText = if (card.cardType == CardType.PENALTY && card.penaltyScale > 0) {
                val currentPlayer = state.currentPlayer
                sessionConfig?.let { config ->
                    val scaledPenalty = scalePenaltyUseCase.scaleWithDetails(
                        card = card,
                        player = currentPlayer,
                        drinkType = config.drinkType,
                        drinkUnit = config.drinkUnit
                    )
                    scaledPenalty.displayText
                }
            } else {
                null
            }

            _uiState.update {
                it.copy(
                    currentCard = card,
                    showCardDialog = true,
                    scaledPenaltyText = scaledText
                )
            }

            Log.d("GameViewModel", "Card drawn: ${card.title}, Penalty text: $scaledText")
        }
    }

    /**
     * 카드 효과 실행
     */
    fun executeCard(targetPlayerIndex: Int? = null) {
        gameState?.let { state ->
            _uiState.value.currentCard?.let { card ->
                val result = executeCardEffectUseCase(state, card, targetPlayerIndex)

                if (result.requiresPlayerSelection) {
                    _uiState.update {
                        it.copy(
                            cardMessage = result.message,
                            requiresPlayerSelection = true,
                            allowedPlayerIndices = result.allowedPlayerIndices
                        )
                    }
                    return
                }

                // Update game state with result
                gameState = result.updatedGameState

                // Discard the card
                gameState = drawCardUseCase.discardCard(result.updatedGameState, card)

                // Update UI
                _uiState.update {
                    it.copy(
                        currentCard = null,
                        cardMessage = result.message,
                        showCardDialog = false,
                        requiresPlayerSelection = false,
                        playerPositions = result.updatedPlayers.associate { p -> p.id to p.position }
                    )
                }

                Log.d("GameViewModel", "Card executed: ${card.title} - ${result.message}")
            }
        }
    }

    /**
     * 카드 대화상자 닫기
     */
    fun dismissCardDialog() {
        _uiState.update {
            it.copy(
                showCardDialog = false,
                currentCard = null,
                cardMessage = null,
                requiresPlayerSelection = false
            )
        }
    }
}

/**
 * UI State
 */
data class GameUiState(
    // Setup
    val players: List<String> = listOf("플레이어 1", "플레이어 2"),
    val selectedSeverity: Severity = Severity.NORMAL,

    // Save/Load
    val hasSavedGame: Boolean = false,

    // Game State
    val isGameStarted: Boolean = false,
    val currentPlayerIndex: Int = 0,
    val currentPlayerName: String = "",
    val playerPositions: Map<String, Int> = emptyMap(),
    val turnPhase: TurnPhase = TurnPhase.START,
    val gameStatus: GameStatus = GameStatus.SETUP,

    // Dice
    val isRolling: Boolean = false,
    val lastDiceResult: DiceResult? = null,
    
    // Card
    val currentCard: Card? = null,
    val showCardDialog: Boolean = false,
    val scaledPenaltyText: String? = null,
    val cardMessage: String? = null,
    val requiresPlayerSelection: Boolean = false,
    val allowedPlayerIndices: List<Int> = emptyList(),

    // DDA (Dynamic Difficulty Adjustment)
    val ddaMessage: String? = null,
    val showDdaBreakSuggestion: Boolean = false,

    // Stats
    val totalTurns: Int = 0,
    val cardsUsed: Int = 0
)
