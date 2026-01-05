package com.manus.juryumarble.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manus.juryumarble.data.local.db.CardDao
import com.manus.juryumarble.data.local.db.CardPackDao
import com.manus.juryumarble.data.local.model.CardEntity
import com.manus.juryumarble.data.local.model.CardPackEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 기본 카드 데이터 로더
 * assets/cards.json 파일에서 카드 데이터를 로드하여 DB에 저장
 */
@Singleton
class CardDataLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cardDao: CardDao,
    private val cardPackDao: CardPackDao,
    private val gson: Gson
) {
    
    /**
     * 앱 시작 시 기본 카드 데이터가 없으면 로드
     */
    suspend fun loadCardsIfNeeded() {
        withContext(Dispatchers.IO) {
            val count = cardDao.getCardCount()
            if (count == 0) {
                loadDefaultCardPack()
                loadDefaultCards()
            }
        }
    }

    /**
     * 기본 카드팩 생성
     */
    private suspend fun loadDefaultCardPack() {
        val defaultPack = CardPackEntity(
            packId = "default",
            name = "기본 카드팩",
            description = "주류마블 기본 게임 카드팩입니다. 순한맛부터 매운맛까지 다양한 카드가 포함되어 있습니다.",
            theme = "classic",
            imageUrl = null,
            thumbnailUrl = null,
            cardCount = 50,
            isCustom = false,
            isEnabled = true,
            isPremium = false,
            price = 0.0,
            mapModifierJson = null
        )

        cardPackDao.insertCardPack(defaultPack)
    }
    
    /**
     * 기본 카드 데이터 로드
     */
    private suspend fun loadDefaultCards() {
        try {
            // assets에서 JSON 파일 로드
            val jsonString = context.assets
                .open("cards.json")
                .bufferedReader()
                .use { it.readText() }
            
            val type = object : TypeToken<List<CardEntity>>() {}.type
            val cards: List<CardEntity> = gson.fromJson(jsonString, type)
            
            cardDao.insertCards(cards)
        } catch (e: Exception) {
            // assets 파일이 없으면 기본 카드 생성
            loadHardcodedCards()
        }
    }
    
    /**
     * 하드코딩된 기본 카드 (fallback)
     */
    private suspend fun loadHardcodedCards() {
        val cards = listOf(
            // MILD 카드들
            CardEntity("default_mission_mild_001", "default", "MISSION", "SELF", "자기소개 타임", "자신을 3가지 키워드로 소개하고, 각 키워드에 얽힌 짧은 에피소드를 이야기하세요. (실패: 1샷)", "MILD", 1.0f),
            CardEntity("default_mission_mild_002", "default", "MISSION", "SELF", "최애 메뉴", "최근 1달간 먹은 음식 중 가장 맛있었던 메뉴와 그 이유를 생생하게 설명하기. (실패: 1샷)", "MILD", 1.0f),
            CardEntity("default_mission_mild_003", "default", "MISSION", "ALL", "건배사 제의", "지금 분위기에 딱 맞는 멋진 건배사를 외치고 다 함께 원샷!", "MILD", 1.0f),
            CardEntity("default_mission_mild_004", "default", "MISSION", "TARGET_ONE", "칭찬 샤워", "오른쪽 사람의 장점 3가지를 찾아서 진심을 담아 칭찬해주세요.", "MILD", 0.0f),
            CardEntity("default_mission_mild_005", "default", "MISSION", "SELF", "애창곡 한 소절", "자신의 애창곡 1절을 구성지게 불러보세요. (실패: 1샷)", "MILD", 1.0f),
            CardEntity("default_penalty_mild_001", "default", "PENALTY", "SELF", "물 한잔 원샷", "축하합니다! 건강을 위해 물 한잔을 시원하게 원샷하세요.", "MILD", 0.0f),
            CardEntity("default_penalty_mild_002", "default", "PENALTY", "SELF", "반샷", "가볍게 반만 마시기.", "MILD", 0.5f),
            CardEntity("default_penalty_mild_003", "default", "PENALTY", "ALL_EXCEPT_SELF", "나 빼고 원샷", "본인을 제외한 모든 플레이어가 한 잔씩 마십니다.", "MILD", 1.0f),
            CardEntity("default_safe_mild_001", "default", "SAFE", "SELF", "벌칙 면제권", "축하합니다! 이번 턴의 모든 벌칙에서 해방되었습니다.", "MILD", 0.0f),
            CardEntity("default_safe_mild_002", "default", "SAFE", "SELF", "물 마시기 타임", "잠시 쉬어가세요. 물 한 컵을 마시며 목을 축입니다.", "MILD", 0.0f),
            CardEntity("default_rule_mild_001", "default", "RULE", "ALL", "한 글자 금지", "다음 턴까지 '나' 라는 단어를 사용하는 사람은 1샷.", "MILD", 1.0f),
            CardEntity("default_event_mild_001", "default", "EVENT", "ALL", "방향 전환", "게임 진행 방향이 반대로 바뀝니다.", "MILD", 0.0f),
            
            // NORMAL 카드들
            CardEntity("default_mission_normal_001", "default", "MISSION", "SELF", "밸런스 게임", "\"평생 1가지 음식만 먹기 vs 평생 1가지 노래만 듣기\". 선택 후 이유 설명. (실패: 1샷)", "NORMAL", 1.0f),
            CardEntity("default_mission_normal_002", "default", "MISSION", "TARGET_ONE", "첫인상 말하기", "지목한 사람의 첫인상과 현재 인상이 어떻게 다른지 솔직하게 말하기.", "NORMAL", 0.0f),
            CardEntity("default_mission_normal_003", "default", "MISSION", "SELF", "흑역사 방출", "내 인생 최악의 흑역사 하나를 모두에게 공개하세요. (실패: 2샷)", "NORMAL", 2.0f),
            CardEntity("default_penalty_normal_001", "default", "PENALTY", "SELF", "원샷", "깔끔하게 원샷!", "NORMAL", 1.0f),
            CardEntity("default_penalty_normal_002", "default", "PENALTY", "TARGET_ONE", "너, 마셔라", "원하는 사람 한 명을 지목해 1샷을 선물하세요.", "NORMAL", 1.0f),
            CardEntity("default_penalty_normal_003", "default", "PENALTY", "SELF", "러브샷", "왼쪽 사람과 다정하게 러브샷.", "NORMAL", 1.0f),
            CardEntity("default_rule_normal_001", "default", "RULE", "ALL", "영어 금지", "다음 턴까지 영어를 사용하는 사람은 무조건 1샷. (OK, YES 등 포함)", "NORMAL", 1.0f),
            CardEntity("default_event_normal_001", "default", "EVENT", "SELF", "자리 바꾸기", "원하는 사람 한 명과 자리를 바꿀 수 있습니다.", "NORMAL", 0.0f),
            CardEntity("default_mission_normal_004", "default", "MISSION", "SELF", "TMI 방출", "사람들이 아무도 궁금해하지 않을 나만의 TMI를 1가지 공개하세요.", "NORMAL", 0.0f),
            CardEntity("default_mission_normal_005", "default", "MISSION", "TARGET_ONE", "술 따라주기", "오른쪽 사람의 잔이 비었다면, 공손하게 술을 따라주세요.", "NORMAL", 0.0f),
            CardEntity("default_penalty_normal_004", "default", "PENALTY", "SELF", "2배 마시기", "축하합니다! 벌칙이 2배가 되었습니다. 2샷 원샷!", "NORMAL", 2.0f),
            CardEntity("default_penalty_normal_005", "default", "PENALTY", "TARGET_ONE", "지목 흑기사", "한 명을 지목해 나의 흑기사로 만드세요. 그 사람이 대신 마십니다.", "NORMAL", 1.0f),
            CardEntity("default_rule_normal_002", "default", "RULE", "ALL", "끝말잇기", "현재 플레이어부터 끝말잇기 시작! 가장 먼저 막히는 사람이 1샷.", "NORMAL", 1.0f),
            
            // SPICY 카드들
            CardEntity("default_mission_spicy_001", "default", "MISSION", "SELF", "폰 공개 1분", "1분간 휴대폰 잠금 풀고 테이블 위에 올려두기. (실패: 3샷)", "SPICY", 3.0f),
            CardEntity("default_mission_spicy_002", "default", "MISSION", "TARGET_ONE", "이상형 월드컵", "이성 플레이어 중 자신의 이상형에 가장 가까운 사람을 꼽고 이유 설명하기.", "SPICY", 0.0f),
            CardEntity("default_penalty_spicy_001", "default", "PENALTY", "SELF", "폭탄주 제조", "원하는 재료로 폭탄주를 만들어 원샷하세요.", "SPICY", 2.0f),
            CardEntity("default_penalty_spicy_002", "default", "PENALTY", "ALL", "연대 책임", "한 명만 걸려도 모두가 함께 마시는 의리주! 다 같이 원샷!", "SPICY", 1.0f),
            CardEntity("default_rule_spicy_001", "default", "RULE", "ALL", "존댓말 금지", "지금부터 반말 모드! 다음 턴까지 존댓말 사용 시 1샷.", "SPICY", 1.0f),
            CardEntity("default_event_spicy_001", "default", "EVENT", "ALL", "왕 게임", "이 카드를 뽑은 사람이 왕! 1회에 한해 어떤 명령이든 내릴 수 있다.", "SPICY", 0.0f),
            CardEntity("default_mission_spicy_003", "default", "MISSION", "SELF", "단톡방에 메시지 보내기", "현재 단톡방에 \"나 사실 너 좋아해\" 라고 보내기. (5분 뒤 해명 필수) (실패: 3샷)", "SPICY", 3.0f),
            CardEntity("default_mission_spicy_004", "default", "MISSION", "TARGET_ONE", "당연하지 게임", "지목한 사람과 \"당연하지\" 게임 시작! 먼저 \"당연하지\"를 외치지 못하면 패배. (패배: 2샷)", "SPICY", 2.0f),
            CardEntity("default_penalty_spicy_003", "default", "PENALTY", "SELF", "섹시 댄스", "노래 한 곡에 맞춰 혼신의 힘을 다해 섹시 댄스를 추세요.", "SPICY", 1.0f),
            CardEntity("default_penalty_spicy_004", "default", "PENALTY", "ALL_EXCEPT_SELF", "왕따주", "본인을 제외한 모든 사람이 당신을 위해 짠~ 하고 마셔줍니다.", "SPICY", 1.0f),
            CardEntity("default_rule_spicy_002", "default", "RULE", "ALL", "호칭 통일", "다음 턴까지 모두 서로를 \"자기야\"라고 부르기. 어길 시 1샷.", "SPICY", 1.0f),
            
            // 추가 카드들
            CardEntity("default_mission_mild_006", "default", "MISSION", "SELF", "4행시 짓기", "'주류마블' 4글자로 센스있는 4행시를 지어보세요. (실패: 1샷)", "MILD", 1.0f),
            CardEntity("default_mission_mild_007", "default", "MISSION", "TARGET_ONE", "안마 서비스", "왼쪽 사람에게 1분간 정성을 다해 안마해주기.", "MILD", 0.0f),
            CardEntity("default_mission_mild_008", "default", "MISSION", "SELF", "만약에...", "\"만약 10억이 생긴다면 가장 먼저 하고 싶은 것은?\"에 답하기.", "MILD", 0.0f),
            CardEntity("default_mission_mild_009", "default", "MISSION", "ALL", "손병호 게임", "\"손병호 게임\" 시작! 5개 손가락을 가장 먼저 다 접는 사람이 패배. (패배: 1샷)", "MILD", 1.0f),
            CardEntity("default_mission_mild_010", "default", "MISSION", "SELF", "영화 명대사", "아는 영화 명대사를 성대모사하며 따라해보세요. (실패: 1샷)", "MILD", 1.0f),
            CardEntity("default_penalty_mild_004", "default", "PENALTY", "SELF", "간지럽히기", "양 옆 사람에게 10초간 간지럼 공격 당하기.", "MILD", 0.0f),
            CardEntity("default_penalty_mild_005", "default", "PENALTY", "SELF", "윙크 발사", "여기 있는 모든 사람과 눈을 마주치며 윙크하기.", "MILD", 0.0f),
            CardEntity("default_safe_mild_003", "default", "SAFE", "SELF", "다음 턴 스킵", "이번 턴은 쉬어갑니다. 다음 턴에 만나요!", "MILD", 0.0f),
            CardEntity("default_safe_mild_004", "default", "SAFE", "SELF", "천사 카드", "다음 벌칙을 다른 사람에게 넘길 수 있습니다. (1회용)", "MILD", 0.0f),
            CardEntity("default_rule_mild_002", "default", "RULE", "ALL", "특정 행동 금지", "다음 턴까지 '다리 꼬기' 금지. 어길 시 1샷.", "MILD", 1.0f),
            CardEntity("default_event_mild_002", "default", "EVENT", "SELF", "워프", "보드판에서 원하는 칸으로 즉시 이동할 수 있습니다.", "MILD", 0.0f),
            CardEntity("default_mission_normal_006", "default", "MISSION", "SELF", "진실 혹은 도전", "\"진실\"을 선택하면 비밀 질문에 답하고, \"도전\"을 선택하면 어려운 미션을 수행합니다.", "NORMAL", 1.5f),
            CardEntity("default_event_normal_002", "default", "EVENT", "ALL", "카드 셔플", "모든 카드 덱을 다시 섞습니다. 새로운 판이 시작됩니다!", "NORMAL", 0.0f),
            CardEntity("default_event_spicy_002", "default", "EVENT", "ALL", "진실의 시간", "5분 동안 모든 질문에 오직 진실만을 말해야 합니다.", "SPICY", 0.0f),
            CardEntity("default_mission_normal_007", "default", "MISSION", "SELF", "휴대폰 사진 공개", "휴대폰 갤러리에서 7번째 사진을 모두에게 공개하고 설명하세요.", "NORMAL", 0.0f),
            CardEntity("default_penalty_normal_006", "default", "PENALTY", "SELF", "양 옆 사람과 짠", "양 옆 사람과 잔을 부딪히고 다 함께 원샷!", "NORMAL", 1.0f),
            CardEntity("default_rule_normal_003", "default", "RULE", "ALL", "웃음 참기", "다음 턴까지 절대 웃으면 안됩니다. 웃는 사람은 1샷!", "NORMAL", 1.0f),
            CardEntity("default_mission_mild_011", "default", "MISSION", "SELF", "최근 본 영상", "최근에 본 유튜브 영상 중 가장 재미있었던 것을 소개하고 추천 이유를 말하세요.", "MILD", 0.0f),
            CardEntity("default_mission_mild_012", "default", "MISSION", "SELF", "학창시절 별명", "학창시절 별명과 그 이유를 설명하세요.", "MILD", 0.0f),
            CardEntity("default_mission_mild_015", "default", "MISSION", "ALL", "눈치 게임", "1부터 순서대로 숫자를 외치세요. 동시에 외치거나 마지막에 외치는 사람이 패배! (패배: 1샷)", "MILD", 1.0f)
        )
        
        cardDao.insertCards(cards)
    }
}
