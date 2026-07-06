package com.example.cinema_saipulaev_shift_2026.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://juniorsbootcamp.ru/"

    val cinemaApi: CinemaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CinemaApi::class.java)
    }
}