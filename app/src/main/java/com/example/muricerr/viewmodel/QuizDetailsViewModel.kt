package com.example.muricerr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muricerr.database.PlaylistDAO
import com.example.muricerr.database.PlaylistEntity
import com.example.muricerr.database.QuizDao
import com.example.muricerr.model.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class QuizDetailsViewModel(
    private val quizDao: QuizDao,
    private val playlistDao: PlaylistDAO
) : ViewModel() {

    private val _quizDetails = MutableLiveData<Quiz>()
    val quizDetails: LiveData<Quiz> get() = _quizDetails

    private val _playlistDetails = MutableLiveData<PlaylistEntity?>()
    val playlistDetails: LiveData<PlaylistEntity?> get() = _playlistDetails

    fun fetchQuizDetails(quizId: Long) {
        viewModelScope.launch {
            val quiz = withContext(Dispatchers.IO) {
                quizDao.getQuizById(quizId)
            }

            val playlist = withContext(Dispatchers.IO) {
                playlistDao.getPlaylistById(quiz.playlistId)
            }

            _quizDetails.postValue(quiz)
            _playlistDetails.postValue(playlist)
        }
    }
}
