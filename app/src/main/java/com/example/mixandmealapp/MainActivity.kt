package com.example.mixandmealapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mixandmealapp.ui.navigation.AppNavigation
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MixAndMealAppTheme {
                AppNavigation()
            }
        }
    }
}
