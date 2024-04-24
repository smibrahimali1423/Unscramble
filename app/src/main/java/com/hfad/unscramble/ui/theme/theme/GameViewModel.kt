package com.hfad.unscramble.ui.theme.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hfad.unscramble.data.HighScore
import com.hfad.unscramble.data.MAX_NO_OF_WORDS
import com.hfad.unscramble.data.OfflineHighScoreRepository
import com.hfad.unscramble.data.allWords

import com.hfad.unscramble.ui.theme.theme.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class GameViewModel(private val repository: OfflineHighScoreRepository) : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    var isNewHighScoreSet = mutableStateOf(false)
        private set

    private var _highScore = MutableStateFlow<HighScore>(HighScore(score = 0))
    val highScore: StateFlow<HighScore> = _highScore.asStateFlow()

    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

    init {
        viewModelScope.launch {
            if (repository.hasItems() == false)
                repository.insertHighScore(highScore = highScore.value)

            _highScore.value = repository.getHighScore()
        }
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle(), isNewHighScoreSet = false)
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val wordLength = currentWord.length
            val scoreIncrease = when {
                wordLength <= 3 -> 10
                wordLength <= 5 -> 20
                wordLength <= 7 -> 30
                wordLength <= 9 -> 40
                else -> 50
            }
            val updatedScore = _uiState.value.score + scoreIncrease
            updateGameState(updatedScore)
        } else {
            _uiState.value = _uiState.value.copy(isGuessedWordWrong = true)
        }
        updateUserGuess("")
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            val currentHighScore: HighScore = highScore.value
            if (currentHighScore.score < updatedScore) {
                _uiState.value = _uiState.value.copy(isNewHighScoreSet = true)
                viewModelScope.launch {
                    repository.updateHighScore(updatedScore)
                    _highScore.value = repository.getHighScore()
                }

            }
            _uiState.value = _uiState.value.copy(
                isGuessedWordWrong = false,
                score = updatedScore,
                isGameOver = true
            )
        } else {
            _uiState.value = _uiState.value.copy(
                isGuessedWordWrong = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                currentWordCount = _uiState.value.currentWordCount.inc(),
                score = updatedScore
            )
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        do {
            currentWord = allWords.random()
        } while (usedWords.contains(currentWord))
        usedWords.add(currentWord)
        return shuffleCurrentWord(currentWord)
    }
}

class GameViewModelFactory(private val repository: OfflineHighScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
