package com.example.muricerr.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muricerr.model.Quiz


@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuiz(quiz: Quiz): Long

    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): List<Quiz>

    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    fun getQuizById(quizId: Long): Quiz

    @Delete
    fun deleteQuiz(quiz: Quiz)
}
