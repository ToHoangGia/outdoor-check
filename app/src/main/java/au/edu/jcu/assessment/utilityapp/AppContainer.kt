package au.edu.jcu.assessment.utilityapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val apiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    val weatherRepository: WeatherRepository by lazy {
        NetworkWeatherRepository(apiService)
    }
}