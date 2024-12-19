package com.example.muricerr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muricerr.model.Quiz
import com.example.muricerr.repo.QuizRepository
import kotlinx.coroutines.launch

class AllQuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>> get() = _quizzes

    private val _playlistsWithSongs = MutableLiveData<List<Pair<Long, String>>>()
    val playlistsWithSongs: LiveData<List<Pair<Long, String>>> get() = _playlistsWithSongs

    fun fetchQuizzes() {
        viewModelScope.launch {
            _quizzes.value = repository.getAllQuizzes()
        }
    }

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch {
            repository.deleteQuiz(quiz)
            fetchQuizzes()
        }
    }

    fun createQuiz(name: String, playlistId: Long) {
        viewModelScope.launch {
            repository.createQuiz(Quiz(name = name, playlistId = playlistId))
            fetchQuizzes()
        }
    }

    fun fetchPlaylistsWithMinSongs(minSongs: Int) {
        viewModelScope.launch {
            val playlists = repository.getPlaylistsWithMinSongs(minSongs)
            _playlistsWithSongs.value = playlists.map { it.id to it.name }
        }
    }
}
