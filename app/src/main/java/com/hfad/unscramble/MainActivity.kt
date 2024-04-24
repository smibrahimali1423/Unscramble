package com.hfad.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hfad.unscramble.ui.theme.UnscrambleTheme
import com.hfad.unscramble.ui.theme.theme.GameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the UnscrambleApplication
        val unscrambleApplication = application as UnscrambleApplication

        // Access dependencies from the container
        val repository = unscrambleApplication.container.repository

        setContent {
            UnscrambleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GameScreen(repository = repository)
                }
            }
        }
    }
}