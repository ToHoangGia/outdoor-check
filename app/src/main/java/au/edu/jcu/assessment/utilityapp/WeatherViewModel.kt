package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())

    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun setTemperatureUnit(useCelsius: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(useCelsius = useCelsius)
        }
    }
}