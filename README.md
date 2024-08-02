# Event Management App

## Overview

The Event Management App is an Android application developed using Kotlin, Jetpack Compose for UI, Room for data persistence, and Koin for dependency injection. The app follows the MVVM architecture pattern. It allows users to create, view, and update events, manage participants for those events, and search for events.

## Features

- Create, update, and view events.
- Add participants to events.
- Handle date and time collisions for participants.
- Search for events by name.
- Clean and responsive UI.
- Data persistence with Room database.
- Dependency injection using Koin.
- Background tasks using coroutines.

## Prerequisites

- Android Studio (latest version recommended)
- Gradle
- An Android device or emulator running API level 21 or higher

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Clone the Repository

git clone https://github.com/nehagarg702/EventsManagemnetApp.git

### Open the Project in Android Studio

1. Open Android Studio.
2. Click on `File` > `Open...`.
3. Navigate to the directory where you cloned the project and select the `build.gradle` file to open the project.

### Build the Project

1. Ensure you have a stable internet connection to download the necessary dependencies.
2. In Android Studio, click on the `Sync Project with Gradle Files` button.
3. Wait for the project to sync and build.

### Run the App

1. Connect your Android device via USB or start an Android emulator.
2. Click on the `Run` button in Android Studio or press `Shift + F10`.
3. Select the device or emulator where you want to install and run the app.

## Key Components

- **MVVM Architecture**: The application is structured using the Model-View-ViewModel architecture to separate concerns and make the codebase more maintainable.
- **Room Database**: Used for local data storage. Entities include `Event`, `Participant`, and `EventParticipant` to manage events and participants effectively.
- **Dependency Injection with Koin**: Simplifies the injection of dependencies and reduces boilerplate code.
- **Jetpack Compose**: Provides a modern toolkit for building native UIs with a declarative approach.
- **Search Feature**: Allows users to search for events by name using a search bar integrated into the main event list.
- **Coroutines for Background Tasks**: Utilized to handle asynchronous operations and background tasks efficiently, ensuring smooth performance and responsiveness of the app.
