# Finance Tracker App

[![en](https://img.shields.io/badge/lang-en-red.svg)](README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](README.it.md)

*Read this in [Italian](README.it.md).*

Android application for personal finance management, built entirely with Kotlin and Jetpack Compose, following the MVVM architecture.

> **Note:** The application interface and content are currently in Italian, but they will be also in English.

## Features

* **Multi-Account Management**: Create and manage different accounts (e.g., Debit Card, Cash, etc.).
* **Expense & Income Tracking**: Quickly add transactions via an intuitive user interface.
* **Custom Categories**: Assign specific categories to every transaction.
* **Charts & Statistics**: Currently under development.
* **Time Filters**: Filter transactions by date. Feature currently being finalized.
* **Local Database**: Data is stored locally, securely, and persistently.

## Tech Stack & Architecture

* **Language**: [Kotlin](https://kotlinlang.org/) (100%)
* **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Navigation**: Jetpack Navigation Compose
* **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
* **Database**: [Room](https://developer.android.com/training/data-storage/room)

## How to Run

An APK will be available upon completion of the first release.

Currently, you need to build the project from source:

1. **Clone the repository**
   ```bash
   git clone [https://github.com/sofialottii/FinanceTracker.git](https://github.com/sofialottii/FinanceTracker.git)
   ```

2. Open the project in Android Studio and wait for the Gradle synchronization to finish.

3. Connect an Android device (or start an Emulator).

4. Click Run.
