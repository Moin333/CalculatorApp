package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
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

        val viewModelCurrency: CurrencyConverterViewModel by viewModels {
            CurrencyConverterViewModelFactory()
        }

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

            val backgroundColor = Color.Black
            Box(modifier = Modifier.background(color = backgroundColor))
            {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "calculator") {
                    composable(
                        route = "calculator",
                        enterTransition = { null }, // No animation when entering the first screen
                        exitTransition = { null }, // No exit transition for the first screen
                        popEnterTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    ) {
                        CalculatorUI(viewModel, navController)
                    }

                    composable(
                        route = "unit_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null }, // No exit transition when navigating forward
                        popEnterTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    ) {
                        UnitConverterPage(navController)
                    }

                    composable(
                        route = "currency_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null }, // No exit transition when navigating forward
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    ) {
                        CurrencyConverterScreen(viewModelCurrency, navController)
                    }

                    composable(
                        route = "length_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        LengthConverterScreen(navController)
                    }

                    composable(
                        route = "area_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        AreaConverterScreen(navController)
                    }

                    composable(
                        route = "speed_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        SpeedConverterScreen(navController)
                    }

                    composable(
                        route = "weight_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        WeightConverterScreen(navController)
                    }

                    composable(
                        route = "temperature_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        TemperatureConverterScreen(navController)
                    }

                    composable(
                        route = "power_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        PowerConverterScreen(navController)
                    }

                    composable(
                        route = "pressure_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        PressureConverterScreen(navController)
                    }

                    composable(
                        route = "volume_converter",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = { null },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    )
                    {
                        VolumeConverterScreen(navController)
                    }
                }
            }
        }
    }
}

