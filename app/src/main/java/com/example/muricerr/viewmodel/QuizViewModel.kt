package com.example.muricerr.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muricerr.database.PlaylistTrackDAO
import com.example.muricerr.database.QuizDao
import com.example.muricerr.model.Quiz
import com.example.muricerr.model.Track
import com.example.muricerr.network.DeezerApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel(
    private val quizDao: QuizDao,
    private val playlistTrackDao: PlaylistTrackDAO,
    private val apiService: DeezerApiInterface
) : ViewModel() {

    private val _currentTrack = MutableLiveData<Track?>()
    val currentTrack: LiveData<Track?> get() = _currentTrack

    private val _options = MutableLiveData<List<String>>()
    val options: LiveData<List<String>> get() = _options

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private val _quizFinished = MutableLiveData<Boolean>()
    val quizFinished: LiveData<Boolean> get() = _quizFinished

    private lateinit var tracks: List<Track>
    private var currentIndex = 0
    private var correctAnswer: String = ""

    init {
        _score.value = 0
        _quizFinished.value = false
    }

    fun fetchQuizDetails(quizId: Long) {
        viewModelScope.launch {
            val quiz = fetchQuizById(quizId)
            val trackIds = fetchTrackIdsForQuiz(quiz)
            fetchTracks(trackIds)
        }
    }

    private suspend fun fetchQuizById(quizId: Long): Quiz {
        return withContext(Dispatchers.IO) {
            quizDao.getQuizById(quizId) ?: throw IllegalArgumentException("Quiz not found")
        }
    }

    private suspend fun fetchTrackIdsForQuiz(quiz: Quiz): List<Long> {
        return withContext(Dispatchers.IO) {
            playlistTrackDao.getTracksForPlaylist(quiz.playlistId).map { it.trackId }
        }
    }

    private fun fetchTracks(trackIds: List<Long>) {
        val fetchedTracks = mutableListOf<Track>()

        trackIds.forEach { id ->
            apiService.getTrackById(id).enqueue(object : retrofit2.Callback<Track> {
                override fun onResponse(
                    call: retrofit2.Call<Track>,
                    response: retrofit2.Response<Track>
                ) {
                    response.body()?.let { fetchedTracks.add(it) }
                    if (fetchedTracks.size == trackIds.size) {
                        tracks = fetchedTracks.shuffled()
                        setupQuestion()
                    }
                }

                override fun onFailure(call: retrofit2.Call<Track>, t: Throwable) {

                }
            })
        }
    }

    private fun setupQuestion() {
        if (currentIndex >= tracks.size) {
            _quizFinished.postValue(true)
            return
        }

        val track = tracks[currentIndex]
        correctAnswer = track.title
        _currentTrack.postValue(track)

        val incorrectAnswers = tracks
            .filter { it.title != correctAnswer }
            .map { it.title }
            .shuffled()

        val options = mutableListOf(correctAnswer).apply {
            addAll(incorrectAnswers.take(2))
            shuffle()
        }

        _options.postValue(options)
    }

    fun selectAnswer(selectedAnswer: String) {
        if (selectedAnswer == correctAnswer) {
            _score.value = _score.value?.plus(1)
        }
        currentIndex++
        setupQuestion()
    }

    fun skipQuestion() {
        currentIndex++
        setupQuestion()
    }
}