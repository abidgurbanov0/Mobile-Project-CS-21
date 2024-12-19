package com.example.muricerr.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.muricerr.R
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.factory.QuizDetailsViewModelFactory
import com.example.muricerr.viewmodel.QuizDetailsViewModel

class QuizDetails : Fragment() {

    private lateinit var quizNameTextView: TextView
    private lateinit var playlistNameTextView: TextView
    private lateinit var startMCQ: Button
    private lateinit var startOpen: Button
    private val viewModel: QuizDetailsViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext().applicationContext)
        QuizDetailsViewModelFactory(db.quizDao(), db.playlistDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_quiz_details, container, false)

        quizNameTextView = view.findViewById(R.id.quizNameTextView)
        playlistNameTextView = view.findViewById(R.id.playlistNameTextView)
        startMCQ = view.findViewById(R.id.start_mcq)
        startOpen = view.findViewById(R.id.start_open)

        val quizId = arguments?.getLong("QUIZ_ID") ?: return view

        setupObservers()
        viewModel.fetchQuizDetails(quizId)

        startMCQ.setOnClickListener {
            navigateToQuizMCQFragment(quizId)
        }

        startOpen.setOnClickListener {
            navigateToQuizOpenFragment(quizId)
        }

        return view
    }

    private fun setupObservers() {
        viewModel.quizDetails.observe(viewLifecycleOwner, Observer { quiz ->
            quizNameTextView.text = quiz.name
        })

        viewModel.playlistDetails.observe(viewLifecycleOwner, Observer { playlist ->
            playlistNameTextView.text = playlist?.name ?: "Unknown Playlist"
        })
    }

    private fun navigateToQuizMCQFragment(quizId: Long) {
        val fragment = Quiz().apply {
            arguments = Bundle().apply {
                putLong("QUIZ_ID", quizId)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToQuizOpenFragment(quizId: Long) {
        val fragment = OpenQuiz().apply {
            arguments = Bundle().apply {
                putLong("QUIZ_ID", quizId)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}