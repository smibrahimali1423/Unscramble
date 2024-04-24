package com.hfad.unscramble.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {
    @Query("SELECT * FROM high_score_table LIMIT 1")
    suspend fun getHighScore(): HighScore

    @Insert
    suspend fun insertHighScore(highScore: HighScore)

    @Update
    suspend fun updateHighScore(highScore: HighScore)

    @Query("SELECT COUNT(*) FROM high_score_table")
    suspend fun getItemsCount(): Int

}
