package com.example.cinema_saipulaev_shift_2026

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

                FilmsScreen(
                    viewModel = cinemaViewModel,
                    onFilmClick = { filmId ->
                        cinemaViewModel.loadFilmById(filmId)
                    }
                )

                selectedFilm?.let { film ->
                    FilmDetailsDialog(
                        film = film,
                        onDismiss = {
                            cinemaViewModel.clearSelectedFilm()
                        }
                    )
                }
            }
        }
    }
}