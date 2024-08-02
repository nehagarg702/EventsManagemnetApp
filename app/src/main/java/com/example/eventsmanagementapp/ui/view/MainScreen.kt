package com.example.eventsmanagementapp.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var showIntroScreen by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // 3 seconds delay
        showIntroScreen = false
        navController.navigate("appNavigation") {
            popUpTo("introScreen") { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = "introScreen") {
        composable("introScreen") {
            IntroScreen {
                showIntroScreen = false
                navController.navigate("appNavigation") {
                    popUpTo("introScreen") { inclusive = true }
                }
            }
        }
        composable("appNavigation") {
            AppNavigation()
        }
    }
}
