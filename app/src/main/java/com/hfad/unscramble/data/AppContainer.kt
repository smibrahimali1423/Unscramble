package com.hfad.unscramble.data

import android.content.Context

class AppContainer(context: Context) {
    // Room Database
    private val database: InventoryDatabase by lazy {
        InventoryDatabase.getDatabase(context = context)
    }

    // HighScoreDao
    val highScoreDao: HighScoreDao by lazy {
        database.highScoreDao()
    }

    // OfflineHighScoreRepository
    val repository: OfflineHighScoreRepository by lazy {
        OfflineHighScoreRepository(highScoreDao)
    }
}
