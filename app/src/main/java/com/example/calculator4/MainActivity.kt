package com.example.calculator4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.calculator4.ui.MainScreen
import com.example.calculator4.ui.Task1Screen
import com.example.calculator4.ui.Task2Screen
import com.example.calculator4.ui.Task3Screen
import com.example.calculator4.ui.theme.Calculator4Theme

enum class Screen {
    Main,
    Task1,
    Task2,
    Task3
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculator4Theme {
                var currentScreen by remember { mutableStateOf(Screen.Main) }
                
                when (currentScreen) {
                    Screen.Main -> MainScreen(
                        onTask1Click = { currentScreen = Screen.Task1 },
                        onTask2Click = { currentScreen = Screen.Task2 },
                        onTask3Click = { currentScreen = Screen.Task3 }
                    )
                    Screen.Task1 -> Task1Screen(
                        onBack = { currentScreen = Screen.Main }
                    )
                    Screen.Task2 -> Task2Screen(
                        onBack = { currentScreen = Screen.Main }
                    )
                    Screen.Task3 -> Task3Screen(
                        onBack = { currentScreen = Screen.Main }
                    )
                }
            }
        }
    }
}