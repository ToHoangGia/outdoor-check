package au.edu.jcu.assessment.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileTech_UtilityAppTheme {
                UtilityApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    MobileTech_UtilityAppTheme {
        UtilityApp()
    }
}

@Composable
fun UtilityApp() {
    var selectedTab by remember { mutableStateOf("Utility") }
    var useCelsius by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                    label = { Text("Utility") },
                    selected = selectedTab == "Utility",
                    onClick = { selectedTab = "Utility" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Utility" -> UtilityScreen(useCelsius = useCelsius)
                "Settings" -> SettingsScreen(
                    useCelsius = useCelsius,
                    onUnitChange = { useCelsius = it }
                )
            }
        }
    }
}

@Composable
fun UtilityScreen(useCelsius: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Outdoor Check",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Singapore",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = formatTemperature(30, useCelsius),
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = "Partly cloudy",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Sample data — live weather is not connected yet.",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Upcoming weather",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "10:00 AM · ${formatTemperature(30, useCelsius)} · Partly cloudy",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "11:00 AM · ${formatTemperature(31, useCelsius)} · Cloudy",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "12:00 PM · ${formatTemperature(32, useCelsius)} · Light rain",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SettingsScreen(
    useCelsius: Boolean,
    onUnitChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Temperature unit",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = if (useCelsius) {
                "Current unit: Celsius (°C)"
            } else {
                "Current unit: Fahrenheit (°F)"
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = { onUnitChange(true) },
            enabled = !useCelsius
        ) {
            Text("Use Celsius (°C)")
        }

        Button(
            onClick = { onUnitChange(false) },
            enabled = useCelsius
        ) {
            Text("Use Fahrenheit (°F)")
        }
    }
}

fun formatTemperature(celsius: Int, useCelsius: Boolean): String {
    return if (useCelsius) {
        "$celsius°C"
    } else {
        val fahrenheit = celsius * 9.0 / 5.0 + 32
        "${Math.round(fahrenheit)}°F"
    }
}