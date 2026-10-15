package au.edu.jcu.assessment.utilityapp

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current")
        current: String = "temperature_2m,weather_code",
        @Query("hourly")
        hourly: String =
            "temperature_2m,weather_code,precipitation_probability",
        @Query("temperature_unit")
        temperatureUnit: String = "celsius",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 2
    ): WeatherResponse
}