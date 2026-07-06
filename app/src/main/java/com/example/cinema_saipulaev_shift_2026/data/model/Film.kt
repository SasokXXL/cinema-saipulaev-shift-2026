package com.example.cinema_saipulaev_shift_2026.data.model

data class Film(
    val id: String,
    val name: String,
    val originalName: String,
    val description: String,
    val releaseDate: String,
    val actors: List<FilmStaff>,
    val directors: List<FilmStaff>,
    val runtime: Int,
    val ageRating: String,
    val genres: List<String>,
    val userRatings: FilmUserRating,
    val img: String,
    val country: Country?
)

data class FilmStaff(
    val id: String,
    val professions: List<String>,
    val fullName: String,
    val photo: String
)

data class FilmUserRating(
    val kinopoisk: String,
    val imdb: String
)

data class Country(
    val name: String,
    val code: String,
    val code2: String,
    val id: Int
)