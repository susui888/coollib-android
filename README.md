# CoolLib - Library Management Android App

CoolLib is a modern Android application designed for library management, built with the latest Android development tools and practices. The app features a clean, "book-inspired" UI and a robust architecture to handle book discovery, borrowing history, and user statistics.

## 📖 Features

- **Book Discovery**: Explore categories, new arrivals, and search for books by author, publisher, or year.
- **Loan History**: Track currently borrowed books, return history, and overdue items with a dedicated filtered view.
- **Real-time Statistics**: Monitor library usage with dynamic metrics (Currently Borrowed, Due Soon, Overdue, Total Borrowed).
- **QR/Barcode Scanner**: Quickly scan books for checkout or information.
- **User Authentication**: Secure login and registration system.
- **Offline Support**: Local database integration with Room for recently viewed books and wishlist.
- **Modern Theme**: A customizable "Book Style" theme with a warm, paper-like color palette.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a fully declarative and modern UI.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) for type-safe app navigation.
- **Concurrency**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html).
- **Dependency Injection**: [Hilt (Dagger)](https://developer.android.com/training/dependency-injection/hilt-android).
- **Networking**: [Retrofit](https://square.github.io/retrofit/) with Moshi for API communication.
- **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room) for persistent data.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for efficient asynchronous image loading.
- **Architecture**: Clean Architecture with MVVM (Model-View-ViewModel) pattern.
- **Testing**: MockK and Kotlin Coroutines Test for unit testing.

## 🏗 Project Structure

```text
com.example.coollib/
├── data/             # Data Layer: Repositories, API interfaces, DAOs, Entities, Mappers
├── domain/           # Domain Layer: Business logic, Use Cases, Models, Repository Interfaces
├── di/               # Dependency Injection: Hilt Modules
└── ui/               # UI Layer: Screens, ViewModels, Navigation, Components, Theme
```

## 🎨 Theme & Styling

The app uses a custom brown/cream color scheme to provide a reading-centric experience. It includes custom components like:
- **Book-style BottomBar**: Flat paper design with top border.
- **Elevated Cards**: Material 3 cards for book items and categories.
- **Status Badges**: Color-coded badges for loan statuses (Borrowed, Returned, Overdue).

## 🧪 Testing

The project includes unit tests for Repositories and ViewModels using MockK.
- **LoanRepositoryImplTest**: Verifies parallel book fetching and error handling.
- **ViewModel Tests**: Covers authentication, wishlist, and cart logic.

## 🚀 Getting Started

1. Clone the repository.
2. Open in Android Studio (Koala or newer recommended).
3. Sync Gradle and build the project.
4. Run the app on an emulator or physical device (API 24+).
