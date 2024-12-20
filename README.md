# Music Application Report

## Overview
This report provides a detailed description of the Music Application project, its features, and its functionalities. The application allows users to search for music, manage playlists, and participate in music quizzes, with various interactive features.

## Features and Screenshots

### 1. Search Functionality
- Users can search for tracks and albums by keywords.
- Results are displayed in a user-friendly interface with separate tabs for tracks and albums.

#### Search Fragment:
![Search Fragment](src/e1.jpeg)

#### Search Results:
![Search Results](src/e2.jpeg)
(Here we usad recycled view to show the resut)
### 2. Album View
- Clicking on an album opens the **AlbumTrackFragment**, displaying all tracks within the album.

#### Album Details:
![Album Details](src/e3.jpeg)

### 3. Music Playback
- Users can play tracks directly from search results or playlists.
- A detailed music playback view is provided with options to like, add to favorites, or view album details.

#### Playback Screen:
![Music Playback](src/e4.jpeg)

### 4. Favorites
- Tracks can be added to the **Favorites List**, which is managed using SharedPreferences.

#### Favorites List:
![Favorites List](src/e5.jpeg)

### 5. Playlist Management
- Users can create and delete playlists.
- Songs can be added to playlists, and playlists can be accessed to play or remove tracks.

#### Create Playlist:
![Create Playlist](src/e10.jpeg)

#### Add to Playlist:
![Add to Playlist](src/e12.jpeg)

### 6. Music Quiz
- Users can participate in music quizzes, which include multiple-choice and open-ended questions.
- Quizzes are based on tracks available in the user’s playlists.

#### Quiz Selection:
![Quiz Selection](src/e6.jpeg)

#### Quiz Questions:
![Quiz Questions](src/e7.jpeg)

#### Open-ended Quiz:
![Open-ended Quiz](src/e8.jpeg)

#### Quiz Results:
![Quiz Results](src/e9.jpeg)

### 7. User Profile and Settings
- Users can customize their application settings and manage their profiles.

#### Settings:
![Settings](src/e14.jpeg)

#### User Profile:
![User Profile](src/e15.jpeg)

### 8. Authentication
- The app includes login, signup, and forgot password functionalities to ensure secure access.

#### Login Page:
![Login Page](src/e16.jpeg)

#### Signup Page:
![Signup Page](src/e17.jpeg)

#### Forgot Password:
![Forgot Password](src/e18.jpeg)

### 9. Main Menu
- Provides navigation to all the core features of the application.(bottom_naviation_bar)
  We added bottom navigation bar to the bottom of the menu which allows us to do action between fragments(Search, Allquiz, Playlist, Fauvarits)

#### Main Menu:
![Main Menu](src/e19.jpeg)

## Technologies Used
- **Kotlin**: Programming language for Android development.
- **Room Database**: For local data storage.
- **SharedPreferences**: For managing user preferences.
- **RecyclerView**: For displaying lists of tracks and albums.
- **Fragments**: For modular UI components.

## How to Run the Application
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync Gradle to download the required dependencies.
4. Run the application on an emulator or a physical device.

## Conclusion
The Music Application combines various features like music search, playlist management, and quizzes to provide a seamless and enjoyable experience for its users. The screenshots included in this document demonstrate the application’s functionalities and user interface.
