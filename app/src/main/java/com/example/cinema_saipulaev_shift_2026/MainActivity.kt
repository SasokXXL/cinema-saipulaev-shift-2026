package com.example.cinema_saipulaev_shift_2026

import com.example.cinema_saipulaev_shift_2026.ui.screens.PaymentScreen
import com.example.cinema_saipulaev_shift_2026.ui.screens.SeatsScreen
import com.example.cinema_saipulaev_shift_2026.ui.screens.SelectedSeat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cinema_saipulaev_shift_2026.ui.screens.ScheduleScreen
import com.example.cinema_saipulaev_shift_2026.data.model.FilmScheduleSeance
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinema_saipulaev_shift_2026.ui.screens.FilmDetailsDialog
import com.example.cinema_saipulaev_shift_2026.ui.screens.FilmsScreen
import com.example.cinema_saipulaev_shift_2026.viewmodel.CinemaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val cinemaViewModel: CinemaViewModel = viewModel()
                val selectedFilm by cinemaViewModel.selectedFilm.collectAsState()
                var currentScreen by remember { mutableStateOf("films") }
                var selectedFilmId by remember { mutableStateOf<String?>(null) }
                var selectedDate by remember { mutableStateOf<String?>(null) }
                var selectedSeance by remember { mutableStateOf<FilmScheduleSeance?>(null) }
                var selectedSeats by remember { mutableStateOf<List<SelectedSeat>>(emptyList()) }

                when (currentScreen) {
                    "films" -> {
                        FilmsScreen(
                            viewModel = cinemaViewModel,
                            onFilmClick = { filmId ->
                                cinemaViewModel.loadFilmById(filmId)
                            }
                        )
                    }

                    "schedule" -> {
                        selectedFilmId?.let { filmId ->
                            ScheduleScreen(
                                filmId = filmId,
                                viewModel = cinemaViewModel,
                                onBackClick = {
                                    currentScreen = "films"
                                    cinemaViewModel.clearSchedule()
                                },
                                onSeanceClick = { date, seance ->
                                    selectedDate = date
                                    selectedSeance = seance
                                    currentScreen = "seats"
                                }
                            )
                        }
                    }

                    "seats" -> {
                        val date = selectedDate
                        val seance = selectedSeance

                        if (date != null && seance != null) {
                            SeatsScreen(
                                date = date,
                                seance = seance,
                                onBackClick = {
                                    currentScreen = "schedule"
                                },
                                onContinueClick = { seats ->
                                    selectedSeats = seats
                                    currentScreen = "payment"
                                }
                            )
                        }
                    }

                    "payment" -> {
                        val filmId = selectedFilmId
                        val date = selectedDate
                        val seance = selectedSeance

                        if (filmId != null && date != null && seance != null) {
                            PaymentScreen(
                                filmId = filmId,
                                date = date,
                                time = seance.time,
                                selectedSeats = selectedSeats,
                                viewModel = cinemaViewModel,
                                onBackClick = {
                                    currentScreen = "seats"
                                },
                                onSuccessClick = {
                                    currentScreen = "films"
                                    selectedFilmId = null
                                    selectedDate = null
                                    selectedSeance = null
                                    selectedSeats = emptyList()
                                }
                            )
                        }
                    }
                }

                selectedFilm?.let { film ->
                    FilmDetailsDialog(
                        film = film,
                        onDismiss = {
                            cinemaViewModel.clearSelectedFilm()
                        },
                        onChooseSession = { filmId ->
                            selectedFilmId = filmId
                            cinemaViewModel.clearSelectedFilm()
                            currentScreen = "schedule"
                        }
                    )
                }
            }
        }
    }
}