package com.example.muricerr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.model.Quiz
import com.example.muricerr.model.Track
import com.example.muricerr.network.DeezerApiInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpenQuizViewModel(private val database: AppDatabase) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _currentTrack = MutableLiveData<Track?>()
    val currentTrack: LiveData<Track?> get() = _currentTrack

    private var quiz: Quiz? = null

    fun loadQuizData(quizId: Long) {
        viewModelScope.launch {
            quiz = fetchQuizById(quizId)
            val trackIds = fetchTrackIdsForQuiz(quiz!!)
            fetchTracksByIds(trackIds)
        }
    }

    private suspend fun fetchQuizById(quizId: Long): Quiz {
        return withContext(Dispatchers.IO) {
            database.quizDao().getQuizById(quizId)
                ?: throw IllegalArgumentException("Quiz not found")
        }
    }

    private suspend fun fetchTrackIdsForQuiz(quiz: Quiz): List<Long> {
        return withContext(Dispatchers.IO) {
            database.playlistTrackDao().getTracksForPlaylist(quiz.playlistId).map { it.trackId }
        }
    }

    private fun fetchTracksByIds(trackIds: List<Long>) {
        val fetchedTracks = mutableListOf<Track>()
        trackIds.forEach { id ->
            DeezerApiInstance.api.getTrackById(id).enqueue(object : retrofit2.Callback<Track> {
                override fun onResponse(call: retrofit2.Call<Track>, response: retrofit2.Response<Track>) {
                    response.body()?.let { fetchedTracks.add(it) }
                    if (fetchedTracks.size == trackIds.size) {
                        _tracks.postValue(fetchedTracks.shuffled())
                        _currentIndex.postValue(0)
                        _currentTrack.postValue(fetchedTracks.firstOrNull())
                    }
                }

                override fun onFailure(call: retrofit2.Call<Track>, t: Throwable) {

                }
            })
        }
    }

    fun submitAnswer(userAnswer: String) {
        if (_tracks.value == null) return

        val currentTrack = _tracks.value!![_currentIndex.value!!]
        if (userAnswer.equals(currentTrack.title, ignoreCase = true)) {
            _score.postValue((_score.value ?: 0) + 1)
        }
        nextTrack()
    }

    fun skipTrack() {
        nextTrack()
    }

    private fun nextTrack() {
        val nextIndex = (_currentIndex.value ?: 0) + 1
        if (nextIndex < (_tracks.value?.size ?: 0)) {
            _currentIndex.postValue(nextIndex)
            _currentTrack.postValue(_tracks.value?.get(nextIndex))
        } else {
            _currentTrack.postValue(null) // Indicate quiz completion
        }
    }
}
