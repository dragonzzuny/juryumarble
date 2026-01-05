# 주류마블 안드로이드 앱: 테마별 카드팩 콘텐츠

## 1. 개요

이 문서는 '주류마블' 앱의 핵심 콘텐츠인 카드 덱의 상세 내용을 정의합니다. 각 카드는 고유 ID, 타입, 수위, 내용 등을 포함하며, 이는 게임의 재미와 다양성을 결정하는 가장 중요한 요소입니다. MVP에서는 '기본 팩' 1종을 우선적으로 제공하며, 추후 다양한 테마의 팩이 추가될 수 있습니다.

## 2. 카드 데이터 구조

모든 카드는 다음의 데이터 구조를 따릅니다. 이는 `Card` 데이터 클래스와 일치합니다.

- **cardId**: 카드의 고유 ID (e.g., `default_mission_mild_001`)
- **cardPackId**: 카드가 속한 팩의 ID (e.g., `default`)
- **cardType**: 카드의 종류 (`MISSION`, `PENALTY`, `RULE`, `EVENT`, `SAFE`)
- **targetType**: 카드의 적용 대상 (`SELF`, `TARGET_ONE`, `ALL`, `ALL_EXCEPT_SELF`)
- **title**: 카드 제목 (UI에 표시될 간결한 이름)
- **description**: 카드의 상세 내용. 미션, 벌칙, 규칙 등을 설명합니다.
- **severity**: 카드의 수위 (`MILD`, `NORMAL`, `SPICY`). 게임 설정의 수위 필터에 사용됩니다.
- **penaltyScale**: 벌칙의 강도를 결정하는 배율. 1.0이 표준(1샷)입니다. 미션 실패 시 적용될 수 있습니다.

## 3. 기본 카드팩 (Default Pack) - 200종

가장 범용적인 상황을 위한 카드팩입니다. 아이스브레이킹부터 점차 고조되는 분위기까지 고려하여 다양한 종류와 수위의 카드를 포함합니다.

| cardId | cardPackId | cardType | targetType | title | description | severity | penaltyScale |
|---|---|---|---|---|---|---|---|
| default_mission_mild_001 | default | MISSION | SELF | 자기소개 타임 | 자신을 3가지 키워드로 소개하고, 각 키워드에 얽힌 짧은 에피소드를 이야기하세요. (실패: 1샷) | MILD | 1.0 |
| default_mission_mild_002 | default | MISSION | SELF | 최애 메뉴 | 최근 1달간 먹은 음식 중 가장 맛있었던 메뉴와 그 이유를 생생하게 설명하기. (실패: 1샷) | MILD | 1.0 |
| default_mission_mild_003 | default | MISSION | ALL | 건배사 제의 | 지금 분위기에 딱 맞는 멋진 건배사를 외치고 다 함께 원샷! | MILD | 1.0 |
| default_mission_mild_004 | default | MISSION | TARGET_ONE | 칭찬 샤워 | 오른쪽 사람의 장점 3가지를 찾아서 진심을 담아 칭찬해주세요. | MILD | 0.0 |
| default_mission_mild_005 | default | MISSION | SELF | 애창곡 한 소절 | 자신의 애창곡 1절을 구성지게 불러보세요. (실패: 1샷) | MILD | 1.0 |
| default_penalty_mild_001 | default | PENALTY | SELF | 물 한잔 원샷 | 축하합니다! 건강을 위해 물 한잔을 시원하게 원샷하세요. | MILD | 0.0 |
| default_penalty_mild_002 | default | PENALTY | SELF | 반샷 | 가볍게 반만 마시기. | MILD | 0.5 |
| default_penalty_mild_003 | default | PENALTY | ALL_EXCEPT_SELF | 나 빼고 원샷 | 본인을 제외한 모든 플레이어가 한 잔씩 마십니다. | MILD | 1.0 |
| default_safe_mild_001 | default | SAFE | SELF | 벌칙 면제권 | 축하합니다! 이번 턴의 모든 벌칙에서 해방되었습니다. | MILD | 0.0 |
| default_safe_mild_002 | default | SAFE | SELF | 물 마시기 타임 | 잠시 쉬어가세요. 물 한 컵을 마시며 목을 축입니다. | MILD | 0.0 |
| default_rule_mild_001 | default | RULE | ALL | 한 글자 금지 | 다음 턴까지 '나' 라는 단어를 사용하는 사람은 1샷. | MILD | 1.0 |
| default_event_mild_001 | default | EVENT | ALL | 방향 전환 | 게임 진행 방향이 반대로 바뀝니다. | MILD | 0.0 |
| default_mission_normal_001 | default | MISSION | SELF | 밸런스 게임 | "평생 1가지 음식만 먹기 vs 평생 1가지 노래만 듣기". 선택 후 이유 설명. (실패: 1샷) | NORMAL | 1.0 |
| default_mission_normal_002 | default | MISSION | TARGET_ONE | 첫인상 말하기 | 지목한 사람의 첫인상과 현재 인상이 어떻게 다른지 솔직하게 말하기. | NORMAL | 0.0 |
| default_mission_normal_003 | default | MISSION | SELF | 흑역사 방출 | 내 인생 최악의 흑역사 하나를 모두에게 공개하세요. (실패: 2샷) | NORMAL | 2.0 |
| default_penalty_normal_001 | default | PENALTY | SELF | 원샷 | 깔끔하게 원샷! | NORMAL | 1.0 |
| default_penalty_normal_002 | default | PENALTY | TARGET_ONE | 너, 마셔라 | 원하는 사람 한 명을 지목해 1샷을 선물하세요. | NORMAL | 1.0 |
| default_penalty_normal_003 | default | PENALTY | SELF | 러브샷 | 왼쪽 사람과 다정하게 러브샷. | NORMAL | 1.0 |
| default_rule_normal_001 | default | RULE | ALL | 영어 금지 | 다음 턴까지 영어를 사용하는 사람은 무조건 1샷. (OK, YES 등 포함) | NORMAL | 1.0 |
| default_event_normal_001 | default | EVENT | SELF | 자리 바꾸기 | 원하는 사람 한 명과 자리를 바꿀 수 있습니다. | NORMAL | 0.0 |
| default_mission_spicy_001 | default | MISSION | SELF | 폰 공개 1분 | 1분간 휴대폰 잠금 풀고 테이블 위에 올려두기. (단, 다른 사람이 만지지는 않기) (실패: 3샷) | SPICY | 3.0 |
| default_mission_spicy_002 | default | MISSION | TARGET_ONE | 이상형 월드컵 | 이성 플레이어 중 자신의 이상형에 가장 가까운 사람을 꼽고 이유 설명하기. | SPICY | 0.0 |
| default_penalty_spicy_001 | default | PENALTY | SELF | 폭탄주 제조 | 원하는 재료로 폭탄주를 만들어 원샷하세요. | SPICY | 2.0 |
| default_penalty_spicy_002 | default | PENALTY | ALL | 연대 책임 | 한 명만 걸려도 모두가 함께 마시는 의리주! 다 같이 원샷! | SPICY | 1.0 |
| default_rule_spicy_001 | default | RULE | ALL | 존댓말 금지 | 지금부터 반말 모드! 다음 턴까지 존댓말 사용 시 1샷. | SPICY | 1.0 |
| default_event_spicy_001 | default | EVENT | ALL | 왕 게임 | 이 카드를 뽑은 사람이 왕! 1회에 한해 어떤 명령이든 내릴 수 있다. (수위 조절 필수) | SPICY | 0.0 |
| default_mission_mild_006 | default | MISSION | SELF | 4행시 짓기 | '주류마블' 4글자로 센스있는 4행시를 지어보세요. (실패: 1샷) | MILD | 1.0 |
| default_mission_mild_007 | default | MISSION | TARGET_ONE | 안마 서비스 | 왼쪽 사람에게 1분간 정성을 다해 안마해주기. | MILD | 0.0 |
| default_mission_mild_008 | default | MISSION | SELF | 만약에... | "만약 10억이 생긴다면 가장 먼저 하고 싶은 것은?"에 답하기. | MILD | 0.0 |
| default_mission_mild_009 | default | MISSION | ALL | 손병호 게임 | "손병호 게임" 시작! 5개 손가락을 가장 먼저 다 접는 사람이 패배. (패배: 1샷) | MILD | 1.0 |
| default_mission_mild_010 | default | MISSION | SELF | 영화 명대사 | 아는 영화 명대사를 성대모사하며 따라해보세요. (실패: 1샷) | MILD | 1.0 |
| default_penalty_mild_004 | default | PENALTY | SELF | 간지럽히기 | 양 옆 사람에게 10초간 간지럼 공격 당하기. | MILD | 0.0 |
| default_penalty_mild_005 | default | PENALTY | SELF | 윙크 발사 | 여기 있는 모든 사람과 눈을 마주치며 윙크하기. | MILD | 0.0 |
| default_safe_mild_003 | default | SAFE | SELF | 다음 턴 스킵 | 이번 턴은 쉬어갑니다. 다음 턴에 만나요! | MILD | 0.0 |
| default_safe_mild_004 | default | SAFE | SELF | 천사 카드 | 다음 벌칙을 다른 사람에게 넘길 수 있습니다. (1회용) | MILD | 0.0 |
| default_rule_mild_002 | default | RULE | ALL | 특정 행동 금지 | 다음 턴까지 '다리 꼬기' 금지. 어길 시 1샷. | MILD | 1.0 |
| default_event_mild_002 | default | EVENT | SELF | 워프 | 보드판에서 원하는 칸으로 즉시 이동할 수 있습니다. (출발점 제외) | MILD | 0.0 |
| default_mission_normal_004 | default | MISSION | SELF | TMI 방출 | 사람들이 아무도 궁금해하지 않을 나만의 TMI(Too Much Information)를 1가지 공개하세요. | NORMAL | 0.0 |
| default_mission_normal_005 | default | MISSION | TARGET_ONE | 술 따라주기 | 오른쪽 사람의 잔이 비었다면, 공손하게 술을 따라주세요. | NORMAL | 0.0 |
| default_mission_normal_006 | default | MISSION | SELF | 진실 혹은 도전 | "진실"을 선택하면 비밀 질문에 답하고, "도전"을 선택하면 어려운 미션을 수행합니다. | NORMAL | 1.5 |
| default_penalty_normal_004 | default | PENALTY | SELF | 2배 마시기 | 축하합니다! 벌칙이 2배가 되었습니다. 2샷 원샷! | NORMAL | 2.0 |
| default_penalty_normal_005 | default | PENALTY | TARGET_ONE | 지목 흑기사 | 한 명을 지목해 나의 흑기사로 만드세요. 그 사람이 대신 마십니다. | NORMAL | 1.0 |
| default_rule_normal_002 | default | RULE | ALL | 끝말잇기 | 현재 플레이어부터 끝말잇기 시작! 가장 먼저 막히는 사람이 1샷. | NORMAL | 1.0 |
| default_event_normal_002 | default | EVENT | ALL | 카드 셔플 | 모든 카드 덱을 다시 섞습니다. 새로운 판이 시작됩니다! | NORMAL | 0.0 |
| default_mission_spicy_003 | default | MISSION | SELF | 단톡방에 메시지 보내기 | 현재 단톡방에 "나 사실 너 좋아해" 라고 보내기. (5분 뒤 해명 필수) (실패: 3샷) | SPICY | 3.0 |
| default_mission_spicy_004 | default | MISSION | TARGET_ONE | 당연하지 게임 | 지목한 사람과 "당연하지" 게임 시작! 먼저 "당연하지"를 외치지 못하면 패배. (패배: 2샷) | SPICY | 2.0 |
| default_penalty_spicy_003 | default | PENALTY | SELF | 섹시 댄스 | 노래 한 곡에 맞춰 혼신의 힘을 다해 섹시 댄스를 추세요. | SPICY | 1.0 |
| default_penalty_spicy_004 | default | PENALTY | ALL_EXCEPT_SELF | 왕따주 | 본인을 제외한 모든 사람이 당신을 위해 짠~ 하고 마셔줍니다. | SPICY | 1.0 |
| default_rule_spicy_002 | default | RULE | ALL | 호칭 통일 | 다음 턴까지 모두 서로를 "자기야"라고 부르기. 어길 시 1샷. | SPICY | 1.0 |
| default_event_spicy_002 | default | EVENT | ALL | 진실의 시간 | 5분 동안 모든 질문에 오직 진실만을 말해야 합니다. 거짓말 탐지기 빙의! | SPICY | 0.0 |
| ... | ... | ... | ... | ... | ... | ... | ... |

(이하 150개의 카드는 위와 같은 형식으로 다양한 아이디어를 조합하여 생성됩니다. 전체 목록은 최종 산출물에 포함될 예정입니다.)

---

**[다음 단계]**

기본 카드팩의 콘텐츠 제작이 완료되었습니다. 다음 단계는 이 모든 개발 가이드 문서들을 하나로 통합하고, 최종적으로 사용자에게 전달할 **최종 개발 가이드 문서 통합 및 전달**입니다. 다른 테마(순한맛, 매운맛, 커플용 등)의 카드팩은 MVP 이후 확장 기능으로, 필요시 추가 기획 및 제작될 수 있습니다.

(이하 150개 카드 목록)

| cardId | cardPackId | cardType | targetType | title | description | severity | penaltyScale |
|---|---|---|---|---|---|---|---|
| default_mission_mild_011 | default | MISSION | SELF | 최근 본 영상 | 최근에 본 유튜브 영상 중 가장 재미있었던 것을 소개하고 추천 이유를 말하세요. | MILD | 0.0 |
| default_mission_mild_012 | default | MISSION | SELF | 학창시절 별명 | 학창시절 별명과 그 이유를 설명하세요. 없었다면 지금 즉석에서 하나 지어보세요. | MILD | 0.0 |
| default_mission_mild_013 | default | MISSION | TARGET_ONE | 하이파이브 | 왼쪽 사람과 하이파이브를 하고, 서로에게 힘이 되는 말을 한마디씩 건네세요. | MILD | 0.0 |
| default_mission_mild_014 | default | MISSION | SELF | 스트레칭 전도사 | 모두가 따라할 수 있는 간단한 스트레칭 동작을 30초간 알려주세요. | MILD | 0.0 |
| default_mission_mild_015 | default | MISSION | ALL | 눈치 게임 | 1부터 순서대로 숫자를 외치세요. 동시에 외치거나 마지막에 외치는 사람이 패배! (패배: 1샷) | MILD | 1.0 |
| default_penalty_mild_006 | default | PENALTY | SELF | 엉덩이로 이름쓰기 | 진지한 표정으로 본인의 이름을 엉덩이로 쓰세요. | MILD | 0.0 |
| default_penalty_mild_007 | default | PENALTY | SELF | 다음 턴까지 동물 흉내 | 다음 자신의 턴이 올 때까지 말 끝마다 동물 소리(e.g., 멍멍, 야옹)를 내야 합니다. | MILD | 1.0 |
| default_penalty_mild_008 | default | PENALTY | SELF | 코끼리코 5바퀴 | 제자리에서 코끼리코 5바퀴를 돌고, 정면을 향해 멋진 포즈를 취하세요. | MILD | 0.0 |
| default_safe_mild_005 | default | SAFE | SELF | 안주 섭취권 | 축하합니다! 안주 하나를 마음껏 드세요. | MILD | 0.0 |
| default_safe_mild_006 | default | SAFE | SELF | 건배사 면제 | 다음 건배사 제의 기회가 와도 하지 않아도 됩니다. | MILD | 0.0 |
| default_rule_mild_003 | default | RULE | ALL | 외래어 사용 금지 | 다음 턴까지 순우리말과 한자어만 사용하세요. 외래어 사용 시 1샷. | MILD | 1.0 |
| default_event_mild_003 | default | EVENT | ALL | 화장실 타임 | 공식적인 화장실 시간입니다. 3분간 휴식! | MILD | 0.0 |
| default_mission_normal_007 | default | MISSION | SELF | 휴대폰 사진 공개 | 휴대폰 갤러리에서 7번째 사진을 모두에게 공개하고 설명하세요. | NORMAL | 0.0 |
| default_mission_normal_008 | default | MISSION | TARGET_ONE | 이상형 인터뷰 | 지목한 사람에게 이상형에 대한 질문 3가지를 하세요. (e.g., 키, 성격, 연예인) | NORMAL | 0.0 |
| default_mission_normal_009 | default | MISSION | SELF | 소주병 세우기 | 소주병, 맥주병 등 병뚜껑을 손가락으로 쳐서 세우는 기술에 도전하세요. (실패: 1샷) | NORMAL | 1.0 |
| default_penalty_normal_006 | default | PENALTY | SELF | 양 옆 사람과 짠 | 양 옆 사람과 잔을 부딪히고 다 함께 원샷! | NORMAL | 1.0 |
| default_penalty_normal_007 | default | PENALTY | SELF | 진실의 미간 | 양 옆 사람이 30초 동안 웃기기. 웃으면 1샷 추가! | NORMAL | 1.0 |
| default_penalty_normal_008 | default | PENALTY | TARGET_ONE | 벌칙 대리인 | 한 명을 지목하여 이번 벌칙을 대신 수행하게 하세요. | NORMAL | 1.0 |
| default_rule_normal_003 | default | RULE | ALL | 웃음 참기 | 다음 턴까지 절대 웃으면 안됩니다. 웃는 사람은 1샷! | NORMAL | 1.0 |
| default_event_normal_003 | default | EVENT | ALL | 안주 만들기 | 다 함께 간단한 안주(e.g., 과일 깎기, 라면 끓이기)를 만들어 오세요. | NORMAL | 0.0 |
| default_mission_spicy_005 | default | MISSION | SELF | 전 애인에게 한마디 | 카메라를 보고, 전 애인에게 영상 편지를 남기세요. "잘 지내니?" | SPICY | 0.0 |
| default_mission_spicy_006 | default | MISSION | TARGET_ONE | 빼빼로 게임 | 지목한 사람과 막대과자 게임에 도전하세요. 1cm 미만 성공 시 벌칙 면제. (실패: 2샷) | SPICY | 2.0 |
| default_penalty_spicy_005 | default | PENALTY | SELF | 랜덤 플레이 댄스 | 아무 노래나 틀고, 노래가 나오는 동안 무조건 춤을 춰야 합니다. | SPICY | 0.0 |
| default_penalty_spicy_006 | default | PENALTY | ALL | 의리 게임 2단계 | 모든 플레이어가 각자 잔에 술을 조금씩 붓습니다. 마지막으로 카드를 뽑은 사람이 원샷! | SPICY | 1.5 |
| default_rule_spicy_003 | default | RULE | ALL | 질문 금지 | 다음 턴까지 질문을 하는 사람은 무조건 1샷. (왜? 포함) | SPICY | 1.0 |
| default_event_spicy_003 | default | EVENT | ALL | 커플 매칭 | 즉석에서 남녀 플레이어를 짝지어 다음 턴까지 커플로 행동하게 하세요. | SPICY | 0.0 |
| default_mission_mild_016 | default | MISSION | SELF | MBTI 맞추기 | 본인의 MBTI를 밝히지 않고, 행동이나 말로 자신을 표현하여 다른 사람들이 맞추게 하세요. | MILD | 0.0 |
| default_mission_mild_017 | default | MISSION | TARGET_ONE | 이미지 게임 | "이 중에서 가장 ~할 것 같은 사람은?" 질문을 하고, 지목된 사람이 이유를 설명하게 하세요. | MILD | 0.0 |
| default_penalty_mild_009 | default | PENALTY | SELF | 다음 턴까지 기사 되기 | 다음 턴까지 말 끝에 "~옵니다", "~까" 등 사극 톤을 유지해야 합니다. (실패: 1샷) | MILD | 1.0 |
| default_safe_mild_007 | default | SAFE | SELF | 안주 뺏어먹기 | 다른 사람의 안주를 한 입 뺏어 먹을 수 있는 권한을 얻습니다. | MILD | 0.0 |
| default_mission_normal_010 | default | MISSION | SELF | 30초 스피치 | "내가 생각하는 행복이란?" 주제로 30초간 즉흥 연설을 하세요. | NORMAL | 0.0 |
| default_penalty_normal_009 | default | PENALTY | SELF | 이마에 글씨 쓰기 | 유성펜으로 이마에 원하는 글씨(2글자)를 쓰고 다음 턴까지 유지하세요. | NORMAL | 0.0 |
| default_rule_normal_004 | default | RULE | ALL | 아파트 게임 | 아파트 게임 시작! 가장 높은 층을 외치거나 가장 낮은 층을 외친 사람이 패배! (패배: 1샷) | NORMAL | 1.0 |
| default_mission_spicy_007 | default | MISSION | SELF | 이상형에게 고백 | 이성 플레이어 중 가장 마음에 드는 사람에게 진지하게 고백 멘트를 날리세요. | SPICY | 0.0 |
| default_penalty_spicy_007 | default | PENALTY | SELF | 19금 단어 말하기 | 5초 안에 19금 단어 3개를 빠르게 외치세요. (실패: 2샷) | SPICY | 2.0 |
| default_mission_mild_018 | default | MISSION | SELF | 10년 후 나에게 | 10년 후의 나에게 보내는 영상 편지를 30초간 찍어보세요. | MILD | 0.0 |
| default_penalty_mild_010 | default | PENALTY | SELF | 얼굴 낙서 | 원하는 사람 한 명에게 얼굴에 작은 낙서를 받으세요. | MILD | 0.0 |
| default_safe_mild_008 | default | SAFE | SELF | 한 턴 쉬기 | 피곤하시죠? 이번 턴은 편안하게 쉬세요. | MILD | 0.0 |
| default_mission_normal_011 | default | MISSION | SELF | 5초 토크 | "내가 가장 자신있는 신체 부위는?" 질문에 5초 안에 답하세요. (실패: 1샷) | NORMAL | 1.0 |
| default_penalty_normal_010 | default | PENALTY | SELF | 자동응답기 | 다음 턴까지 모든 질문에 "사랑합니다 고객님"으로만 대답해야 합니다. | NORMAL | 1.0 |
| default_mission_spicy_008 | default | MISSION | TARGET_ONE | 스킨십 찬스 | 지목한 사람과 10초간 손깍지를 끼고 있으세요. | SPICY | 0.0 |
| default_penalty_spicy_008 | default | PENALTY | SELF | SNS에 흑역사 사진 올리기 | 자신의 SNS에 10분간 흑역사 사진을 게시하세요. (실패: 3샷) | SPICY | 3.0 |
| default_mission_mild_019 | default | MISSION | ALL | 훈민정음 게임 | 자음 하나를 정하고, 그 자음이 들어간 단어를 돌아가면서 말하세요. 막히면 1샷! | MILD | 1.0 |
| default_penalty_mild_011 | default | PENALTY | SELF | 물 따르기 | 모든 사람의 잔에 물을 채워주세요. | MILD | 0.0 |
| default_safe_mild_009 | default | SAFE | SELF | 질문 거부권 | 다음 턴에 받는 질문에 "노코멘트"를 외칠 수 있습니다. | MILD | 0.0 |
| default_mission_normal_012 | default | MISSION | SELF | 초성 퀴즈 | 앱에서 제시하는 초성 단어를 10초 안에 맞추세요. (실패: 1샷) | NORMAL | 1.0 |
| default_penalty_normal_011 | default | PENALTY | SELF | 술자리 BGM 담당 | 다음 턴까지 모두가 좋아할 만한 술자리 BGM을 선정하여 재생하세요. | NORMAL | 0.0 |
| default_mission_spicy_009 | default | MISSION | SELF | 가장 야했던 경험 | 자신이 겪은 가장 야했던 경험을 수위를 조절하여 이야기하세요. | SPICY | 0.0 |
| default_penalty_spicy_009 | default | PENALTY | SELF | 애교 3종 세트 | "귀요미송"에 맞춰 율동을 하거나, 자신만의 애교 3종 세트를 보여주세요. | SPICY | 0.0 |
| default_mission_mild_020 | default | MISSION | SELF | 여행 자랑 | 가장 기억에 남는 여행지와 에피소드를 자랑해주세요. | MILD | 0.0 |
| default_penalty_mild_012 | default | PENALTY | SELF | 쌈 싸주기 | 옆 사람에게 정성껏 쌈을 싸서 먹여주세요. | MILD | 0.0 |
| default_safe_mild_010 | default | SAFE | SELF | 자리 유지권 | 다음 자리 바꾸기 이벤트에서 자리를 지킬 수 있습니다. | MILD | 0.0 |
| default_mission_normal_013 | default | MISSION | SELF | 만취 연기 | 1분간 만취한 사람처럼 행동하고 말하세요. 어색하면 1샷! | NORMAL | 1.0 |
| default_penalty_normal_012 | default | PENALTY | SELF | 투명인간 | 다음 턴까지 아무도 당신에게 말을 걸 수 없고, 당신도 말을 할 수 없습니다. | NORMAL | 0.0 |
| default_mission_spicy_010 | default | MISSION | TARGET_ONE | 첫키스 썰 풀기 | 지목한 사람의 첫키스 경험을 들어보세요. | SPICY | 0.0 |
| default_penalty_spicy_010 | default | PENALTY | SELF | 랜덤 전화 | 휴대폰 연락처에서 무작위로 한 명을 골라 1분간 통화하세요. | SPICY | 2.0 |
| default_mission_mild_021 | default | MISSION | SELF | 2025년 목표 | 2025년에 이루고 싶은 목표 3가지를 발표하세요. | MILD | 0.0 |
| default_penalty_mild_013 | default | PENALTY | SELF | 칭찬 감옥 | 1분 동안 모든 플레이어로부터 칭찬만 들어야 합니다. | MILD | 0.0 |
| default_safe_mild_011 | default | SAFE | SELF | 더블 찬스 | 다음 주사위를 굴릴 때, 한 번 더 굴릴 수 있는 기회를 얻습니다. | MILD | 0.0 |
| default_mission_normal_014 | default | MISSION | SELF | 인디언밥 | 모든 사람에게 등을 한 대씩 맞습니다. (인디언밥) | NORMAL | 0.0 |
| default_penalty_normal_013 | default | PENALTY | SELF | 마피아 게임 제안 | 지금 즉시 마피아 게임 한 판을 제안하고 사회자를 맡으세요. | NORMAL | 0.0 |
| default_mission_spicy_011 | default | MISSION | SELF | 이상형 외모 묘사 | 이 자리에 없는 사람 중, 자신의 이상형 외모를 구체적으로 묘사하세요. | SPICY | 0.0 |
| default_penalty_spicy_011 | default | PENALTY | SELF | 술잔 바꾸기 | 원하는 사람 한 명과 술잔을 바꿔서 마십니다. | SPICY | 1.0 |
| default_mission_mild_022 | default | MISSION | SELF | 넌센스 퀴즈 | 넌센스 퀴즈를 하나 내서, 아무도 못 맞추면 성공! (성공: 벌칙 면제) | MILD | 1.0 |
| default_penalty_mild_014 | default | PENALTY | SELF | 다음 사람 지목 | 다음 턴에 벌칙을 받을 사람을 미리 한 명 지목하세요. | MILD | 0.0 |
| default_safe_mild_012 | default | SAFE | SELF | 카드 다시 뽑기 | 마음에 들지 않는다면, 카드를 한 번 다시 뽑을 수 있습니다. | MILD | 0.0 |
| default_mission_normal_015 | default | MISSION | SELF | 라이어 게임 | 한 가지 주제를 정하고, 라이어 게임을 제안하여 사회자를 맡으세요. | NORMAL | 0.0 |
| default_penalty_normal_014 | default | PENALTY | SELF | 나를 따르라 | 다음 턴까지 모든 사람이 당신의 말과 행동을 똑같이 따라해야 합니다. | NORMAL | 0.0 |
| default_mission_spicy_012 | default | MISSION | TARGET_ONE | 비밀 공유 | 지목한 사람과 1분간 둘만의 비밀 이야기를 나누세요. | SPICY | 0.0 |
| default_penalty_spicy_012 | default | PENALTY | SELF | 술자리 ASMR | 술 따르는 소리, 잔 부딪히는 소리 등을 마이크에 대고 녹음하여 모두에게 들려주세요. | SPICY | 0.0 |
| default_mission_mild_023 | default | MISSION | SELF | 오늘의 TPO 설명 | 오늘 입은 옷의 TPO(시간, 장소, 상황) 컨셉을 설명하세요. | MILD | 0.0 |
| default_penalty_mild_015 | default | PENALTY | SELF | 아이 컨택 금지 | 다음 턴까지 누구와도 눈을 마주치면 안됩니다. | MILD | 1.0 |
| default_safe_mild_013 | default | SAFE | SELF | 안주 주문권 | 원하는 안주를 하나 주문할 수 있는 권한을 얻습니다. (비용은 N/1) | MILD | 0.0 |
| default_mission_normal_016 | default | MISSION | SELF | 유튜브 채널 추천 | 자신이 구독한 유튜브 채널 중 3가지를 추천하고 이유를 설명하세요. | NORMAL | 0.0 |
| default_penalty_normal_015 | default | PENALTY | SELF | 한 발 들고 서 있기 | 다음 턴까지 한 발을 들고 있어야 합니다. 발이 땅에 닿으면 1샷. | NORMAL | 1.0 |
| default_mission_spicy_013 | default | MISSION | SELF | 플레이어 외모 순위 | 이성 플레이어들의 외모 순위를 정하고 그 이유를 설명하세요. | SPICY | 0.0 |
| default_penalty_spicy_013 | default | PENALTY | SELF | 카톡 프사 바꾸기 | 엽기적인 사진으로 10분간 카카오톡 프로필 사진을 바꾸세요. | SPICY | 2.0 |
| default_mission_mild_024 | default | MISSION | SELF | 닉네임 변경 | 다음 게임이 끝날 때까지 사용할 새로운 닉네임을 정하세요. | MILD | 0.0 |
| default_penalty_mild_016 | default | PENALTY | SELF | 손 머리 위로 | 다음 턴까지 양손을 머리 위로 올리고 있어야 합니다. | MILD | 1.0 |
| default_safe_mild_014 | default | SAFE | SELF | 흑기사 소환권 | 흑기사/흑장미를 1회 소환할 수 있습니다. | MILD | 0.0 |
| default_mission_normal_017 | default | MISSION | SELF | 좀비 게임 | 좀비 게임을 제안하고 첫 좀비가 되세요. | NORMAL | 0.0 |
| default_penalty_normal_016 | default | PENALTY | SELF | 바닥에 눕기 | 30초간 바닥에 편안하게 누워 천장을 바라보세요. | NORMAL | 0.0 |
| default_mission_spicy_014 | default | MISSION | TARGET_ONE | 결혼 상대 고르기 | 이성 플레이어 중 단 한 명과 결혼해야 한다면 누구를 선택할지 발표하세요. | SPICY | 0.0 |
| default_penalty_spicy_014 | default | PENALTY | SELF | 상의 탈의 | 1분간 상의를 탈의하고 있으세요. (분위기에 따라 조절) | SPICY | 3.0 |
| default_mission_mild_025 | default | MISSION | SELF | 릴스 챌린지 | 최근 유행하는 릴스 챌린지 춤을 15초간 춰보세요. | MILD | 1.0 |
| default_penalty_mild_017 | default | PENALTY | SELF | 압존법 마스터 | 다음 턴까지 모든 사람에게 완벽한 압존법을 사용해야 합니다. | MILD | 1.0 |
| default_safe_mild_015 | default | SAFE | SELF | BGM 변경권 | 현재 재생 중인 배경음악을 원하는 곡으로 바꿀 수 있습니다. | MILD | 0.0 |
| default_mission_normal_018 | default | MISSION | SELF | 인생네컷 포즈 | 즉석에서 인생네컷 사진을 찍는 것처럼 4가지 포즈를 취하세요. | NORMAL | 0.0 |
| default_penalty_normal_017 | default | PENALTY | SELF | 물티슈로 발 닦기 | 물티슈로 자신의 발을 정성껏 닦으세요. | NORMAL | 0.0 |
| default_mission_spicy_015 | default | MISSION | SELF | 카톡 친구 수 공개 | 자신의 카카오톡 친구 수를 모두에게 공개하세요. | SPICY | 0.0 |
| default_penalty_spicy_015 | default | PENALTY | SELF | 파트너 정하기 | 다음 게임이 끝날 때까지 함께할 파트너를 정하고, 벌칙을 공유하세요. | SPICY | 1.0 |
| default_mission_mild_026 | default | MISSION | SELF | 그림 퀴즈 | 30초 안에 단어를 그림으로 설명하여 다른 사람들이 맞추게 하세요. | MILD | 1.0 |
| default_penalty_mild_018 | default | PENALTY | SELF | 설거지 당번 | 오늘의 설거지 당번으로 임명됩니다. | MILD | 0.0 |
| default_safe_mild_016 | default | SAFE | SELF | 자리 선택권 | 다음 턴에 원하는 자리로 이동할 수 있습니다. | MILD | 0.0 |
| default_mission_normal_019 | default | MISSION | SELF | 꼰대처럼 말하기 | "나 때는 말이야~"로 시작하는 꼰대 연기를 1분간 선보이세요. | NORMAL | 0.0 |
| default_penalty_normal_018 | default | PENALTY | SELF | 묵언수행 | 다음 턴까지 말을 하지 않고, 오직 몸짓으로만 소통해야 합니다. | NORMAL | 1.0 |
| default_mission_spicy_016 | default | MISSION | TARGET_ONE | 플러팅 멘트 | 지목한 사람에게 세상에서 가장 느끼한 플러팅 멘트를 날리세요. | SPICY | 0.0 |
| default_penalty_spicy_016 | default | PENALTY | SELF | 폰 압수 | 다음 턴까지 휴대폰을 사회자에게 맡깁니다. | SPICY | 0.0 |
| default_mission_mild_027 | default | MISSION | SELF | 1분 안에 웃기기 | 1분 안에 플레이어 중 한 명이라도 웃기면 성공! (실패: 1샷) | MILD | 1.0 |
| default_penalty_mild_019 | default | PENALTY | SELF | 시낭송 | 아무 시나 한 편 찾아서 감정을 담아 낭송하세요. | MILD | 0.0 |
| default_safe_mild_017 | default | SAFE | SELF | 벌칙 절반 | 다음 벌칙은 절반만 수행합니다. (e.g., 1샷 -> 반샷) | MILD | 0.0 |
| default_mission_normal_020 | default | MISSION | SELF | 콜라 원샷 | 술 대신 콜라 500ml를 원샷하세요. | NORMAL | 0.0 |
| default_penalty_normal_019 | default | PENALTY | SELF | 다음 안주 쏘기 | 다음 안주는 당신이 삽니다! | NORMAL | 0.0 |
| default_mission_spicy_017 | default | MISSION | SELF | 가장 비싼 선물 | 이성에게 해준 가장 비싼 선물이 무엇인지 공개하세요. | SPICY | 0.0 |
| default_penalty_spicy_017 | default | PENALTY | SELF | 노래방 18번 부르기 | 노래방 기기가 있다면, 자신의 18번을 열창하세요. 없다면 무반주로! | SPICY | 0.0 |
| default_mission_mild_028 | default | MISSION | SELF | MBTI 궁합 | 플레이어 중 자신과 가장 잘 맞는 MBTI 궁합을 가진 사람을 찾아보세요. | MILD | 0.0 |
| default_penalty_mild_020 | default | PENALTY | SELF | 사회자 되기 | 다음 3턴 동안 게임의 사회자가 되어 진행을 맡습니다. | MILD | 0.0 |
| default_safe_mild_018 | default | SAFE | SELF | 미션 변경권 | 마음에 들지 않는 미션을 다른 미션으로 바꿀 수 있습니다. | MILD | 0.0 |
| default_mission_normal_021 | default | MISSION | SELF | AI 성대모사 | AI 스피커(시리, 빅스비 등)의 목소리를 흉내내며 다음 턴까지 말하세요. | NORMAL | 1.0 |
| default_penalty_normal_020 | default | PENALTY | SELF | 인간 의자 | 1분 동안 옆 사람의 의자가 되어주세요. | NORMAL | 0.0 |
| default_mission_spicy_018 | default | MISSION | TARGET_ONE | 애인과 싸운 썰 | 지목한 사람의 최근 애인과 다툰 경험담을 들어보세요. | SPICY | 0.0 |
| default_penalty_spicy_018 | default | PENALTY | SELF | 귓속말 전파 | 한 사람에게 귓속말을 하고, 릴레이로 전달하여 마지막 사람이 맞추게 하세요. 틀리면 모두 1샷! | SPICY | 1.0 |
| default_mission_mild_029 | default | MISSION | SELF | 손금 자랑 | 자신의 손금을 보여주며 생명선, 두뇌선, 감정선을 자랑하세요. | MILD | 0.0 |
| default_penalty_mild_021 | default | PENALTY | SELF | 다음 턴까지 영어 이름 사용 | 다음 턴까지 모두를 영어 이름으로 불러야 합니다. | MILD | 1.0 |
| default_safe_mild_019 | default | SAFE | SELF | 지목권 | 다음 벌칙을 받을 사람을 지목할 수 있는 권한을 얻습니다. | MILD | 0.0 |
| default_mission_normal_022 | default | MISSION | SELF | 나를 맞춰봐 | 자신에 대한 질문 3가지를 내고, 다른 사람들이 맞추게 하세요. | NORMAL | 0.0 |
| default_penalty_normal_021 | default | PENALTY | SELF | 가방 공개 | 자신의 가방 속 소지품을 모두 꺼내 보여주세요. | NORMAL | 0.0 |
| default_mission_spicy_019 | default | MISSION | SELF | 가장 최근 통화 목록 공개 | 가장 최근 통화 목록 3개를 공개하고 누구와 통화했는지 설명하세요. | SPICY | 0.0 |
| default_penalty_spicy_019 | default | PENALTY | SELF | 눈 가리고 술 마시기 | 눈을 가린 채로 술을 마십니다. 흘리면 1샷 추가! | SPICY | 1.5 |
| default_mission_mild_030 | default | MISSION | SELF | 2024년 최고의 순간 | 2024년 한 해 동안 가장 행복했던 순간을 이야기하세요. | MILD | 0.0 |
| default_penalty_mild_022 | default | PENALTY | SELF | 발 마사지 | 옆 사람에게 1분간 발 마사지를 해주세요. | MILD | 0.0 |
| default_safe_mild_020 | default | SAFE | SELF | 왕 카드 | 당신은 왕입니다. 다음 턴까지 어떤 명령이든 한 번 내릴 수 있습니다. (순한맛) | MILD | 0.0 |
| default_mission_normal_023 | default | MISSION | SELF | 로봇 연기 | 다음 턴까지 로봇처럼 삐걱거리며 말하고 행동하세요. | NORMAL | 1.0 |
| default_penalty_normal_022 | default | PENALTY | SELF | 술상 정리 | 현재 술상을 깔끔하게 정리정돈 하세요. | NORMAL | 0.0 |
| default_mission_spicy_020 | default | MISSION | TARGET_ONE | 이상형에게 노래 불러주기 | 지목한 이상형을 위해 세레나데를 한 곡 불러주세요. | SPICY | 0.0 |
| default_penalty_spicy_020 | default | PENALTY | SELF | 폰 배경화면 바꾸기 | 엽기적인 사진으로 10분간 휴대폰 배경화면을 바꾸세요. | SPICY | 2.0 |

| cardId | cardPackId | cardType | targetType | title | description | severity | penaltyScale |
|---|---|---|---|---|---|---|---|
| default_mission_mild_031 | default | MISSION | SELF | ASMR | 과자 먹는 소리, 물 마시는 소리 등 ASMR을 30초간 들려주세요. | MILD | 0.0 |
| default_penalty_mild_023 | default | PENALTY | SELF | 다음 턴까지 반대로 말하기 | 다음 턴까지 "네"는 "아니오"로, "아니오"는 "네"로 대답해야 합니다. | MILD | 1.0 |
| default_safe_mild_021 | default | SAFE | SELF | 힌트 얻기 | 다음 미션에서 힌트를 하나 얻을 수 있습니다. | MILD | 0.0 |
| default_mission_normal_024 | default | MISSION | SELF | 유행어 3종 세트 | 요즘 유행하는 유행어 3가지를 선보이세요. | NORMAL | 0.0 |
| default_penalty_normal_023 | default | PENALTY | SELF | 묵찌빠 | 옆 사람과 묵찌빠 3판 2선승제를 해서, 지면 1샷! | NORMAL | 1.0 |
| default_mission_spicy_021 | default | MISSION | SELF | 가장 최근 검색어 공개 | 포털 사이트 가장 최근 검색어 3개를 공개하세요. | SPICY | 0.0 |
| default_penalty_spicy_021 | default | PENALTY | SELF | 모든 잔 채우기 | 모든 사람의 빈 잔에 술을 채워주세요. | SPICY | 0.0 |
| default_mission_mild_032 | default | MISSION | SELF | 반려동물 자랑 | 자신의 반려동물을 자랑하거나, 키우고 싶은 반려동물에 대해 이야기하세요. | MILD | 0.0 |
| default_penalty_mild_024 | default | PENALTY | SELF | 이모티콘 따라하기 | 카카오톡 이모티콘 중 하나를 골라 똑같이 따라하세요. | MILD | 0.0 |
| default_safe_mild_022 | default | SAFE | SELF | 벌칙 투표권 | 다음 벌칙을 받을 사람을 투표로 정할 수 있습니다. | MILD | 0.0 |
| default_mission_normal_025 | default | MISSION | SELF | 흑역사 BGM | 자신의 흑역사에 어울리는 BGM을 틀고 썰을 푸세요. | NORMAL | 0.0 |
| default_penalty_normal_024 | default | PENALTY | SELF | 물구나무 서기 | 10초간 물구나무 서기에 도전하세요. (주변의 도움 가능) | NORMAL | 0.0 |
| default_mission_spicy_022 | default | MISSION | TARGET_ONE | 가장 아찔했던 순간 | 지목한 사람의 인생에서 가장 아찔했던 순간의 경험담을 들어보세요. | SPICY | 0.0 |
| default_penalty_spicy_022 | default | PENALTY | SELF | 립스틱 바르기 | 립스틱을 바르고 다음 턴까지 유지하세요. | SPICY | 0.0 |
| default_mission_mild_033 | default | MISSION | SELF | 멍때리기 | 1분 동안 아무 생각 없이 멍을 때리세요. (움직이거나 웃으면 실패) | MILD | 1.0 |
| default_penalty_mild_025 | default | PENALTY | SELF | 토끼 귀 머리띠 | 다음 턴까지 토끼 귀 머리띠를 하고 있으세요. | MILD | 0.0 |
| default_safe_mild_023 | default | SAFE | SELF | 자리 바꾸기 방어권 | 다음 자리 바꾸기 이벤트에서 자리를 지킬 수 있습니다. | MILD | 0.0 |
| default_mission_normal_026 | default | MISSION | SELF | 복근 공개 | 복근이 있다면 살짝 공개하고, 없다면 가장 자신있는 다른 부위를 공개하세요. | NORMAL | 0.0 |
| default_penalty_normal_025 | default | PENALTY | SELF | 다음 게임 룰 정하기 | 다음 게임의 특별 룰을 하나 정할 수 있습니다. (e.g., 특정 단어 금지) | NORMAL | 0.0 |
| default_mission_spicy_023 | default | MISSION | SELF | 가장 최근 이성과의 카톡 | 가장 최근에 이성과 나눈 카톡 대화 3개를 공개하세요. | SPICY | 0.0 |
| default_penalty_spicy_023 | default | PENALTY | SELF | 러시안 룰렛 | 여러 잔 중 한 잔에만 소금물을 타고, 다 함께 골라 마시기. | SPICY | 1.0 |
| default_mission_mild_034 | default | MISSION | SELF | 오늘의 운세 | 앱으로 오늘의 운세를 보고 모두에게 알려주세요. | MILD | 0.0 |
| default_penalty_mild_026 | default | PENALTY | SELF | 다음 턴까지 한쪽 눈 감기 | 다음 턴까지 한쪽 눈을 감고 애꾸눈으로 지내야 합니다. | MILD | 1.0 |
| default_safe_mild_024 | default | SAFE | SELF | 벌칙 2배 넘기기 | 다음 벌칙을 2배로 만들어 다른 사람에게 넘길 수 있습니다. | MILD | 0.0 |
| default_mission_normal_027 | default | MISSION | SELF | 닭싸움 | 옆 사람과 닭싸움 한 판! 지는 사람이 1샷. | NORMAL | 1.0 |
| default_penalty_normal_026 | default | PENALTY | SELF | 편의점 다녀오기 | 5분 안에 편의점에 가서 원하는 물건 하나 사오기. (비용은 본인 부담) | NORMAL | 0.0 |
| default_mission_spicy_024 | default | MISSION | TARGET_ONE | 스킨십 레벨 | 지목한 사람과 가능한 스킨십의 최대 레벨을 말하세요. (e.g., 손잡기, 포옹) | SPICY | 0.0 |
| default_penalty_spicy_024 | default | PENALTY | SELF | 엉덩이 맞기 | 모든 사람에게 엉덩이를 한 대씩 맞습니다. | SPICY | 0.0 |
| default_mission_mild_035 | default | MISSION | SELF | 숨겨진 재능 | 자신의 숨겨진 재능(e.g., 혀로 체리 꼭지 묶기)을 보여주세요. | MILD | 0.0 |
| default_penalty_mild_027 | default | PENALTY | SELF | 다음 턴까지 콧소리 | 다음 턴까지 말할 때마다 콧소리를 섞어서 말해야 합니다. | MILD | 1.0 |
| default_safe_mild_025 | default | SAFE | SELF | 턴 순서 바꾸기 | 원하는 사람과 턴 순서를 바꿀 수 있습니다. | MILD | 0.0 |
| default_mission_normal_028 | default | MISSION | SELF | 팔씨름 | 이 중에서 가장 힘이 셀 것 같은 사람과 팔씨름 대결! | NORMAL | 1.0 |
| default_penalty_normal_027 | default | PENALTY | SELF | 마늘 먹기 | 생마늘 한 쪽을 쌈장 없이 먹으세요. | NORMAL | 0.0 |
| default_mission_spicy_025 | default | MISSION | SELF | 이성 유혹하기 | 1분 안에 이성 플레이어 중 한 명을 유혹하여 전화번호를 받아내세요. | SPICY | 2.0 |
| default_penalty_spicy_025 | default | PENALTY | SELF | 흑역사 영상 시청 | 자신의 흑역사가 담긴 영상을 모두와 함께 시청하세요. | SPICY | 0.0 |
| default_mission_mild_036 | default | MISSION | SELF | 버킷리스트 | 자신의 버킷리스트 3가지를 말하고, 그 중 하나를 고른 이유를 설명하세요. | MILD | 0.0 |
| default_penalty_mild_028 | default | PENALTY | SELF | 간장 종지에 술 마시기 | 다음 술은 간장 종지에 따라 마십니다. | MILD | 0.5 |
| default_safe_mild_026 | default | SAFE | SELF | 안주 독점권 | 3분 동안 자신만 안주를 먹을 수 있습니다. | MILD | 0.0 |
| default_mission_normal_029 | default | MISSION | SELF | 허벅지 씨름 | 원하는 사람과 허벅지 씨름 대결! | NORMAL | 1.0 |
| default_penalty_normal_028 | default | PENALTY | SELF | 2차 장소 정하기 | 오늘의 2차 장소를 책임지고 정하세요. | NORMAL | 0.0 |
| default_mission_spicy_026 | default | MISSION | TARGET_ONE | 이성에게 들었던 최악의 말 | 지목한 사람이 이성에게 들었던 가장 상처되는 말을 들어보세요. | SPICY | 0.0 |
| default_penalty_spicy_026 | default | PENALTY | SELF | 얼음물 등목 | 얼음물을 등에 부어 더위를 식히세요. | SPICY | 0.0 |
| default_mission_mild_037 | default | MISSION | SELF | 최애 캐릭터 | 인생에서 가장 좋아하는 캐릭터와 그 이유를 설명하세요. | MILD | 0.0 |
| default_penalty_mild_029 | default | PENALTY | SELF | 30초간 투명의자 | 30초 동안 투명의자 자세를 유지하세요. | MILD | 0.0 |
| default_safe_mild_027 | default | SAFE | SELF | 룰렛 돌리기 | 앱에서 제공하는 룰렛을 돌려 랜덤 벌칙/혜택을 받으세요. | MILD | 0.0 |
| default_mission_normal_030 | default | MISSION | SELF | 손바닥 밀치기 | 원하는 사람과 손바닥 밀치기 게임! | NORMAL | 1.0 |
| default_penalty_normal_029 | default | PENALTY | SELF | 롤모델 따라하기 | 자신의 롤모델처럼 1분간 말하고 행동하세요. | NORMAL | 0.0 |
| default_mission_spicy_027 | default | MISSION | SELF | 내가 바람을 핀다면? | 만약 내가 바람을 핀다면, 그 이유는 무엇일지 솔직하게 말해보세요. | SPICY | 0.0 |
| default_penalty_spicy_027 | default | PENALTY | SELF | 겨드랑이 박수 | 겨드랑이로 박수를 쳐서 멜로디를 연주하세요. | SPICY | 0.0 |
| default_mission_mild_038 | default | MISSION | SELF | 알람 소리 공개 | 자신의 휴대폰 알람 소리를 모두에게 들려주세요. | MILD | 0.0 |
| default_penalty_mild_030 | default | PENALTY | SELF | 다음 턴까지 무릎 꿇기 | 다음 턴까지 무릎을 꿇고 게임에 참여하세요. | MILD | 0.0 |
| default_safe_mild_028 | default | SAFE | SELF | 카드 2장 뽑기 | 다음 턴에 카드를 2장 뽑아 그 중 하나를 선택할 수 있습니다. | MILD | 0.0 |
| default_mission_normal_031 | default | MISSION | SELF | 병뚜껑 멀리 보내기 | 병뚜껑을 손가락으로 쳐서 가장 멀리 보낸 사람이 승리! 꼴찌가 1샷. | NORMAL | 1.0 |
| default_penalty_normal_030 | default | PENALTY | SELF | 계산기 | 다음 계산은 당신이 책임지고 하세요. (N/1) | NORMAL | 0.0 |
| default_mission_spicy_028 | default | MISSION | TARGET_ONE | 가장 최근 키스 | 지목한 사람의 가장 최근 키스는 언제, 어디서, 누구와 했는지 들어보세요. | SPICY | 0.0 |
| default_penalty_spicy_028 | default | PENALTY | SELF | 양말에 술 따라 마시기 | 자신의 양말을 벗어 그 안에 술을 따라 마시는 시늉을 하세요. | SPICY | 0.0 |
| default_mission_mild_039 | default | MISSION | SELF | 인생 영화 | 내 인생 최고의 영화 3편을 꼽고 그 이유를 설명하세요. | MILD | 0.0 |
| default_penalty_mild_031 | default | PENALTY | SELF | 다음 턴까지 존댓말 | 다음 턴까지 모든 사람에게 극존칭을 사용해야 합니다. | MILD | 1.0 |
| default_safe_mild_029 | default | SAFE | SELF | 벌칙 교환권 | 다른 사람과 벌칙을 교환할 수 있습니다. | MILD | 0.0 |
| default_mission_normal_032 | default | MISSION | SELF | 눈싸움 | 원하는 사람과 눈싸움을 해서, 먼저 눈을 깜빡이면 패배! (패배: 1샷) | NORMAL | 1.0 |
| default_penalty_normal_031 | default | PENALTY | SELF | 심부름꾼 | 10분 동안 이 모임의 공식 심부름꾼이 됩니다. | NORMAL | 0.0 |
| default_mission_spicy_029 | default | MISSION | SELF | 이성에게 어필하기 | 1분 동안 자신의 매력을 어필하여 이성 플레이어의 마음을 사로잡으세요. | SPICY | 0.0 |
| default_penalty_spicy_029 | default | PENALTY | SELF | 폰 연락처 5번째 사람에게 전화 | 휴대폰 연락처 5번째 사람에게 전화해서 "사랑해"라고 말하기. | SPICY | 3.0 |
| default_mission_mild_040 | default | MISSION | SELF | 어릴 적 꿈 | 어릴 적 나의 꿈은 무엇이었는지 이야기해주세요. | MILD | 0.0 |
| default_penalty_mild_032 | default | PENALTY | SELF | 5분간 막내 되기 | 5분 동안 이 모임의 막내가 되어, 선배들의 말을 잘 따르세요. | MILD | 0.0 |
| default_safe_mild_030 | default | SAFE | SELF | 황금열쇠 | 축하합니다! 어떤 벌칙이든 한번 무효화할 수 있는 황금열쇠를 얻었습니다. | MILD | 0.0 |
| default_mission_normal_033 | default | MISSION | SELF | 물병 세우기 | 물이 반쯤 남은 물병을 던져서 세우는 데 도전하세요. (3번 기회, 실패: 1샷) | NORMAL | 1.0 |
| default_penalty_normal_032 | default | PENALTY | SELF | 1분 스피치 | "내가 대통령이 된다면?" 주제로 1분간 연설하세요. | NORMAL | 0.0 |
| default_mission_spicy_030 | default | MISSION | TARGET_ONE | 스킨십 벌칙 정하기 | 지목한 사람에게 어울리는 스킨십 벌칙을 하나 정해주세요. | SPICY | 0.0 |
| default_penalty_spicy_030 | default | PENALTY | SELF | 소금물 원샷 | 소금물을 진하게 타서 원샷하세요. | SPICY | 0.0 |
