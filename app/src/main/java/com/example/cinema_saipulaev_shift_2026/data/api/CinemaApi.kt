package com.example.cinema_saipulaev_shift_2026.data.api


import com.example.cinema_saipulaev_shift_2026.data.model.FilmResponse
import com.example.cinema_saipulaev_shift_2026.data.model.FilmsResponse
import retrofit2.http.GET
import retrofit2.http.Path

import retrofit2.http.Body
import retrofit2.http.POST
import com.example.cinema_saipulaev_shift_2026.data.model.ScheduleResponse
import com.example.cinema_saipulaev_shift_2026.data.model.CreateCinemaPaymentDto
import com.example.cinema_saipulaev_shift_2026.data.model.PaymentResponse

interface CinemaApi {

    @GET("api/cinema/films")
    suspend fun getFilms(): FilmsResponse

    @GET("api/cinema/film/{filmId}")
    suspend fun getFilm(
        @Path("filmId") filmId: String
    ): FilmResponse

    @GET("api/cinema/film/{filmId}/schedule")
    suspend fun getFilmSchedule(
        @Path("filmId") filmId: String
    ): ScheduleResponse

    @POST("api/cinema/payment")
    suspend fun createPayment(
        @Body request: CreateCinemaPaymentDto
    ): PaymentResponse
}