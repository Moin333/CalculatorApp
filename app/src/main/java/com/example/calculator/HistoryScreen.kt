package com.example.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(viewModel: CalculatorViewModel) {
    val history by viewModel.history.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(history) { entry ->
                Text(
                    text = "${entry.inputValue} = ${entry.calculatedValue}",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Button(
            onClick = { viewModel.clearHistory() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Clear History")
        }
    }
}