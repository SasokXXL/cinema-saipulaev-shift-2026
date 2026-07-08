package com.example.cinema_saipulaev_shift_2026.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinema_saipulaev_shift_2026.data.model.FilmSchedule
import com.example.cinema_saipulaev_shift_2026.data.model.FilmScheduleSeance
import com.example.cinema_saipulaev_shift_2026.viewmodel.CinemaViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    filmId: String,
    viewModel: CinemaViewModel,
    onBackClick: () -> Unit,
    onSeanceClick: (String, FilmScheduleSeance) -> Unit
) {
    val schedules by viewModel.schedules.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedDate by remember { mutableStateOf<String?>(null) }
    var showAllDates by remember { mutableStateOf(false) }

    LaunchedEffect(filmId) {
        viewModel.loadSchedule(filmId)
    }

    LaunchedEffect(schedules) {
        if (selectedDate == null && schedules.isNotEmpty()) {
            selectedDate = schedules.first().date
        }
    }

    val selectedSchedule = schedules.firstOrNull { it.date == selectedDate }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Выбор сеанса")
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                error != null -> {
                    Text(
                        text = error ?: "Ошибка",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                schedules.isEmpty() -> {
                    Text(
                        text = "Расписание не найдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        DateSelector(
                            schedules = schedules,
                            selectedDate = selectedDate,
                            showAllDates = showAllDates,
                            onTodayClick = {
                                selectedDate = schedules.getOrNull(0)?.date
                                showAllDates = false
                            },
                            onTomorrowClick = {
                                selectedDate = schedules.getOrNull(1)?.date
                                showAllDates = false
                            },
                            onChooseDayClick = {
                                showAllDates = !showAllDates
                            },
                            onDateClick = { date ->
                                selectedDate = date
                                showAllDates = false
                            }
                        )

                        if (selectedSchedule == null) {
                            Text(
                                text = "На выбранную дату сеансов нет",
                                modifier = Modifier.padding(16.dp)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Text(
                                        text = "Дата: ${formatDateWithWeekday(selectedSchedule.date)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                items(selectedSchedule.seances) { seance ->
                                    SeanceCard(
                                        seance = seance,
                                        onClick = {
                                            onSeanceClick(selectedSchedule.date, seance)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSelector(
    schedules: List<FilmSchedule>,
    selectedDate: String?,
    showAllDates: Boolean,
    onTodayClick: () -> Unit,
    onTomorrowClick: () -> Unit,
    onChooseDayClick: () -> Unit,
    onDateClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            DateChip(
                text = "Сегодня",
                selected = selectedDate == schedules.getOrNull(0)?.date,
                onClick = onTodayClick
            )

            if (schedules.size > 1) {
                DateChip(
                    text = "Завтра",
                    selected = selectedDate == schedules.getOrNull(1)?.date,
                    onClick = onTomorrowClick
                )
            }

            DateChip(
                text = "Выбрать день",
                selected = showAllDates,
                onClick = onChooseDayClick
            )
        }

        if (showAllDates) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                schedules.forEach { schedule ->
                    DateChip(
                        text = formatDateWithWeekday(schedule.date),
                        selected = selectedDate == schedule.date,
                        onClick = {
                            onDateClick(schedule.date)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        ElevatedAssistChip(
            onClick = onClick,
            label = {
                Text(text = text)
            }
        )
    } else {
        AssistChip(
            onClick = onClick,
            label = {
                Text(text = text)
            }
        )
    }
}

@Composable
private fun SeanceCard(
    seance: FilmScheduleSeance,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Время: ${seance.time}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Зал: ${seance.hall.name}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun formatDateWithWeekday(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val outputFormat = SimpleDateFormat("dd.MM, EEEE", Locale("ru"))

        val parsedDate = inputFormat.parse(date)
        if (parsedDate != null) {
            outputFormat.format(parsedDate)
        } else {
            date
        }
    } catch (e: Exception) {
        date
    }
}