package com.example.cinema_saipulaev_shift_2026.data.model

data class FilmsResponse(
    val success: Boolean,
    val reason: String?,
    val films: List<Film>
)

data class FilmResponse(
    val success: Boolean,
    val reason: String?,
    val film: Film
)