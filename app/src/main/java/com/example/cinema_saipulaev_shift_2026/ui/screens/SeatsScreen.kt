package com.example.cinema_saipulaev_shift_2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinema_saipulaev_shift_2026.data.model.FilmScheduleSeance

data class SelectedSeat(
    val row: Int, val column: Int, val price: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatsScreen(
    date: String,
    seance: FilmScheduleSeance,
    onBackClick: () -> Unit,
    onContinueClick: (List<SelectedSeat>) -> Unit
) {
    val selectedSeats = remember { mutableStateListOf<SelectedSeat>() }
    val totalPrice = selectedSeats.sumOf { it.price }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Выбор мест")
        }, navigationIcon = {
            TextButton(onClick = onBackClick) {
                Text(text = "Назад")
            }
        })
    }, bottomBar = {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Выбрано мест: ${selectedSeats.size}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Итого: ${totalPrice.toInt()} ₽",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onContinueClick(selectedSeats.toList())
                    }, enabled = selectedSeats.isNotEmpty()
                ) {
                    Text(text = "Перейти к оплате")
                }
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Дата: $date", style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Время: ${seance.time}", style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Зал: ${seance.hall.name}", style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Экран",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 24.dp)
                    .size(width = 220.dp, height = 8.dp)
                    .background(Color.Gray, RoundedCornerShape(50))
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                seance.hall.places.forEachIndexed { rowIndex, row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${rowIndex + 1}",
                            modifier = Modifier.size(24.dp),
                            style = MaterialTheme.typography.bodySmall
                        )

                        row.forEachIndexed { columnIndex, place ->
                            val rowNumber = rowIndex + 1
                            val columnNumber = columnIndex + 1
                            val price = place.price ?: 0.0
                            val isAvailable = place.type != null && price > 0.0

                            if (isAvailable) {
                                val isSelected = selectedSeats.any {
                                    it.row == rowNumber && it.column == columnNumber
                                }

                                SeatItem(
                                    isSelected = isSelected, price = price, onClick = {
                                        val existingSeat = selectedSeats.firstOrNull {
                                            it.row == rowNumber && it.column == columnNumber
                                        }

                                        if (existingSeat != null) {
                                            selectedSeats.remove(existingSeat)
                                        } else {
                                            selectedSeats.add(
                                                SelectedSeat(
                                                    row = rowNumber,
                                                    column = columnNumber,
                                                    price = price
                                                )
                                            )
                                        }
                                    })
                            } else {
                                Spacer(modifier = Modifier.size(34.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Нажмите на свободные места, чтобы выбрать билеты.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SeatItem(
    isSelected: Boolean, price: Double, onClick: () -> Unit
) {
    val color = if (isSelected) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFF2196F3)
    }

    Box(
        modifier = Modifier
            .size(34.dp)
            .background(color, RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = price.toInt().toString(),
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}