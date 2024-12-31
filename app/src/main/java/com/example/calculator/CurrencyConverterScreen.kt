package com.example.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrencyConverterScreen(viewModel: CurrencyConverterViewModel) {
    val exchangeRates by viewModel.exchangeRates.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("INR") }
    var result by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates(BuildConfig.OPEN_API_ACCESS_KEY)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Currency Converter",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Enter Amount", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E1E1E),
                        cursorColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (error != null) {
                    Text(
                        text = error ?: "An error occurred",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Material3DropdownMenu(
                        options = exchangeRates.keys.toList(),
                        selected = fromCurrency,
                        onSelectedChange = { fromCurrency = it },
                        label = "From Currency"
                    )

                    Material3DropdownMenu(
                        options = exchangeRates.keys.toList(),
                        selected = toCurrency,
                        onSelectedChange = { toCurrency = it },
                        label = "To Currency"
                    )
                }
            }

            Button(
                onClick = {
                    val fromRate = exchangeRates[fromCurrency] ?: 1.0
                    val toRate = exchangeRates[toCurrency] ?: 1.0
                    result = viewModel.convertCurrency(amount.toDoubleOrNull() ?: 0.0, fromRate, toRate)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "Convert", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (result > 0.0) {
                Text(
                    text = "Converted Amount: $result $toCurrency",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun Material3DropdownMenu(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E), RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            Text(
                text = selected,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF1E1E1E))
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = Color.White) },
                        onClick = {
                            onSelectedChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
