package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SpeedConverterScreen(navController: NavHostController) {
    var amount by remember { mutableStateOf("0") }
    var fromUnit by remember { mutableStateOf("m/s") }
    var toUnit by remember { mutableStateOf("km/h") }
    var result by remember { mutableDoubleStateOf(0.0) }

    val showFromUnitSheet = remember { mutableStateOf(false) }
    val showToUnitSheet = remember { mutableStateOf(false) }

    val units = listOf(
        "m/s" to "Meters per Second",
        "km/h" to "Kilometers per Hour",
        "mph" to "Miles per Hour",
        "ft/s" to "Feet per Second",
        "kn" to "Knots"
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonSize = (screenWidth / 5).dp

    // Perform conversion when inputs change
    LaunchedEffect(amount, fromUnit, toUnit) {
        result = convertSpeed(amount.toDoubleOrNull() ?: 0.0, fromUnit, toUnit)
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
                // Back button and screen title
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
                        text = "Speed Conversion",
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

            // Keypad
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

    // From Unit BottomSheet
    ReusableBottomSheet(
        options = units,
        selected = fromUnit,
        onSelectedChange = { fromUnit = it },
        label = "Select From Unit",
        showBottomSheet = showFromUnitSheet
    )

    // To Unit BottomSheet
    ReusableBottomSheet(
        options = units,
        selected = toUnit,
        onSelectedChange = { toUnit = it },
        label = "Select To Unit",
        showBottomSheet = showToUnitSheet
    )
}

fun convertSpeed(value: Double, fromUnit: String, toUnit: String): Double {
    val conversionRates = mapOf(
        "m/s" to 1.0,
        "km/h" to 3.6,
        "mph" to 2.23694,
        "ft/s" to 3.28084,
        "kn" to 1.94384
    )

    val fromRate = conversionRates[fromUnit] ?: 1.0
    val toRate = conversionRates[toUnit] ?: 1.0

    return value * fromRate / toRate
}
