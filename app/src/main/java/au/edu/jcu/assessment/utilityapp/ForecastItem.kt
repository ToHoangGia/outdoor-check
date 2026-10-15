package au.edu.jcu.assessment.utilityapp

data class ForecastItem(
    val time: String,
    val temperature: Double?,
    val weatherCode: Int?,
    val rainProbability: Int?
)