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
                            weather = response
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
}