package com.example.cinema_saipulaev_shift_2026.data.model

data class ScheduleResponse(
    val success: Boolean,
    val reason: String?,
    val schedules: List<FilmSchedule>
)

data class FilmSchedule(
    val date: String,
    val seances: List<FilmScheduleSeance>
)

data class FilmScheduleSeance(
    val time: String,
    val hall: FilmHall
)

data class FilmHall(
    val name: String,
    val places: List<List<FilmHallCell>>
)

data class FilmHallCell(
    val type: String? = null,
    val price: Double? = null
)