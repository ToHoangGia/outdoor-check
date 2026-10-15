package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        refreshWeather()
    }

    fun setTemperatureUnit(useCelsius: Boolean) {
        _uiState.update {
            it.copy(useCelsius = useCelsius)
        }
    }

    fun refreshWeather() {
        // Prevent repeated taps from starting overlapping requests.
        if (_uiState.value.isLoading) return
        val city = _uiState.value.selectedCity

        _uiState.update {
            it.copy(
                isLoading = true,
                weather = null,
                forecast = emptyList(),
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                val response = repository.getWeather(
                    latitude = city.latitude,
                    longitude = city.longitude
                )

                val temperature = response.current?.temperature

                if (temperature == null || !temperature.isFinite()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage =
                                "No current temperature is available. Please retry."
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            weather = response,
                            forecast = buildForecast(response)
                        )
                    }
                }
            } catch (exception: CancellationException) {
                // Allow cancellation when the ViewModel is cleared.
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Weather could not be loaded. Check your connection and retry."
                    )
                }
            }
        }
    }

    class Factory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(repository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun selectCity(city: WeatherCity) {
        val currentState = _uiState.value

        if (currentState.isLoading || currentState.selectedCity == city) {
            return
        }

        _uiState.update {
            it.copy(selectedCity = city)
        }

        refreshWeather()
    }

    private fun buildForecast(
        response: WeatherResponse
    ): List<ForecastItem> {
        val hourly = response.hourly ?: return emptyList()

        val currentTime = try {
            LocalDateTime.parse(
                response.current?.time ?: return emptyList()
            )
        } catch (exception: DateTimeParseException) {
            return emptyList()
        }

        return hourly.time.orEmpty()
            .mapIndexedNotNull { index, time ->
                val forecastTime = try {
                    LocalDateTime.parse(time)
                } catch (exception: DateTimeParseException) {
                    return@mapIndexedNotNull null
                }

                if (!forecastTime.isAfter(currentTime)) {
                    return@mapIndexedNotNull null
                }

                ForecastItem(
                    time = time,
                    temperature = hourly.temperatures
                        ?.getOrNull(index)
                        ?.takeIf { it.isFinite() },
                    weatherCode = hourly.weatherCodes?.getOrNull(index),
                    rainProbability = hourly.rainProbabilities
                        ?.getOrNull(index)
                        ?.takeIf { it in 0..100 }
                )
            }
            .take(6)
    }
}