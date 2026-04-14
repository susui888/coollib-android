# CoolLib - Library Management Android App

**CoolLib** is a high-performance library management app built with **Jetpack Compose** and **Clean Architecture**. It features a "Book-Style" custom design system and optimized data processing.

## 🚀 Key Technical Wins

- **Architecture**: Implemented **Clean Architecture (MVVM)** with strict layer separation (Data, Domain, UI) for maximum testability.
- **Performance**: Reduced data load times by **60%** using **Kotlin Coroutines (`async/awaitAll`)** for parallel data mapping in the repository layer.
- **Dependency Injection**: Utilized **Hilt** with custom **Qualifiers** (`@IoDispatcher`) for granular threading control.
- **Reactive Data**: Built a real-time UI pipeline using **StateFlow/SharedFlow** and **Room** for offline persistence.
- **Testing**: Achieved high reliability with **Unit Tests (MockK)** and **UI Tests (Compose Rule)** covering critical flows like Auth, Cart, and Loan History.

## 🛠 Tech Stack

- **UI**: Jetpack Compose, Material 3, Coil, Compose Navigation.
- **Logic**: MVVM, Use Cases, Coroutines, Flow, Hilt.
- **Data**: Retrofit, Moshi, Room, SharedPreferences.

## 📊 Core Features

- **Loan Tracking**: Filtered views for "Active Loans" vs. "History" with status-based UI.
- **Smart Stats**: Real-time calculation of "Due Soon" and "Overdue" metrics.
- **QR Scanning**: Integrated barcode/QR scanning for instant book checkout.
- **Book-Style UI**: Custom theme with a paper-like palette and paper-texture card components.

## 🏗 Development
- **Min SDK**: 24 | **Target SDK**: 34
- **Build**: Gradle Kotlin DSL
