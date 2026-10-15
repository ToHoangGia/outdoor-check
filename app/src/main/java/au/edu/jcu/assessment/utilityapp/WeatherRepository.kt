package au.edu.jcu.assessment.utilityapp

interface WeatherRepository {
    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResponse
}

class NetworkWeatherRepository(
    private val apiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        return apiService.getWeather(
            latitude = latitude,
            longitude = longitude
        )
    }
}