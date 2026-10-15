package au.edu.jcu.assessment.utilityapp

enum class WeatherCity(
    val displayName: String,
    val latitude: Double,
    val longitude: Double
) {
    SINGAPORE("Singapore", 1.3521, 103.8198),
    TOWNSVILLE("Townsville", -19.2590, 146.8169),
    BRISBANE("Brisbane", -27.4698, 153.0251)
}