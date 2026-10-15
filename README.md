# MobileTech Utility App

This is a basic Android app template for **Assessment 1: Utility App**.
It provides the structure for a simple tabular UI using **Jetpack Compose** and **Material Design 3**.

---

## Getting Started

### How to Run
1. Clone or download this repo  
2. Open in Android Studio  
3. Run on an emulator or physical device (API 26+ recommended)  

---

## Composables

### UtilityApp()
- Contains the screen layout using a Scaffold
- Toggles content between Utility and Settings

### UtilityScreen()
- Displays a simple counter (replace with your utility logic)  
- Includes a button to increment the counter

### SettingsScreen()
- Placeholder for user preferences or configuration  
- Can be extended to modify main screen behavior (e.g., theme, units, limits)  

---

## Key Concepts Covered

| Week | Concept                        | Used In                          |
|------|--------------------------------|----------------------------------|
| 1    | Kotlin + Android Studio         | MainActivity.kt |
| 2    | Jetpack Compose Layouts         | UtilityApp(), UtilityScreen(), SettingsScreen()   |
| 3    | Material Design 3               | MobileTechUtilityAppTheme, MaterialTheme.typography |
| 4    | ViewModel | Not included in starter          |
| 5    | Retrofit  | Not included in starter          |

---

## Suggested Extensions
- Replace counter with a real utility (e.g., hydration tracker, timer)  
- Add a ViewModel for state management  
- Use SharedPreferences or DataStore to persist settings  
- Add a simple API call using Retrofit (e.g., fetch weather or quotes)  

---

## 📚 License
This template is provided for educational use in CP3406.  
Feel free to modify and extend it for your assessment.


| Item | Your Outdoor Check decision |
|---|---|
| App purpose | Help users quickly check weather before leaving home or planning an outdoor activity. |
| Information display | Use a prominent current-weather card and a short forecast list. These cards are planned; the current prototype uses text. |
| Navigation | Keep the template’s two bottom tabs for the main screen and settings. |
| Accessibility improvement | Make the screen scrollable so information remains accessible on small screens and with larger text. |
| Trust and clarity | Clearly label the current weather values as sample data. When live data is added, display its update time and any loading errors. |
| Privacy | Let users select a city manually without requiring location permission. |
| Testing observations | Write what happened during the checks above, including anything that needs improvement. |
| Tutor feedback question | “Does my main screen communicate the app’s purpose clearly, and is the amount of weather information appropriate?” |
