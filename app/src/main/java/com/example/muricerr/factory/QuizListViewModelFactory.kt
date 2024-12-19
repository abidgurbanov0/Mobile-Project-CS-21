package com.example.muricerr.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muricerr.repo.QuizRepository
import com.example.muricerr.viewmodel.AllQuizViewModel


class QuizListViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllQuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllQuizViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}