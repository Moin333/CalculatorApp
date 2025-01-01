package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

val currencyCountryMapping = mapOf(
    "AED" to "United Arab Emirates",
    "AFN" to "Afghanistan",
    "ALL" to "Albania",
    "AMD" to "Armenia",
    "ANG" to "Netherlands Antilles",
    "AOA" to "Angola",
    "ARS" to "Argentina",
    "AUD" to "Australia",
    "AWG" to "Aruba",
    "AZN" to "Azerbaijan",
    "BAM" to "Bosnia and Herzegovina",
    "BBD" to "Barbados",
    "BDT" to "Bangladesh",
    "BGN" to "Bulgaria",
    "BHD" to "Bahrain",
    "BIF" to "Burundi",
    "BMD" to "Bermuda",
    "BND" to "Brunei",
    "BOB" to "Bolivia",
    "BRL" to "Brazil",
    "BSD" to "Bahamas",
    "BTC" to "Bitcoin",
    "BTN" to "Bhutan",
    "BWP" to "Botswana",
    "BYN" to "Belarus",
    "BZD" to "Belize",
    "CAD" to "Canada",
    "CDF" to "Congo (Kinshasa)",
    "CHF" to "Switzerland",
    "CLF" to "Chile (UF)",
    "CLP" to "Chile",
    "CNH" to "China (Offshore)",
    "CNY" to "China",
    "COP" to "Colombia",
    "CRC" to "Costa Rica",
    "CUC" to "Cuba (Convertible)",
    "CUP" to "Cuba",
    "CVE" to "Cape Verde",
    "CZK" to "Czech Republic",
    "DJF" to "Djibouti",
    "DKK" to "Denmark",
    "DOP" to "Dominican Republic",
    "DZD" to "Algeria",
    "EGP" to "Egypt",
    "ERN" to "Eritrea",
    "ETB" to "Ethiopia",
    "EUR" to "Eurozone",
    "FJD" to "Fiji",
    "FKP" to "Falkland Islands",
    "GBP" to "United Kingdom",
    "GEL" to "Georgia",
    "GGP" to "Guernsey",
    "GHS" to "Ghana",
    "GIP" to "Gibraltar",
    "GMD" to "Gambia",
    "GNF" to "Guinea",
    "GTQ" to "Guatemala",
    "GYD" to "Guyana",
    "HKD" to "Hong Kong",
    "HNL" to "Honduras",
    "HRK" to "Croatia",
    "HTG" to "Haiti",
    "HUF" to "Hungary",
    "IDR" to "Indonesia",
    "ILS" to "Israel",
    "IMP" to "Isle of Man",
    "INR" to "India",
    "IQD" to "Iraq",
    "IRR" to "Iran",
    "ISK" to "Iceland",
    "JEP" to "Jersey",
    "JMD" to "Jamaica",
    "JOD" to "Jordan",
    "JPY" to "Japan",
    "KES" to "Kenya",
    "KGS" to "Kyrgyzstan",
    "KHR" to "Cambodia",
    "KMF" to "Comoros",
    "KPW" to "North Korea",
    "KRW" to "South Korea",
    "KWD" to "Kuwait",
    "KYD" to "Cayman Islands",
    "KZT" to "Kazakhstan",
    "LAK" to "Laos",
    "LBP" to "Lebanon",
    "LKR" to "Sri Lanka",
    "LRD" to "Liberia",
    "LSL" to "Lesotho",
    "LYD" to "Libya",
    "MAD" to "Morocco",
    "MDL" to "Moldova",
    "MGA" to "Madagascar",
    "MKD" to "North Macedonia",
    "MMK" to "Myanmar",
    "MNT" to "Mongolia",
    "MOP" to "Macau",
    "MRU" to "Mauritania",
    "MUR" to "Mauritius",
    "MVR" to "Maldives",
    "MWK" to "Malawi",
    "MXN" to "Mexico",
    "MYR" to "Malaysia",
    "MZN" to "Mozambique",
    "NAD" to "Namibia",
    "NGN" to "Nigeria",
    "NIO" to "Nicaragua",
    "NOK" to "Norway",
    "NPR" to "Nepal",
    "NZD" to "New Zealand",
    "OMR" to "Oman",
    "PAB" to "Panama",
    "PEN" to "Peru",
    "PGK" to "Papua New Guinea",
    "PHP" to "Philippines",
    "PKR" to "Pakistan",
    "PLN" to "Poland",
    "PYG" to "Paraguay",
    "QAR" to "Qatar",
    "RON" to "Romania",
    "RSD" to "Serbia",
    "RUB" to "Russia",
    "RWF" to "Rwanda",
    "SAR" to "Saudi Arabia",
    "SBD" to "Solomon Islands",
    "SCR" to "Seychelles",
    "SDG" to "Sudan",
    "SEK" to "Sweden",
    "SGD" to "Singapore",
    "SHP" to "Saint Helena",
    "SLL" to "Sierra Leone",
    "SOS" to "Somalia",
    "SRD" to "Suriname",
    "SSP" to "South Sudan",
    "STD" to "São Tomé and Príncipe",
    "STN" to "São Tomé and Príncipe (New)",
    "SVC" to "El Salvador",
    "SYP" to "Syria",
    "SZL" to "Eswatini",
    "THB" to "Thailand",
    "TJS" to "Tajikistan",
    "TMT" to "Turkmenistan",
    "TND" to "Tunisia",
    "TOP" to "Tonga",
    "TRY" to "Turkey",
    "TTD" to "Trinidad and Tobago",
    "TWD" to "Taiwan",
    "TZS" to "Tanzania",
    "UAH" to "Ukraine",
    "UGX" to "Uganda",
    "USD" to "United States",
    "UYU" to "Uruguay",
    "UZS" to "Uzbekistan",
    "VES" to "Venezuela",
    "VND" to "Vietnam",
    "VUV" to "Vanuatu",
    "WST" to "Samoa",
    "XAF" to "Central African CFA Franc",
    "XAG" to "Silver (Ounce)",
    "XAU" to "Gold (Ounce)",
    "XCD" to "East Caribbean",
    "XDR" to "IMF Special Drawing Rights",
    "XOF" to "West African CFA Franc",
    "XPD" to "Palladium (Ounce)",
    "XPF" to "CFP Franc",
    "XPT" to "Platinum (Ounce)",
    "YER" to "Yemen",
    "ZAR" to "South Africa",
    "ZMW" to "Zambia",
    "ZWL" to "Zimbabwe"
)


@Composable
fun CurrencyConverterScreen(viewModel: CurrencyConverterViewModel, navController: NavHostController) {
    val exchangeRates by viewModel.exchangeRates.collectAsState()

    var amount by remember { mutableStateOf("0") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("INR") }
    var result by remember { mutableDoubleStateOf(0.0) }
    val showFromCurrencySheet = remember { mutableStateOf(false) }
    val showToCurrencySheet = remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonSize = (screenWidth / 5).dp

    val currencyOptions = exchangeRates.keys.map {
        it to (currencyCountryMapping[it] ?: "Unknown Currency")
    }

    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates(BuildConfig.OPEN_API_ACCESS_KEY)
    }

    LaunchedEffect(amount, fromCurrency, toCurrency) {
        val fromRate = exchangeRates[fromCurrency] ?: 1.0
        val toRate = exchangeRates[toCurrency] ?: 1.0
        result = viewModel.convertCurrency(amount.toDoubleOrNull() ?: 0.0, fromRate, toRate)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
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
                        tint = Color.White
                    )
                    Text(
                        text = "Currency",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                // Conversion Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // From Currency Selector
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currencyCountryMapping[fromCurrency] ?: fromCurrency} ($fromCurrency)",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.clickable { showFromCurrencySheet.value = true }
                            )
                            Text("⌵", modifier = Modifier.graphicsLayer { rotationZ = -90f }, color = DarkGray, fontSize = 18.sp)
                        }

                        Text(
                            text = amount,
                            color = Color.White,
                            fontSize = 24.sp,
                            textAlign = TextAlign.End
                        )
                    }

                    HorizontalDivider(color = DarkGray, modifier = Modifier.padding(start = 4.dp, end = 4.dp))

                    // To Currency Selector
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currencyCountryMapping[toCurrency] ?: toCurrency} ($toCurrency)",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.clickable { showToCurrencySheet.value = true }
                            )
                            Text("⌵", modifier = Modifier.graphicsLayer { rotationZ = -90f }, color = DarkGray, fontSize = 18.sp)
                        }

                        Text(
                            text = "$result",
                            color = Color.White,
                            fontSize = 24.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            // Keypad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x32848682))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currencyButtons = listOf(
                        listOf("7", "8", "9", "C"),
                        listOf("4", "5", "6", "Backspace"),
                        listOf("1", "2", "3", ""),
                        listOf("00", "0", ".", "")
                    )

                    currencyButtons.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            row.forEach { label ->
                                if (label.isNotEmpty()) {
                                    Surface(
                                        modifier = Modifier
                                            .size(buttonSize)
                                            .background(Color.Transparent, CircleShape),
                                        shape = CircleShape,
                                        contentColor = Color.White,
                                        color = if (label == "C" || label == "Backspace") DarkGray else Color.Transparent,
                                        onClick = {
                                            when (label) {
                                                "C" -> amount = "0"
                                                "Backspace" -> amount = amount.dropLast(1).ifEmpty { "0" }
                                                else -> {
                                                    if (amount == "0") amount = label else amount += label
                                                }
                                            }
                                        }
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            if (label == "Backspace") {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                                    contentDescription = "Backspace",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(buttonSize * 0.35f)
                                                )
                                            } else {
                                                Text(
                                                    text = label,
                                                    color = Color.White,
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

        }
    }

    // Bottom Sheets for Currency Selection
    CurrencySelectorBottomSheet(
        options = currencyOptions,
        selected = fromCurrency,
        onSelectedChange = { fromCurrency = it },
        label = "Select currency",
        onDismissRequest = { showFromCurrencySheet.value = false },
        showBottomSheet = showFromCurrencySheet
    )

    CurrencySelectorBottomSheet(
        options = currencyOptions,
        selected = toCurrency,
        onSelectedChange = { toCurrency = it },
        label = "Select currency",
        onDismissRequest = { showToCurrencySheet.value = false },
        showBottomSheet = showToCurrencySheet
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectorBottomSheet(
    options: List<Pair<String, String>>, // List of currency codes and names
    selected: String,
    onSelectedChange: (String) -> Unit,
    label: String,
    onDismissRequest: () -> Unit,
    showBottomSheet: MutableState<Boolean>
) {
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
                onDismissRequest()
            },
            containerColor = Color(0xFF191A19)
        ) {
            var searchQuery by remember { mutableStateOf("") }
            val filteredOptions = options.filter {
                it.first.contains(searchQuery, ignoreCase = true) ||
                        it.second.contains(searchQuery, ignoreCase = true)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Display the label as a header
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 36.dp), // Move it upwards
                    textAlign = TextAlign.Center
                )

                // Search bar
                SlimSearchField(
                    searchQuery = searchQuery,
                    onValueChange = { searchQuery = it }
                )

                Spacer(modifier = Modifier.height(36.dp))
                // Display filtered options
                LazyColumn {
                    items(filteredOptions) { option ->
                        Text(
                            text = "${option.second} (${option.first})",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectedChange(option.first)
                                    showBottomSheet.value = false
                                    onDismissRequest()
                                }
                                .padding(12.dp),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SlimSearchField(
    searchQuery: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(40.dp) // Slimmer height
            .background(DarkGray, shape = RoundedCornerShape(24.dp)) // Slim, rounded container
            .padding(horizontal = 12.dp), // Inner padding for content
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp) // Smaller icon size
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            BasicTextField(
                value = searchQuery,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                ),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
