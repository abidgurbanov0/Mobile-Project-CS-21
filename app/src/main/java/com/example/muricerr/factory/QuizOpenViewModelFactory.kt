package com.example.muricerr.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.viewmodel.OpenQuizViewModel

class QuizOpenViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OpenQuizViewModel::class.java)) {
            return OpenQuizViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}