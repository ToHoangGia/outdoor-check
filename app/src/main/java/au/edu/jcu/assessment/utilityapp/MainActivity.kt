package au.edu.jcu.assessment.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator

class MainActivity : ComponentActivity() {
    private val appContainer by lazy { AppContainer() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileTech_UtilityAppTheme {
                val weatherViewModel: WeatherViewModel = viewModel(
                    factory = WeatherViewModel.Factory(
                        appContainer.weatherRepository
                    )
                )
                val uiState by weatherViewModel.uiState.collectAsStateWithLifecycle()

                UtilityApp(
                    uiState = uiState,
                    onUnitChange = weatherViewModel::setTemperatureUnit,
                    onCityChange = weatherViewModel::selectCity,
                    onRefresh = weatherViewModel::refreshWeather
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    MobileTech_UtilityAppTheme {
        UtilityApp(
            uiState = WeatherUiState(),
            onUnitChange = {},
            onCityChange = {},
            onRefresh = {}
        )
    }
}

@Composable
fun UtilityApp(
    uiState: WeatherUiState,
    onUnitChange: (Boolean) -> Unit,
    onCityChange: (WeatherCity) -> Unit,
    onRefresh: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf("Utility") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                    label = { Text("Utility") },
                    selected = selectedTab == "Utility",
                    onClick = { selectedTab = "Utility" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Utility" -> UtilityScreen(
                    uiState = uiState,
                    onRefresh = onRefresh
                )

                "Settings" -> SettingsScreen(
                    useCelsius = uiState.useCelsius,
                    onUnitChange = onUnitChange,
                    selectedCity = uiState.selectedCity,
                    isLoading = uiState.isLoading,
                    onCityChange = onCityChange
                )
            }
        }
    }
}

@Composable
fun UtilityScreen(
    uiState: WeatherUiState,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Outdoor Check",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = uiState.selectedCity.displayName,
            style = MaterialTheme.typography.titleLarge
        )

        val currentWeather = uiState.weather?.current
        val temperature = currentWeather?.temperature
        val errorMessage = uiState.errorMessage

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
                Text("Loading weather…")
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            temperature != null -> {
                Text(
                    text = formatTemperature(
                        temperature,
                        uiState.useCelsius
                    ),
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = weatherDescription(currentWeather.weatherCode),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Weather data time: ${
                        currentWeather.time?.replace("T", " ")
                            ?: "Unavailable"
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Time zone: ${uiState.weather?.timezone ?: "Unavailable"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                ForecastSection(
                    forecast = uiState.forecast,
                    useCelsius = uiState.useCelsius
                )
            }

            else -> {
                Text("No weather data available. Tap Refresh.")
            }
        }

        Button(
            onClick = onRefresh,
            enabled = !uiState.isLoading
        ) {
            Text(
                if (errorMessage != null) "Retry" else "Refresh"
            )
        }

        Text(
            text = "Weather data: Open-Meteo.com (CC BY 4.0)",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SettingsScreen(
    useCelsius: Boolean,
    onUnitChange: (Boolean) -> Unit,
    selectedCity: WeatherCity,
    isLoading: Boolean,
    onCityChange: (WeatherCity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Weather location",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Selected city: ${selectedCity.displayName}",
            style = MaterialTheme.typography.bodyLarge
        )

        WeatherCity.entries.forEach { city ->
            Button(
                onClick = { onCityChange(city) },
                enabled = !isLoading && city != selectedCity
            ) {
                Text(city.displayName)
            }
        }

        if (isLoading) {
            Text("Updating weather…")
        }

        Text(
            text = "Temperature unit",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = if (useCelsius) {
                "Current unit: Celsius (°C)"
            } else {
                "Current unit: Fahrenheit (°F)"
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = { onUnitChange(true) },
            enabled = !useCelsius
        ) {
            Text("Use Celsius (°C)")
        }

        Button(
            onClick = { onUnitChange(false) },
            enabled = useCelsius
        ) {
            Text("Use Fahrenheit (°F)")
        }
    }
}

fun formatTemperature(
    celsius: Double,
    useCelsius: Boolean
): String {
    val temperature = if (useCelsius) {
        celsius
    } else {
        celsius * 9.0 / 5.0 + 32
    }

    val unit = if (useCelsius) "°C" else "°F"
    return "${Math.round(temperature)}$unit"
}

fun weatherDescription(code: Int?): String {
    return when (code) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Freezing drizzle"
        61, 63, 65 -> "Rain"
        66, 67 -> "Freezing rain"
        71, 73, 75 -> "Snow"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with hail"
        else -> "Weather description unavailable"
    }
}

