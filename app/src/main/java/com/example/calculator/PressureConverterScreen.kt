package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PressureConverterScreen(navController: NavHostController) {
    var amount by remember { mutableStateOf("0") }
    var fromUnit by remember { mutableStateOf("Pa") }
    var toUnit by remember { mutableStateOf("atm") }
    var result by remember { mutableDoubleStateOf(0.0) }

    val showFromUnitSheet = remember { mutableStateOf(false) }
    val showToUnitSheet = remember { mutableStateOf(false) }

    val units = listOf(
        "Pa" to "Pascal",
        "atm" to "Atmosphere",
        "bar" to "Bar",
        "mbar" to "Millibar",
        "Torr" to "Torr",
        "psi" to "Pound per square inch"
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonSize = (screenWidth / 5).dp

    // Automatically update result dynamically
    LaunchedEffect(amount, fromUnit, toUnit) {
        result = if (amount.isNotEmpty() && amount != "0") {
            val input = amount.toDoubleOrNull() ?: 0.0
            convertPressure(input, fromUnit, toUnit)
        } else 0.0
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Back Button and Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { navController.popBackStack() },
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Pressure Conversion",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // From Unit Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showFromUnitSheet.value = true },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${units.firstOrNull { it.first == fromUnit }?.second ?: ""} ($fromUnit)",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        )
                        Text(
                            text = amount,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                            textAlign = TextAlign.End
                        )
                    }

                    HorizontalDivider(color = DarkGray)

                    // To Unit Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showToUnitSheet.value = true },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${units.firstOrNull { it.first == toUnit }?.second ?: ""} ($toUnit)",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$result",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                            textAlign = TextAlign.End
                        )
                    }

                    HorizontalDivider(color = DarkGray)
                }
            }

            // Keypad Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                ReusableKeypad(
                    onKeyPress = { key ->
                        when (key) {
                            "C" -> amount = "0"
                            "Backspace" -> amount = amount.dropLast(1).ifEmpty { "0" }
                            else -> {
                                if (amount == "0") amount = key else amount += key
                            }
                        }
                    },
                    buttonSize = buttonSize
                )
            }
        }
    }

    // Bottom Sheet for Selecting Units
    ReusableBottomSheet(
        options = units,
        selected = fromUnit,
        onSelectedChange = { fromUnit = it },
        label = "Select From Unit",
        showBottomSheet = showFromUnitSheet
    )

    ReusableBottomSheet(
        options = units,
        selected = toUnit,
        onSelectedChange = { toUnit = it },
        label = "Select To Unit",
        showBottomSheet = showToUnitSheet
    )
}

// Conversion Logic
fun convertPressure(value: Double, fromUnit: String, toUnit: String): Double {
    val conversionRates = mapOf(
        "Pa" to 1.0,
        "atm" to 101325.0,
        "bar" to 100000.0,
        "mbar" to 100.0,
        "Torr" to 133.322,
        "psi" to 6894.76
    )

    val fromRate = conversionRates[fromUnit] ?: 1.0
    val toRate = conversionRates[toUnit] ?: 1.0

    return value * fromRate / toRate
}
