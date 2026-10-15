package au.edu.jcu.assessment.utilityapp

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val current: CurrentWeather?,
    val hourly: HourlyWeather?,
    val timezone: String?
)

data class CurrentWeather(
    val time: String?,

    @SerializedName("temperature_2m")
    val temperature: Double?,

    @SerializedName("weather_code")
    val weatherCode: Int?
)

data class HourlyWeather(
    val time: List<String>?,

    @SerializedName("temperature_2m")
    val temperatures: List<Double?>?,

    @SerializedName("weather_code")
    val weatherCodes: List<Int?>?,

    @SerializedName("precipitation_probability")
    val rainProbabilities: List<Int?>?
)