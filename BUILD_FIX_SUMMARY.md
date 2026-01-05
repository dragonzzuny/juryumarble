# 빌드 에러 수정 요약

## ✅ 수정 완료된 사항

### 1. Tile 모델 - position 필드 추가
**파일**: `domain/src/main/java/com/manus/juryumarble/domain/model/Tile.kt`
**변경**: `id` → `position`으로 필드명 변경

```kotlin
// Before
data class Tile(
    val id: Int,
    ...
)

// After
data class Tile(
    val position: Int,  // 보드상 위치
    ...
)
```

### 2. AppModule DI 수정
**파일**: `app/src/main/java/com/manus/juryumarble/di/AppModule.kt`
**추가**: `GenerateMapUseCase` Provider 및 의존성 주입

```kotlin
@Provides
@Singleton
fun provideGenerateMapUseCase(): GenerateMapUseCase {
    return GenerateMapUseCase()
}

@Provides
@Singleton
fun provideInitializeGameUseCase(
    cardRepository: CardRepository,
    generateMapUseCase: GenerateMapUseCase  // 추가
): InitializeGameUseCase {
    return InitializeGameUseCase(cardRepository, generateMapUseCase)
}
```

### 3. GameViewModel - SessionConfig 수정
**파일**: `app/src/main/java/com/manus/juryumarble/presentation/viewmodel/GameViewModel.kt`
**변경**: `cardPacks` → `activatedCardPackIds`

```kotlin
val config = SessionConfig(
    playerNames = _uiState.value.players,
    severityFilter = _uiState.value.selectedSeverity,
    activatedCardPackIds = listOf("default")  // 추가
)
```

### 4. CardPackEntity - 필드 추가
**파일**: `data/src/main/java/com/manus/juryumarble/data/local/model/CardPackEntity.kt`
**추가**: `isPremium`, `price`, `mapModifierJson` 필드

```kotlin
@Entity(tableName = "card_packs")
data class CardPackEntity(
    ...
    val isPremium: Boolean = false,
    val price: Double = 0.0,
    val mapModifierJson: String? = null
)
```

### 5. Room TypeConverters 추가
**파일**: `data/src/main/java/com/manus/juryumarble/data/local/db/Converters.kt` (신규)
**내용**: MapModifier ↔ JSON 변환

### 6. JuryumarbleDatabase - TypeConverters 등록
**파일**: `data/src/main/java/com/manus/juryumarble/data/local/db/JuryumarbleDatabase.kt`
**추가**: `@TypeConverters(Converters::class)`

### 7. CardRepositoryImpl - 변환 로직 업데이트
**파일**: `data/src/main/java/com/manus/juryumarble/data/repository/CardRepositoryImpl.kt`
**변경**: CardPackEntity ↔ CardPack 변환 시 MapModifier JSON 처리

### 8. GameRepositoryImpl - Import 정리
**파일**: `data/src/main/java/com/manus/juryumarble/data/repository/GameRepositoryImpl.kt`
**변경**: 불필요한 import 제거

### 9. UI Import 수정
**파일**:
- `app/src/main/java/com/manus/juryumarble/presentation/ui/screen/GameBoardScreen.kt`
- `app/src/main/java/com/manus/juryumarble/presentation/ui/screen/HomeScreen.kt`
**추가**: `import androidx.compose.ui.draw.blur`

### 10. libs.versions.toml 수정
**파일**: `gradle/libs.versions.toml`
**수정**: `[libraries]` 헤더 추가

---

## 🔍 빌드 확인 방법

### Android Studio 사용 시:
1. **File** → **Sync Project with Gradle Files**
2. **Build** → **Rebuild Project**
3. 에러 확인

### 명령줄 사용 시:
```bash
# Windows
gradlew.bat build

# Mac/Linux
./gradlew build
```

---

## ⚠️ 잠재적 에러

다음 에러들이 발생할 수 있습니다:

### 1. Gradle Wrapper 없음
**증상**: `./gradlew: No such file or directory`
**해결**: Android Studio에서 Gradle Sync 실행

### 2. Kotlin 버전 불일치
**증상**: `Kotlin version mismatch`
**해결**: `gradle/libs.versions.toml`에서 버전 확인

### 3. Hilt 컴파일 에러
**증상**: `Cannot find symbol: DaggerXXX`
**해결**: Rebuild Project 실행

---

## 📋 다음 단계

빌드가 성공하면:
1. ✅ 에뮬레이터/실제 기기에서 앱 실행
2. ✅ 기본 게임 플레이 테스트
3. ✅ 에러 로그 확인

빌드 실패 시:
1. 에러 메시지 전체 복사
2. 어느 파일의 몇 번째 줄인지 확인
3. 추가 수정 진행

---

**마지막 업데이트**: 2026-01-01
