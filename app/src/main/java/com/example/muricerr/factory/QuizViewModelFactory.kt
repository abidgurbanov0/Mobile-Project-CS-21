package com.example.muricerr.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muricerr.database.PlaylistTrackDAO
import com.example.muricerr.database.QuizDao
import com.example.muricerr.network.DeezerApiInterface
import com.example.muricerr.viewmodel.QuizViewModel

class QuizViewModelFactory(
    private val quizDao: QuizDao,
    private val playlistTrackDao: PlaylistTrackDAO,
    private val apiService: DeezerApiInterface
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(quizDao, playlistTrackDao, apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}