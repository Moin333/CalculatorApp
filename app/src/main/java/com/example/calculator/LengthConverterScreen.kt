package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LengthConverterScreen(navController: NavHostController) {
    var amount by remember { mutableStateOf("0") }
    var fromUnit by remember { mutableStateOf("m") }
    var toUnit by remember { mutableStateOf("km") }
    var result by remember { mutableDoubleStateOf(0.0) }

    val showFromUnitSheet = remember { mutableStateOf(false) }
    val showToUnitSheet = remember { mutableStateOf(false) }

    val units = listOf(
        "m" to "Meters",
        "km" to "Kilometers",
        "cm" to "Centimeters",
        "mm" to "Millimeters",
        "in" to "Inches",
        "ft" to "Feet",
        "yd" to "Yards",
        "mi" to "Miles"
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonSize = (screenWidth / 5).dp

    LaunchedEffect(amount, fromUnit, toUnit) {
        result = convertLength(amount.toDoubleOrNull() ?: 0.0, fromUnit, toUnit)
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
                        text = "Length Conversion",
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

    // Bottom Sheets for Unit Selection
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

fun convertLength(value: Double, fromUnit: String, toUnit: String): Double {
    val conversionRates = mapOf(
        "m" to 1.0,
        "km" to 1000.0,
        "cm" to 0.01,
        "mm" to 0.001,
        "in" to 0.0254,
        "ft" to 0.3048,
        "yd" to 0.9144,
        "mi" to 1609.34
    )

    val fromRate = conversionRates[fromUnit] ?: 1.0
    val toRate = conversionRates[toUnit] ?: 1.0

    return value * fromRate / toRate
}


@Composable
fun ReusableKeypad(
    onKeyPress: (String) -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp,
    customKeys: List<List<String>>? = null
) {
    val defaultKeypadLayout = listOf(
        listOf("7", "8", "9", "C"),
        listOf("4", "5", "6", "Backspace"),
        listOf("1", "2", "3", ""),
        listOf("00", "0", ".", "")
    )

    val keypadLayout = customKeys ?: defaultKeypadLayout

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keypadLayout.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { key ->
                    if (key.isNotEmpty()) {
                        Surface(
                            modifier = Modifier
                                .size(buttonSize)
                                .clip(CircleShape)
                                .background(Color.Transparent, CircleShape)
                                .clickable { onKeyPress(key) },
                            shape = CircleShape,
                            color = if (key == "C" || key == "Backspace" || key == "+/-") MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                when (key) {
                                    "Backspace" -> Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                        contentDescription = "Backspace",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(buttonSize * 0.35f)
                                    )
                                    else -> Text(
                                        text = key,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 26.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.size(buttonSize)) // Placeholder for alignment
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableBottomSheet(
    options: List<Pair<String, String>>,
    selected: String,
    onSelectedChange: (String) -> Unit,
    label: String,
    showBottomSheet: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            var searchQuery by remember { mutableStateOf("") }
            val filteredOptions = options.filter {
                it.first.contains(searchQuery, ignoreCase = true) ||
                        it.second.contains(searchQuery, ignoreCase = true)
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 36.dp),
                    textAlign = TextAlign.Center
                )

                SlimSearchField(
                    searchQuery = searchQuery,
                    onValueChange = { searchQuery = it }
                )

                Spacer(modifier = Modifier.height(36.dp))

                LazyColumn {
                    items(filteredOptions) { option ->
                        Text(
                            text = "${option.second} (${option.first})",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectedChange(option.first)
                                    showBottomSheet.value = false
                                }
                                .padding(12.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
