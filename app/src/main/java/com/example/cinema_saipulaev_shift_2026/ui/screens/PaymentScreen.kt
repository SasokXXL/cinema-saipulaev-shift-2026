package com.example.cinema_saipulaev_shift_2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinema_saipulaev_shift_2026.data.model.CreateCinemaPaymentDto
import com.example.cinema_saipulaev_shift_2026.data.model.CreatePaymentDebitCardDto
import com.example.cinema_saipulaev_shift_2026.data.model.CreatePaymentPersonDto
import com.example.cinema_saipulaev_shift_2026.data.model.CreatePaymentSeanceDto
import com.example.cinema_saipulaev_shift_2026.data.model.CreatePaymentTicketsDto
import com.example.cinema_saipulaev_shift_2026.viewmodel.CinemaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    filmId: String,
    date: String,
    time: String,
    selectedSeats: List<SelectedSeat>,
    viewModel: CinemaViewModel,
    onBackClick: () -> Unit,
    onSuccessClick: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val paymentResult by viewModel.paymentResult.collectAsState()

    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var middlename by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var cardNumber by remember { mutableStateOf("") }
    var expireDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val totalPrice = selectedSeats.sumOf { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Оплата билетов")
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ваш заказ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Дата: $date")
                    Text(text = "Время: $time")
                    Text(text = "Мест: ${selectedSeats.size}")
                    Text(
                        text = "Итого: ${totalPrice.toInt()} ₽",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Персональные данные",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = middlename,
                onValueChange = { middlename = it },
                label = { Text("Отчество") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Банковская карта",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Номер карты") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expireDate,
                onValueChange = { expireDate = it },
                label = { Text("Срок действия, например 12/28") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cvv,
                onValueChange = { cvv = it },
                label = { Text("CVV") },
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(
                    text = error ?: "Ошибка",
                    color = MaterialTheme.colorScheme.error
                )
            }

            paymentResult?.let { result ->
                if (result.success) {
                    Text(
                        text = "Билеты успешно оплачены",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            viewModel.clearPaymentResult()
                            onSuccessClick()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Вернуться к афише")
                    }
                }
            }

            Button(
                onClick = {
                    val request = CreateCinemaPaymentDto(
                        filmId = filmId,
                        person = CreatePaymentPersonDto(
                            firstname = firstname,
                            lastname = lastname,
                            middlename = middlename,
                            phone = phone
                        ),
                        debitCard = CreatePaymentDebitCardDto(
                            pan = cardNumber,
                            expireDate = expireDate,
                            cvv = cvv
                        ),
                        seance = CreatePaymentSeanceDto(
                            date = date,
                            time = time
                        ),
                        tickets = selectedSeats.map { seat ->
                            CreatePaymentTicketsDto(
                                row = seat.row,
                                column = seat.column
                            )
                        }
                    )

                    viewModel.createPayment(request)
                },
                enabled = !isLoading &&
                        firstname.isNotBlank() &&
                        lastname.isNotBlank() &&
                        middlename.isNotBlank() &&
                        phone.isNotBlank() &&
                        cardNumber.isNotBlank() &&
                        expireDate.isNotBlank() &&
                        cvv.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Оплатить")
                }
            }
        }
    }
}