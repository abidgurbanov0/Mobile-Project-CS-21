package com.example.muricerr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muricerr.R
import com.example.muricerr.model.Quiz

class QuizAdapter(
    private var quizzes: List<Quiz>,  // Make quizzes mutable so it can be updated
    private val onQuizClick: (Quiz) -> Unit,
    private val onRemoveClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quizNameTextView: TextView = itemView.findViewById(R.id.quizNameTextView)
        val removeButton: ImageButton = itemView.findViewById(R.id.delete_button)

        fun bind(quiz: Quiz) {
            quizNameTextView.text = quiz.name
            itemView.setOnClickListener { onQuizClick(quiz) }
            removeButton.setOnClickListener { onRemoveClick(quiz) }
        }
    }

    // This method updates the list of quizzes and notifies the adapter of the change
    fun updateQuizzes(newQuizzes: List<Quiz>) {
        this.quizzes = newQuizzes
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    override fun getItemCount(): Int = quizzes.size
}
