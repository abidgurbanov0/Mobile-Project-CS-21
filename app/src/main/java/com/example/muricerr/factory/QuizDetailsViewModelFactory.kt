package com.example.muricerr.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muricerr.database.PlaylistDAO
import com.example.muricerr.database.QuizDao
import com.example.muricerr.viewmodel.QuizDetailsViewModel


class QuizDetailsViewModelFactory(
    private val quizDao: QuizDao,
    private val playlistDao: PlaylistDAO
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizDetailsViewModel::class.java)) {
            return QuizDetailsViewModel(quizDao, playlistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}