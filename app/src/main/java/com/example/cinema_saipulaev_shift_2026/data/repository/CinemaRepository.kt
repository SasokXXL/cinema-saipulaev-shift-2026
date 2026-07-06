package com.example.cinema_saipulaev_shift_2026.data.repository

import com.example.cinema_saipulaev_shift_2026.data.api.RetrofitInstance
import com.example.cinema_saipulaev_shift_2026.data.model.Film

class CinemaRepository {

    private val api = RetrofitInstance.cinemaApi

    suspend fun getFilms(): List<Film> {
        return api.getFilms().films
    }

    suspend fun getFilm(filmId: String): Film {
        return api.getFilm(filmId).film
    }
}