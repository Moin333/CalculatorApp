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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AreaConverterScreen(navController: NavHostController) {
    var amount by remember { mutableStateOf("0") }
    var fromUnit by remember { mutableStateOf("m²") }
    var toUnit by remember { mutableStateOf("km²") }
    var result by remember { mutableDoubleStateOf(0.0) }

    val showFromUnitSheet = remember { mutableStateOf(false) }
    val showToUnitSheet = remember { mutableStateOf(false) }

    val units = listOf(
        "m²" to "Square Meters",
        "km²" to "Square Kilometers",
        "cm²" to "Square Centimeters",
        "mm²" to "Square Millimeters",
        "in²" to "Square Inches",
        "ft²" to "Square Feet",
        "yd²" to "Square Yards",
        "ac" to "Acres",
        "ha" to "Hectares"
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonSize = (screenWidth / 5).dp

    LaunchedEffect(amount, fromUnit, toUnit) {
        result = convertArea(amount.toDoubleOrNull() ?: 0.0, fromUnit, toUnit)
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
                // Header
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
                        text = "Area Conversion",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    )
                }

                // Conversion Section
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

    // BottomSheet for From Unit
    ReusableBottomSheet(
        options = units,
        selected = fromUnit,
        onSelectedChange = { fromUnit = it },
        label = "Select From Unit",
        showBottomSheet = showFromUnitSheet
    )

    // BottomSheet for To Unit
    ReusableBottomSheet(
        options = units,
        selected = toUnit,
        onSelectedChange = { toUnit = it },
        label = "Select To Unit",
        showBottomSheet = showToUnitSheet
    )
}

fun convertArea(value: Double, fromUnit: String, toUnit: String): Double {
    val conversionRates = mapOf(
        "m²" to 1.0,                 // Square Meter
        "km²" to 1_000_000.0,        // Square Kilometer to Square Meters
        "cm²" to 0.0001,             // Square Centimeter to Square Meters
        "mm²" to 0.000001,           // Square Millimeter to Square Meters
        "in²" to 0.00064516,         // Square Inch to Square Meters
        "ft²" to 0.092903,           // Square Foot to Square Meters
        "yd²" to 0.836127,           // Square Yard to Square Meters
        "ac" to 4046.8564224,        // Acre to Square Meters
        "ha" to 10_000.0             // Hectare to Square Meters
    )

    // Retrieve conversion rates for 'fromUnit' and 'toUnit'
    val fromRate = conversionRates[fromUnit] ?: error("Invalid fromUnit: $fromUnit")
    val toRate = conversionRates[toUnit] ?: error("Invalid toUnit: $toUnit")

    // Convert from the original unit to square meters, then to the target unit
    return value * fromRate / toRate
}

