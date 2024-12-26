package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = CalculatorDatabase.getDatabase(this)
        val dao = database.calculatorDao()
        val viewModelFactory = CalculatorViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)[CalculatorViewModel::class.java]

        setContent {
            val systemUiController = rememberSystemUiController()
            val statusBarColor = Color.Black // Choose your desired color
            // Set the status bar color
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = statusBarColor,
                    darkIcons = false // Use light icons for dark backgrounds
                )
            }
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "calculator") {
                composable("calculator") { CalculatorUI(viewModel, navController) }
                composable("history") { HistoryScreen(viewModel) }
            }
        }
    }
}

