# Loan Calculator App

A robust Android application for calculating loan repayments, viewing amortization schedules, and managing active loans. Built with modern Android technologies and a focus on clean architecture.

## How to Run the App

### Prerequisites
* **Android Studio Ladybug** (or newer)
* **JDK 17**
* **Android SDK 34** (Target SDK)
* A physical device or emulator running **Android 8.0 (API 26)** or higher.

### Steps
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   ```
2. **Open in Android Studio:**
   Launch Android Studio and select "Open" -> Navigate to the `TestApp` directory.
3. **Sync Gradle:**
   Wait for the project to sync. You can manually trigger this by clicking the "Elephant" icon in the top right.
4. **Run:**
   Select the `app` configuration and your device, then click the **Run** (Play) button.

---

## Architecture Overview

The project follows **Clean Architecture** principles and the **MVI (Model-View-Intent)** pattern for the UI layer to ensure separation of concerns, testability, and a predictable state flow.

### Layers
1.  **UI Layer (`ui` package):**
    *   **Compose:** Declarative UI using Jetpack Compose.
    *   **ViewModels:** Manage UI state and handle user intents using Kotlin Flows.
    *   **MVI:** State is emitted as a single `State` flow, and one-time events (like navigation) use an `Effect` flow.
2.  **Domain Layer (`domain` package):**
    *   **Models:** Pure Kotlin data classes representing business entities.
    *   **Use Cases:** Encapsulate specific business logic (e.g., `CalculateLoanUseCase`).
    *   **Repository Interfaces:** Define the contract for data operations.
3.  **Data Layer (`data` package):**
    *   **Local:** Room Persistence Library for caching loans and `EncryptedSharedPreferences` for secure data (like user preferences/tokens).
    *   **Remote (Ktor):** Implementation for network calls (e.g., syncing loan data).
    *   **Worker (WorkManager):** Handles background tasks like data synchronization.

### Dependency Injection
*   **Koin:** Used for lightweight dependency injection across all layers.

---

## Assumptions Made

*   **Currency:** The app assumes **KES (Kenyan Shillings)** as the primary currency for loan calculations.
*   **Interest Calculation:** The loan uses a standard monthly interest rate calculation. It assumes the interest rate provided in `LoanOption` is an annual rate divided by months or a flat monthly rate depending on the specific product.
*   **Authentication:** For the scope of this project, it is assumed the user is already authenticated or using a guest profile. Secure data storage is implemented via `SecurePrefsManager` to demonstrate security best practices.
*   **Data Freshness:** It's assumed that loan options change infrequently and can be cached locally.

---

## Trade-offs and Limitations

*   **Single Module:** The project currently uses a single-module structure (`:app`). While a multi-module approach (e.g., `:core`, `:domain`, `:feature:loan`) is better for larger teams, a single module was chosen for simplicity and faster development given the current scope.
*   **Local-First Approach:** The app is designed to work offline. While it includes a `LoanSyncWorker`, the primary data source for the UI is the local Room database. This improves performance but requires a strategy for handling sync conflicts in a real-world production environment.
*   **Simplified Amortization:** The amortization schedule assumes fixed monthly payments. It does not currently account for varying interest rates over time or early repayment penalties.
*   **UI Components:** Some UI components are shared across features for consistency. In a more complex app, these might be extracted into a dedicated design system library.

---

## Libraries Used
*   **UI:** Jetpack Compose, Material 3
*   **Concurrency:** Kotlin Coroutines & Flow
*   **Database:** Room
*   **Networking:** Ktor
*   **DI:** Koin
*   **Background Tasks:** WorkManager
*   **Security:** Jetpack Security (Crypto)
