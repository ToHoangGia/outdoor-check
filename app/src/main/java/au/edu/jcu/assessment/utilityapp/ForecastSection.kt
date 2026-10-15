package au.edu.jcu.assessment.utilityapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ForecastSection(
    forecast: List<ForecastItem>,
    useCelsius: Boolean
) {
    Text(
        text = "Upcoming weather",
        style = MaterialTheme.typography.titleLarge
    )

    if (forecast.isEmpty()) {
        Text("Hourly forecast is currently unavailable.")
    } else {
        forecast.forEach { item ->
            ForecastCard(
                item = item,
                useCelsius = useCelsius
            )
        }
    }
}

@Composable
private fun ForecastCard(
    item: ForecastItem,
    useCelsius: Boolean
) {
    val timeLabel = LocalDateTime.parse(item.time).format(
        DateTimeFormatter.ofPattern("EEE d MMM, HH:mm")
    )

    val temperatureLabel = item.temperature?.let {
        formatTemperature(it, useCelsius)
    } ?: "Temperature unavailable"

    val rainLabel = item.rainProbability?.let {
        "Chance of precipitation: $it%"
    } ?: "Precipitation probability unavailable"

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = timeLabel,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = temperatureLabel,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = weatherDescription(item.weatherCode),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = rainLabel,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}