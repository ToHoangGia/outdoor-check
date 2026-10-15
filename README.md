# Outdoor Check

## Introduction

- Who it helps: people checking conditions before going outside.
- What it shows: city, temperature, weather description, and a short forecast.
- Its current limitation: all weather values are sample data; live weather is not connected.

## Features

- Main and settings screens with bottom navigation.
- Sample weather for Singapore.
- Celsius/Fahrenheit selection.
- Temperature conversion on the main screen.

## Composables

| File or component | Responsibility |
|---|---|
| `MainActivity.kt` | Starts the app and connects the ViewModel state to Compose. |
| `UtilityApp()` | Provides bottom navigation and passes state and callbacks to screens. |
| `UtilityScreen()` | Displays sample weather using the selected temperature unit. |
| `SettingsScreen()` | Displays the unit preference and sends change requests. |
| `WeatherUiState.kt` | Describes shared UI state, currently `useCelsius`. |
| `WeatherViewModel.kt` | Owns shared state and updates it through `StateFlow`. |
| `formatTemperature()` | Formats Celsius values or converts them to Fahrenheit. |

## Remaining Works

- City selection.
- Weather cards and a scrollable layout.
- Repository and dependency injection.
- Live weather through Retrofit.
- Loading/error states and update timestamps.
- ViewModel unit tests.