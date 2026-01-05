# 주류마블 - 파티 보드게임 앱

술자리에서 스마트폰 하나로 즐기는 부루마블 형식의 파티 게임

## 기술 스택
- **언어**: Kotlin
- **UI**: Jetpack Compose
- **아키텍처**: Clean Architecture (MVVM)
- **DI**: Hilt
- **DB**: Room
- **비동기**: Coroutines & Flow

## 프로젝트 구조
```
/marble
├── /app        # Presentation Layer (UI, ViewModel)
├── /domain     # Domain Layer (Models, UseCases, Repository Interfaces)
└── /data       # Data Layer (Room DB, Repository Implementations)
```

## 시작하기

1. Android Studio에서 프로젝트 열기
2. Gradle Sync 실행
3. 에뮬레이터 또는 실제 기기에서 앱 실행

## 개발 가이드

자세한 개발 가이드는 `/development_guide` 디렉토리를 참고하세요.
