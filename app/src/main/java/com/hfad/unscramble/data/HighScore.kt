package com.hfad.unscramble.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_score_table")
data class HighScore(
    @PrimaryKey val id: Int = 0,
    var score: Int = 0
)
