package com.example.cinema_saipulaev_shift_2026.data.model

data class CreateCinemaPaymentDto(
    val filmId: String,
    val person: CreatePaymentPersonDto,
    val debitCard: CreatePaymentDebitCardDto,
    val seance: CreatePaymentSeanceDto,
    val tickets: List<CreatePaymentTicketsDto>
)

data class CreatePaymentPersonDto(
    val firstname: String,
    val lastname: String,
    val middlename: String,
    val phone: String
)

data class CreatePaymentDebitCardDto(
    val pan: String,
    val expireDate: String,
    val cvv: String
)

data class CreatePaymentSeanceDto(
    val date: String,
    val time: String
)

data class CreatePaymentTicketsDto(
    val row: Int,
    val column: Int
)

data class PaymentResponse(
    val success: Boolean,
    val reason: String?,
    val order: CinemaOrder?
)

data class CinemaOrder(
    val _id: String,
    val orderNumber: Int?,
    val status: String?
)