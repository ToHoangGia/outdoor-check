# Outdoor Check

## App purpose
Provide a way for people to check weather in preset cities.

## Features
Current temperature and weather description; upcoming hourly forecast with rain probability; Singapore, Townsville, and Brisbane selection; Celsius/Fahrenheit; refresh; loading and error messages; main and settings screens.

## How to run
Open the project in Android Studio, let Gradle sync, select an emulator or device running Android 8.0/API 26 or newer, and run the app with Internet access. No API key is required by the current implementation.

## Project structure
| File | Responsibility |
|---|---|
| `MainActivity.kt` | Connects the ViewModel to Compose and contains the screens and forecast display. |
| `WeatherUiState.kt` | Represents weather, forecast, selected city, units, loading, and errors. |
| `WeatherViewModel.kt` | Updates screen state, requests weather, and prepares forecast entries. |
| `WeatherRepository.kt` | Provides weather data through the API service. |
| `WeatherApiService.kt` | Defines the Retrofit weather request. |
| `AppContainer.kt` | Creates the API service and repository for dependency injection. |
| `WeatherCity.kt` | Defines the supported cities and their coordinates. |

## Limitations
Only three preset cities; Internet required; no offline weather cache; preferences are not saved across a fresh app launch.

## Data source and acknowledgements
Link to Open-Meteo, AI used to help write certain part of code