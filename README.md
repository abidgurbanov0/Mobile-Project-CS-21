
# Music Application Report

## Overview

The following is a guide to the Music Application project as follows; Upon deciding on a context, this application allows users to search for music and manage playlists as well as play music tracks and take part in a musical quiz. And the application is coded in Kotlin for modern technologies in Android development to provide a featureful and smooth experience in the mobile application.

---

## Features and Functionality

### 1. **Music Search**

- **Description**: Users can search for tracks and albums using keywords.
- **Interface**: Search results are displayed in a clean and user-friendly format, with separate tabs for tracks and albums.
- **Navigation**: Clicking on an album or track navigates to detailed views for more actions.

#### Key Screens:

- **Search Fragment**: Displays the search input and results interface. ![Search Fragment](src/SearchFragment.jpeg)
- **Search Results**: Presents categorized results for easy exploration. ![Search Results](src/SearchResults.jpeg)

### 2. **Album View**

- **Description**: Provides detailed information about albums, including a list of all associated tracks.
- **Navigation**: Accessible from the search results or playback view.

#### Key Screen:

- **Album Details**: Displays album metadata and tracks. ![Album Details](src/AlbumTrackFragment.jpeg)

### 3. **Music Playback**

- **Description**: Users can play tracks directly from search results, favorites, or playlists.
- **Features**:
  - Interactive controls for playback.
  - Options to like, add to favorites, or navigate to album details.

#### Key Screens:

- **Playback Screen**: Includes playback controls, track details, and interactive actions. ![Music Playback Screen](src/MusicPlaybackScreen.jpeg)
- **Track Added to Favorites**: Confirmation screen after adding a track to favorites. ![Track Added to Favorites](src/TrackAddedtoFavorites.jpeg)

### 4. **Favorites Management**

- **Description**: Users can save their favorite tracks to a dedicated list.
- **Storage**: Managed using SharedPreferences for persistence.
- **Features**: Add, view, and remove favorites.

#### Key Screen:

- **Favorites List**: Displays all favorited tracks. ![Favorites List](src/FavoritesList.jpeg)

### 5. **Playlist Management**

- **Description**: Users can create, edit, and delete playlists.
- **Features**:
  - Add tracks to playlists.
  - Play, rename, or delete playlists.

#### Key Screens:

- **Playlist Fragment**: Here you can play the music in a fragmant you are ![Playlist Fragment](src/PlaylistFragment.jpeg)
- **Playlist Creation**: Simplified interface to create a new playlist. ![Playlist Creation](src/PlaylistCreation.jpeg)
- **Playlist Naming**: Dialog for naming a new playlist. ![Playlist Naming](src/PlaylistNaming.jpeg)
- **Choose Playlist Dialog**: Select a playlist to add a track. ![Choose Playlist Dialog](src/ChoosePlaylistDialog.jpeg)
- **Playlist Track Playing**: Interface showing tracks being played from a playlist. ![Playlist Track Playing](src/PlaylistTrackPlaying.jpeg)
- **Playlist View**: Display of all tracks within a specific playlist. ![Playlist View](src/PlaylistView.jpeg)

### 6. **Interactive Music Quiz**

- **Description**: Engages users with quizzes based on tracks in their playlists.
- **Types**: Multiple-choice and open-ended questions.
- **Features**:
  - Score tracking.
  - Interactive feedback on answers.

#### Key Screens:

- **Quiz Fragment**: Central hub to start quizzes. ![Quiz Fragment](src/QuizFragment.jpeg)
- **Quiz Type Selection**: Choose between multiple-choice and open-ended quiz types. ![Quiz Type Selection](src/QuizTypeSelection.jpeg)
- **Multiple-Choice Quiz**: Interactive interface for answering multiple-choice questions. ![Multiple Choice Quiz](src/MultipleChoiceQuiz.jpeg)
- **Open-ended Quiz**: Engage with more complex open-ended questions. ![Open-ended Quiz](src/OpenEndedQuiz.jpeg)
- **Create Quiz Dialog**: Dialog for creating a new quiz. ![Create Quiz Dialog](src/CreateQuizDialog.jpeg)

### 7. **Application Architecture**

- **Description**: The application is designed using the MVVM (Model-View-ViewModel) architecture, ensuring separation of concerns and making the codebase scalable and maintainable.
- **Key Components**:
  1. **UI (Fragments)**: Represents the user interface, interacting with the ViewModel to display data and capture user input.
  2. **ViewModel**: Acts as a bridge between the UI and Repository, holding UI-related data and handling business logic.
  3. **Repository**: Manages data operations and serves as a single source of truth by interacting with APIs and databases.
  4. **Deezer API**: Provides the data layer, supplying music-related data to the application.

#### Architecture Flow Diagram:

![Application Architecture](src/AppStructure.jpeg)

---

## Technologies Used

### Programming Language

- **Kotlin**: Primary language for Android development.

### Storage and Persistence

- **Room Database**: For structured local data storage.
- **SharedPreferences**: Lightweight storage for user preferences and favorites.

### UI Components

- **RecyclerView**: Efficiently displays large lists of tracks, albums, and playlists.
- **Fragments**: Modular UI design for seamless navigation.

### Build System

- **Gradle**: Simplifies dependency management and project configuration.

---

## Contribution Guidelines

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes with detailed messages.
4. Open a pull request for review.

---
## Team

1. Abid Gurbanov (Swift intern at Internatinal bank of Azerbaijan)
App architecture.
Search screens.
Implemented the Multiple Choice and open questions
Create network Deezer API for fetching music data.
3. Mahammad Dadashov
Developed the Playlist and Quiz functionalities.

---
## Conclusion

As a fully functional application for music lovers, The Music Application comprises of four major components: search, playlist, music player, and quiz. This README is intended to provide an overview of how to use and deploy the application as well as detailing out some of the primary concerns and services of the application.
