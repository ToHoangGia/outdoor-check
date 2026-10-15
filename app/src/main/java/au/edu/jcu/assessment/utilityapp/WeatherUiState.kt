package au.edu.jcu.assessment.utilityapp

data class WeatherUiState(
    val useCelsius: Boolean = true,
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val errorMessage: String? = null,
    val selectedCity: WeatherCity = WeatherCity.SINGAPORE,
)