package com.example.muricerr.view

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muricerr.R
import com.example.muricerr.adapter.QuizAdapter
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.factory.QuizListViewModelFactory
import com.example.muricerr.model.Quiz
import com.example.muricerr.repo.QuizRepository
import com.example.muricerr.viewmodel.AllQuizViewModel

class AllQuiz : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter

    private val viewModel: AllQuizViewModel by viewModels {
        QuizListViewModelFactory(
            QuizRepository(
                AppDatabase.getDatabase(requireContext()).quizDao(),
                AppDatabase.getDatabase(requireContext()).playlistDao(),
                AppDatabase.getDatabase(requireContext()).playlistTrackDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_all_quiz, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        quizAdapter = QuizAdapter(
            mutableListOf(),
            onQuizClick = { quiz -> navigateToQuizDetails(quiz.id) },
            onRemoveClick = { quiz -> confirmAndDeleteQuiz(quiz) }
        )
        recyclerView.adapter = quizAdapter

        view.findViewById<View>(R.id.fab).setOnClickListener {
            showCreateQuizDialog()
        }

        setupObservers()
        viewModel.fetchQuizzes()

        return view
    }

    private fun setupObservers() {
        viewModel.quizzes.observe(viewLifecycleOwner, Observer { quizzes ->
            quizAdapter.updateQuizzes(quizzes)
        })

        viewModel.playlistsWithSongs.observe(viewLifecycleOwner, Observer { playlists ->
            showCreateQuizDialog(playlists)
        })
    }

    private fun confirmAndDeleteQuiz(quiz: Quiz) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Quiz")
            .setMessage("Are you sure you want to delete the quiz \"${quiz.name}\"?")
            .setPositiveButton("Delete") { _, _ -> viewModel.deleteQuiz(quiz) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToQuizDetails(quizId: Long) {
        val fragment = QuizDetails().apply {
            arguments = Bundle().apply {
                putLong("QUIZ_ID", quizId)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showCreateQuizDialog() {
        viewModel.fetchPlaylistsWithMinSongs(3)
    }

    private fun showCreateQuizDialog(playlists: List<Pair<Long, String>>) {
        if (playlists.isEmpty()) {
            Toast.makeText(requireContext(), "There must be at least 3 songs", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_quiz, null)
        val quizNameInput: EditText = dialogView.findViewById(R.id.quizNameEditText)
        quizNameInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        val playlistSpinner: Spinner = dialogView.findViewById(R.id.playlistSpinner)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, playlists.map { it.second })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        playlistSpinner.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle("Create Quiz")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val quizName = quizNameInput.text.toString().trim()
                val selectedPlaylistId = playlists[playlistSpinner.selectedItemPosition].first

                if (quizName.isNotEmpty()) {
                    viewModel.createQuiz(quizName, selectedPlaylistId)
                } else {
                    Toast.makeText(requireContext(), "Quiz name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
