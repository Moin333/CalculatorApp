package com.example.calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(viewModel: CalculatorViewModel, onItemClick: (String) -> Unit) {
    val history by viewModel.history.collectAsState(initial = emptyList())

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Main content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(history) { entry ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemClick(entry.inputValue)
                            },
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = entry.inputValue,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = entry.calculatedValue,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }


            OutlinedButton(
                onClick = { viewModel.clearHistory() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = MaterialTheme.shapes.medium, // Ensures the button has a rectangular shape
                border = null
            ) {
                Text(
                    text = "Clear History",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
        }

        // Vertical divider
        Box(
            modifier = Modifier
                .width(0.7.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}
