package com.example.cinema_saipulaev_shift_2026.data.api


import com.example.cinema_saipulaev_shift_2026.data.model.FilmResponse
import com.example.cinema_saipulaev_shift_2026.data.model.FilmsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaApi {

    @GET("api/cinema/films")
    suspend fun getFilms(): FilmsResponse

    @GET("api/cinema/film/{filmId}")
    suspend fun getFilm(
        @Path("filmId") filmId: String
    ): FilmResponse
}