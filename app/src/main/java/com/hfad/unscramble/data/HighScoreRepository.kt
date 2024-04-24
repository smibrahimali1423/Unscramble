package com.hfad.unscramble.data
import kotlinx.coroutines.flow.Flow



interface HighScoreRepository {
    suspend fun getHighScore(): HighScore
    suspend fun updateHighScore(score: Int)
    suspend fun insertHighScore(highScore: HighScore)
    suspend fun hasItems(): Boolean

}

class OfflineHighScoreRepository(private val highScoreDao: HighScoreDao) : HighScoreRepository {

    override suspend fun getHighScore(): HighScore = highScoreDao.getHighScore()

    override suspend fun updateHighScore(score: Int) = highScoreDao.updateHighScore(HighScore(score = score))

    override suspend fun insertHighScore(highScore: HighScore) = highScoreDao.insertHighScore(highScore = highScore)

    override suspend fun hasItems(): Boolean {
        return highScoreDao.getItemsCount() > 0
    }

}