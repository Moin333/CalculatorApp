package com.example.calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(viewModel: CalculatorViewModel) {
    val history by viewModel.history.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(history) { entry ->
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = entry.inputValue,
                        color = Color.Gray,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = entry.calculatedValue,
                        color = Color.White,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { viewModel.clearHistory() },
            modifier = Modifier.fillMaxWidth(),
            border = null
        ) {
            Text(
                text = "Clear History",
                color = Color(0xFFF68B19)
            )
        }
    }
}