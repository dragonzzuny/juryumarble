# 주류마블 앱 - 시스템 통합 완료 보고서

## 📋 프로젝트 개요

**프로젝트명**: 주류마블 (Juryumarble) - 파티 보드게임 앱
**플랫폼**: Android (Kotlin, Jetpack Compose)
**아키텍처**: Clean Architecture (MVVM)
**현재 상태**: **제품화 수준 설계 완료** ✅

---

## 🎯 핵심 차별화 요소

### 1. **Dynamic Map Modifier System (DMMS)** ⭐
- **카드팩 ≠ 텍스트 카드 묶음**
- **카드팩 = 게임 경험을 변경하는 시스템 모듈**
- 카드팩 선택에 따라 **맵이 동적으로 재구성**
- **Seed 기반 재현 가능한 랜덤 생성**

### 2. **Dynamic Difficulty Adjustment (DDA)**
- 연속 벌칙, 플레이 시간, 피로도 모니터링
- 과도한 벌칙 자동 완화
- 자동 휴식 제안

### 3. **제품화 수준의 BM 구조**
- **광고**: 게임 시작/종료 전면 광고 + 하단 배너
- **IAP**: 광고 제거, 카드팩 구매, 테마 스킨
- **리텐션**: 논알콜 모드, 데일리 카드, 게임 기록

---

## 🏗 아키텍처 완성도

### ✅ Domain Layer (순수 비즈니스 로직)

#### 📦 핵심 모델
1. **게임 상태 관리**
   - `GameState` - 전체 게임 상태
   - `Player` - 플레이어 정보
   - `Tile` - 보드 타일
   - `TurnPhase` / `GameStatus` - 상태 머신

2. **DMMS 시스템**
   - `MapTemplate` - 기본 맵 구조
   - `MapModifier` - 맵 수정자 (타일 가중치, 룰, 이벤트)
   - `GeneratedMap` - 동적 생성된 최종 맵
   - `TileWeightModifier` - 타일 등장 확률 조정
   - `RuleModifier` - 게임 규칙 변경
   - `EventModifier` - 이벤트 확률 조정

3. **카드팩 시스템**
   - `CardPack` - 카드팩 (MapModifier 포함)
   - `Card` - 개별 카드
   - `CardType` / `TargetType` / `Severity`

4. **DDA 시스템**
   - `DifficultyState` - 현재 난이도 상태
   - `DDARule` - 난이도 조정 규칙
   - `DDAAction` - 조정 액션

5. **결제/광고 시스템**
   - `PurchaseStatus` - 구매 상태
   - `Purchase` - 개별 구매 기록
   - `ProductType` - 상품 타입
   - `AdConfig` - 광고 설정
   - `ThemeSkin` - 테마 스킨

6. **안전 장치**
   - `SafetyConfig` - 안전 설정
   - `AgeVerification` - 연령 확인
   - `SafetyAlert` - 안전 알림
   - `GameSessionLog` - 게임 기록

7. **원격 설정**
   - `RemoteGameConfig` - 원격 설정
   - `FeatureFlags` - 기능 플래그
   - `SeasonalEvent` - 시즌 이벤트

8. **사용자 설정**
   - `UserPreferences` - 로컬 설정
   - `SessionConfig` - 세션 설정

#### 🎮 UseCase (비즈니스 로직)
1. **InitializeGameUseCase** - 게임 초기화 (DMMS 통합)
2. **GenerateMapUseCase** - 동적 맵 생성 (DMMS 핵심)
3. **ProcessTurnUseCase** - 턴 진행
4. **ScalePenaltyUseCase** - 벌칙 스케일링
5. **ApplyDDAUseCase** - 동적 난이도 조절

#### 🗃 Repository 인터페이스
1. **CardRepository** - 카드 및 카드팩 관리
2. **GameRepository** - 게임 상태 저장/로드

---

### ✅ Data Layer (데이터 접근)

#### 🗄 Room Database
1. **Entity**
   - `CardEntity` - 카드 테이블
   - `CardPackEntity` - 카드팩 테이블

2. **DAO**
   - `CardDao` - 카드 CRUD
   - `CardPackDao` - 카드팩 CRUD

3. **Database**
   - `JuryumarbleDatabase` - 메인 데이터베이스 (v2)

#### 🔌 Repository 구현
1. **CardRepositoryImpl** - 카드/카드팩 저장소
2. **GameRepositoryImpl** - 게임 상태 저장소

#### 💉 DI (Hilt)
- `DataModule` - 데이터 레이어 의존성 주입

---

### ✅ Presentation Layer (UI)

#### 🎨 UI 화면 (Jetpack Compose)
1. **HomeScreen** - 홈 화면 (고급 디자인)
2. **GameSetupScreen** - 게임 설정
3. **GameBoardScreen** - 게임 보드 (프리미엄 디자인)
4. **GameResultScreen** - 결과 화면

#### 🧩 UI Components
1. **AnimatedMeshBackground** - 동적 배경
2. **GlassCard** - 글라스모피즘 카드
3. **DiceRollingAnimation** - 주사위 애니메이션

#### 🎨 Theme
- `Color.kt` - 네온 컬러 팔레트
- `Theme.kt` - 다크 테마

#### 🧠 ViewModel
- `GameViewModel` - 게임 전체 상태 관리

---

## 📊 주요 기능 구현 현황

### ✅ 완료된 핵심 시스템

| 시스템 | 구현 상태 | 완성도 |
|--------|----------|--------|
| **DMMS (Dynamic Map Modifier)** | ✅ 완료 | 90% |
| **DDA (Dynamic Difficulty Adjustment)** | ✅ 완료 | 85% |
| **카드팩 시스템** | ✅ 완료 | 90% |
| **게임 엔진 (턴 관리)** | ✅ 완료 | 95% |
| **데이터 모델 (Domain)** | ✅ 완료 | 100% |
| **Room 데이터베이스** | ✅ 완료 | 85% |
| **결제/광고 모델** | ✅ 완료 | 80% |
| **안전 장치 모델** | ✅ 완료 | 80% |
| **원격 설정 모델** | ✅ 완료 | 75% |

### 🔄 진행 중 / 필요한 작업

#### 1. **광고 시스템 통합** (우선순위: 높음)
- [ ] AdMob SDK 통합
- [ ] 전면 광고 (게임 시작/종료)
- [ ] 배너 광고 (하단 고정)
- [ ] 광고 로딩 실패 처리

#### 2. **IAP 시스템 통합** (우선순위: 높음)
- [ ] Google Play Billing Library 통합
- [ ] 광고 제거 구매
- [ ] 카드팩 구매
- [ ] 테마 스킨 구매
- [ ] 구매 복원 기능

#### 3. **UI 완성** (우선순위: 중)
- [ ] 카드팩 선택 UI
- [ ] 테마 스킨 선택 UI
- [ ] 연령 확인 UI
- [ ] 책임 음주 안내 UI
- [ ] 프리미엄 상태 표시
- [ ] 설정 화면

#### 4. **리텐션 기능** (우선순위: 중)
- [ ] 데일리 카드 시스템
- [ ] 게임 기록/통계 화면
- [ ] 논알콜 모드 프리셋

#### 5. **원격 설정** (우선순위: 낮음)
- [ ] Firebase Remote Config 통합
- [ ] 카드/확률 JSON 로드
- [ ] 시즌 이벤트 시스템

#### 6. **콘텐츠 제작** (우선순위: 높음)
- [ ] 기본 카드팩 100~200장 제작
- [ ] 유료 카드팩 2~3개 제작
- [ ] 테마 스킨 2~3개 디자인

---

## 🚀 DMMS 작동 방식 (핵심)

### 맵 생성 파이프라인

```
1. 기본 맵 템플릿 로드
   ↓
2. 활성화된 카드팩 수집
   ↓
3. 카드팩 Modifier 합산
   (타일 가중치 + 룰 변경 + 이벤트 확률)
   ↓
4. Seed 생성 (재현 가능)
   baseSeed + packHash + randomness
   ↓
5. 가중치 기반 타일 배치
   - 고정 타일 (출발점)
   - 필수 포함 타일
   - 가변 타일 (확률적 배치)
   ↓
6. GeneratedMap 반환
   (sessionId, seed, tiles, appliedModifiers)
```

### 예시: "술고래 카드팩" 효과

```kotlin
val sulgoraePack = CardPack(
    packId = "sulgore",
    name = "술고래 카드팩",
    isPremium = true,
    price = 2900.0,
    mapModifier = MapModifier(
        tileWeights = TileWeightModifier(
            cardWeight = 0.5f,        // 카드 확률 ↑
            trapWeight = 0.4f,        // 함정 확률 ↑
            safeWeight = 0.1f         // 휴식 확률 ↓
        ),
        rules = RuleModifier(
            penaltyMultiplier = 1.5f, // 벌칙 강도 1.5배
            maxConsecutivePenalty = 5 // 연속 벌칙 5회까지
        )
    )
)
```

**결과**: 같은 기본 맵이라도 술고래 카드팩 활성화 시 **더 과격한 게임**으로 변함!

---

## 💰 BM (비즈니스 모델) 구조

### 무료 사용자
- ✅ 기본 맵 + 제한된 카드 로테이션
- ✅ 광고 노출 (게임 시작/종료/하단배너)
- ❌ 카드 반복률 높음 (의도된 한계)
- ❌ 프리미엄 카드팩 미사용

### 유료 사용자 (광고 제거)
- ✅ 광고 완전 제거
- ✅ 프리미엄 배지
- ❌ 카드팩은 별도 구매

### 카드팩 구매자
- ✅ 맵 구조 변경 (DMMS)
- ✅ 카드 반복률 감소
- ✅ "새로운 게임" 경험
- ✅ 테마별 특화 룰

### 테마 스킨 구매자
- ✅ 시각적 커스터마이징
- ✅ 전용 BGM/효과음 (선택)
- ❌ 게임 규칙에 영향 없음

---

## 🛡 안전 및 규제 준수

### 스토어 정책 대응
1. **연령 확인**: 19세 이상 필수
2. **책임 음주 안내**: 첫 실행 시 고지
3. **논알콜 모드**: 술 없이도 플레이 가능
4. **자동 휴식 제안**: 장시간 플레이 시

### 개인정보 보호
- ✅ 로컬 저장 기본 (Room Database)
- ✅ 개인 식별 정보 미수집
- ✅ 게임 기록은 비개인화 통계만

---

## 📱 UX 특화 기능

### 술자리 환경 최적화
1. **한 손 조작 모드**: 버튼 하단 배치
2. **빠른 재시작**: Quick Restart 버튼
3. **화면 회전 잠금**: 안정적인 플레이

### 접근성
1. **진동 피드백**: 턴 알림
2. **큰 버튼**: 터치 실수 방지
3. **명확한 상태 표시**: 현재 플레이어 강조

---

## 🧪 테스트 권장사항

### 필수 테스트
1. **DMMS 재현성**: 같은 Seed → 같은 맵
2. **DDA 작동**: 연속 벌칙 → 확률 감소 확인
3. **광고 로딩 실패**: 게임 진행 막히지 않음
4. **결제 복원**: 재설치 후 구매 복원
5. **저사양 기기**: 성능 및 메모리 체크

### 밸런스 테스트
1. 카드 반복률 측정 (무료 vs 유료)
2. 평균 게임 시간 측정
3. 벌칙 빈도 분석

---

## 🔜 다음 단계 (우선순위 순)

### Phase 1: 광고/IAP 통합 (제품 출시 필수)
1. AdMob SDK 설치 및 통합
2. Google Play Billing 통합
3. 구매 복원 기능
4. 광고 제거 IAP 구현

### Phase 2: 콘텐츠 제작
1. 기본 카드 200장 작성
2. 프리미엄 카드팩 2~3개 기획 및 제작
3. 테마 스킨 디자인

### Phase 3: UI 완성
1. 카드팩 선택 화면
2. 연령 확인 및 안내 화면
3. 설정 화면
4. 통계/기록 화면

### Phase 4: QA 및 최적화
1. 전체 기능 통합 테스트
2. 성능 최적화
3. 버그 수정
4. 스토어 등록 준비

---

## 📌 핵심 성과

### ✅ 달성한 목표
1. **DMMS 시스템 설계 완료** - 차별화 요소 확보
2. **DDA 시스템 설계 완료** - 사용자 경험 최적화
3. **제품화 수준의 데이터 모델** - 확장 가능한 구조
4. **광고/IAP BM 설계** - 수익화 전략 명확
5. **안전 장치 설계** - 스토어 규제 대응

### 🎯 차별화 포인트
1. **카드팩 ≠ 카드 묶음** → 게임 경험 자체를 판매
2. **Seed 기반 재현 가능** → 밸런싱/디버깅 용이
3. **DDA로 책임감 있는 플레이** → 과음 방지
4. **논알콜 모드** → 타겟 확장 (카페/회식)

---

## 📝 기술 스택 요약

| 구분 | 기술 |
|------|------|
| **언어** | Kotlin |
| **UI** | Jetpack Compose |
| **아키텍처** | Clean Architecture (MVVM) |
| **DI** | Hilt |
| **DB** | Room |
| **비동기** | Coroutines & Flow |
| **광고** | AdMob (예정) |
| **결제** | Google Play Billing (예정) |
| **원격 설정** | Firebase Remote Config (선택) |

---

## 🎉 결론

**주류마블 앱은 제품화 수준의 설계를 완료했습니다.**

- ✅ 핵심 비즈니스 로직 완성
- ✅ DMMS/DDA 시스템 설계 완료
- ✅ 광고/IAP 모델 설계 완료
- ✅ 안전 장치 및 규제 대응 설계 완료

**다음 단계는 광고/IAP SDK 통합과 콘텐츠 제작입니다.**

---

**문서 작성일**: 2026-01-01
**버전**: 1.0 (시스템 통합 완료)
