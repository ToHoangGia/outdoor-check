package au.edu.jcu.assessment.utilityapp

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successfulRequest_changesLoadingToWeather() = runTest {
        val repository = FakeWeatherRepository()
        val viewModel = WeatherViewModel(repository)

        // The ViewModel starts loading when it is created.
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.weather)

        // Allow the queued request to finish.
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(repository.response, state.weather)
    }

    @Test
    fun failedRequest_canRecoverWhenRetried() = runTest {
        val repository = FakeWeatherRepository()
        repository.shouldFail = true
        val viewModel = WeatherViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.weather)

        // Simulate the connection becoming available again.
        repository.shouldFail = false
        viewModel.refreshWeather()

        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(
            repository.response,
            viewModel.uiState.value.weather
        )
    }

    private class FakeWeatherRepository : WeatherRepository {
        var shouldFail = false

        val response = WeatherResponse(
            current = CurrentWeather(
                time = "2026-10-19T12:00",
                temperature = 30.0,
                weatherCode = 0
            ),
            hourly = null,
            timezone = "Asia/Singapore"
        )

        override suspend fun getWeather(
            latitude: Double,
            longitude: Double
        ): WeatherResponse {
            if (shouldFail) {
                throw IOException("Simulated connection failure")
            }
            return response
        }
    }
}