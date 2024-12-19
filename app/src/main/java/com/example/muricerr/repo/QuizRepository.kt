package com.example.muricerr.repo

import com.example.muricerr.database.PlaylistDAO
import com.example.muricerr.database.PlaylistTrackDAO
import com.example.muricerr.database.QuizDao
import com.example.muricerr.model.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(
    private val quizDao: QuizDao,
    private val playlistDao: PlaylistDAO,
    private val playlistTrackDao: PlaylistTrackDAO
) {

    // Fetch all quizzes (background thread)
    suspend fun getAllQuizzes() = withContext(Dispatchers.IO) {
        quizDao.getAllQuizzes()
    }

    // Delete a quiz (background thread)
    suspend fun deleteQuiz(quiz: Quiz) = withContext(Dispatchers.IO) {
        quizDao.deleteQuiz(quiz)
    }

    // Create a new quiz (background thread)
    suspend fun createQuiz(quiz: Quiz) = withContext(Dispatchers.IO) {
        quizDao.insertQuiz(quiz)
    }

    // Fetch playlists with at least `minSongs` songs (background thread)
    suspend fun getPlaylistsWithMinSongs(minSongs: Int) = withContext(Dispatchers.IO) {
        // Fetch all playlists and filter those with enough tracks
        playlistDao.getAllPlaylists().filter { playlist ->
            playlistTrackDao.getTracksForPlaylist(playlist.id).size >= minSongs
        }
    }
}
