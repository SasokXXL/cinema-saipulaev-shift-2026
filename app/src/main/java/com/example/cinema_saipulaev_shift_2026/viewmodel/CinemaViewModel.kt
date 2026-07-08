package com.example.cinema_saipulaev_shift_2026.viewmodel

import com.example.cinema_saipulaev_shift_2026.data.model.CreateCinemaPaymentDto
import com.example.cinema_saipulaev_shift_2026.data.model.PaymentResponse
import com.example.cinema_saipulaev_shift_2026.data.model.FilmSchedule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema_saipulaev_shift_2026.data.model.Film
import com.example.cinema_saipulaev_shift_2026.data.repository.CinemaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CinemaViewModel : ViewModel() {

    private val repository = CinemaRepository()

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films

    private val _selectedFilm = MutableStateFlow<Film?>(null)
    val selectedFilm: StateFlow<Film?> = _selectedFilm

    private val _schedules = MutableStateFlow<List<FilmSchedule>>(emptyList())
    val schedules: StateFlow<List<FilmSchedule>> = _schedules

    private val _paymentResult = MutableStateFlow<PaymentResponse?>(null)
    val paymentResult: StateFlow<PaymentResponse?> = _paymentResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadFilms() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _films.value = repository.getFilms()
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить фильмы"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadFilmById(filmId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _selectedFilm.value = repository.getFilm(filmId)
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить фильм"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedFilm() {
        _selectedFilm.value = null
    }

    fun loadSchedule(filmId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _schedules.value = repository.getFilmSchedule(filmId)
            } catch (e: Exception) {
                _error.value = "Для этого фильма расписание пока недоступно"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSchedule() {
        _schedules.value = emptyList()
    }

    fun createPayment(request: CreateCinemaPaymentDto) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _paymentResult.value = repository.createPayment(request)
            } catch (e: Exception) {
                _error.value = e.message ?: "Не удалось оплатить билеты"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearPaymentResult() {
        _paymentResult.value = null
    }
}