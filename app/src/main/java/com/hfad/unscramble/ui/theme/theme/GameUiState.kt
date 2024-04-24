
package com.hfad.unscramble.ui.theme.theme


/**
 * Data class that represents the game UI state
 */
data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val isNewHighScoreSet: Boolean = false
)
