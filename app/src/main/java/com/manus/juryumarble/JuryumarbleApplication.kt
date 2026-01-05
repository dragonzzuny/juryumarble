package com.manus.juryumarble

import android.app.Application
import com.manus.juryumarble.data.local.CardDataLoader
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Juryumarble Application class with Hilt
 * 앱 시작 시 기본 카드 데이터 로드
 */
@HiltAndroidApp
class JuryumarbleApplication : Application() {
    
    @Inject
    lateinit var cardDataLoader: CardDataLoader
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        
        // 앱 시작 시 기본 카드 데이터 로드
        applicationScope.launch {
            cardDataLoader.loadCardsIfNeeded()
        }
    }
}
