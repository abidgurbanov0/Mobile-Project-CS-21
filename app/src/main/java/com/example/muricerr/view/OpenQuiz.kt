package com.example.muricerr.view

import android.media.MediaPlayer
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.muricerr.R
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.factory.QuizOpenViewModelFactory
import com.example.muricerr.viewmodel.OpenQuizViewModel

class OpenQuiz : Fragment() {

    private val viewModel: OpenQuizViewModel by viewModels {
        QuizOpenViewModelFactory(AppDatabase.getDatabase(requireContext()))
    }

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var skipButton: Button
    private lateinit var trackPreviewLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_open_quiz, container, false)

        answerEditText = view.findViewById(R.id.answerEditText)
        answerEditText.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        submitButton = view.findViewById(R.id.submitButton)
        skipButton = view.findViewById(R.id.skipButton)
        trackPreviewLabel = view.findViewById(R.id.trackPreviewLabel)

        val quizId = arguments?.getLong("QUIZ_ID") ?: return view

        submitButton.setOnClickListener {
            val answer = answerEditText.text.toString().trim()
            if (answer.isNotEmpty()) {
                viewModel.submitAnswer(answer)
                answerEditText.text = null
            } else {
                Toast.makeText(requireContext(), "Please enter an answer", Toast.LENGTH_SHORT).show()
            }
        }

        skipButton.setOnClickListener {
            viewModel.skipTrack()
        }

        viewModel.loadQuizData(quizId)

        viewModel.currentTrack.observe(viewLifecycleOwner, Observer { track ->
            if (track == null) {
                Toast.makeText(requireContext(), "Quiz Finished! Score: ${viewModel.score.value}", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
                return@Observer
            }
            playTrack(track.preview)
            trackPreviewLabel.text = "Which music is playing now?"
        })

        return view
    }

    private fun playTrack(previewUrl: String) {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(previewUrl)
                prepareAsync()
                setOnPreparedListener { start() }
                setOnErrorListener { _, _, _ ->
                    Toast.makeText(requireContext(), "Error playing track", Toast.LENGTH_SHORT).show()
                    true
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error loading track", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
