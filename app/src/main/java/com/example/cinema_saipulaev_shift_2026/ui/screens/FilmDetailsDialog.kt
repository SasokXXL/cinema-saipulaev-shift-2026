package com.example.cinema_saipulaev_shift_2026.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinema_saipulaev_shift_2026.data.model.Film

private fun getImageUrl(path: String): String {
    return "https://juniorsbootcamp.ru/api$path"
}

@Composable
fun FilmDetailsDialog(
    film: Film,
    onDismiss: () -> Unit,
    onChooseSession: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onChooseSession(film.id)
                }
            ) {
                Text(text = "Выбрать сеанс")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Закрыть")
            }
        },
        title = {
            Text(
                text = film.name,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                SubcomposeAsyncImage(
                    model = getImageUrl(film.img),
                    contentDescription = film.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Нет фото")
                        }
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = film.originalName,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Жанры: ${film.genres.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Длительность: ${film.runtime} мин",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Возрастной рейтинг: ${film.ageRating}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Кинопоиск: ${film.userRatings.kinopoisk} • IMDb: ${film.userRatings.imdb}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = film.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (film.directors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Режиссёр: ${film.directors.joinToString { it.fullName }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (film.actors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Актёры: ${film.actors.take(5).joinToString { it.fullName }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    )
}