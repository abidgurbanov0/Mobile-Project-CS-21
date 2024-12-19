package com.example.muricerr.view

import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.muricerr.R
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.factory.QuizViewModelFactory
import com.example.muricerr.network.DeezerApiInstance
import com.example.muricerr.viewmodel.QuizViewModel

class Quiz : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var choiceButtonA: Button
    private lateinit var choiceButtonB: Button
    private lateinit var choiceButtonC: Button
    private lateinit var choiceButtonD: Button
    private lateinit var trackPreviewLabel: TextView

    private val viewModel: QuizViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext().applicationContext)
        QuizViewModelFactory(db.quizDao(), db.playlistTrackDao(), DeezerApiInstance.api)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        choiceButtonA = view.findViewById(R.id.choiceButtonA)
        choiceButtonB = view.findViewById(R.id.choiceButtonB)
        choiceButtonC = view.findViewById(R.id.choiceButtonC)
        choiceButtonD = view.findViewById(R.id.choiceButtonD)
        trackPreviewLabel = view.findViewById(R.id.trackPreviewLabel)

        choiceButtonA.setOnClickListener { onAnswerSelected(choiceButtonA.text.toString()) }
        choiceButtonB.setOnClickListener { onAnswerSelected(choiceButtonB.text.toString()) }
        choiceButtonC.setOnClickListener { onAnswerSelected(choiceButtonC.text.toString()) }
        choiceButtonD.setOnClickListener { viewModel.skipQuestion() }

        val quizId = arguments?.getLong("QUIZ_ID") ?: return view
        viewModel.fetchQuizDetails(quizId)

        setupObservers()
        return view
    }

    private fun setupObservers() {
        viewModel.currentTrack.observe(viewLifecycleOwner) { track ->
            track?.let {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(it.preview)
                    prepareAsync()
                    setOnPreparedListener { start() }
                }
                trackPreviewLabel.text = "Which song is playing now?"
            }
        }

        viewModel.options.observe(viewLifecycleOwner) { options ->
            choiceButtonA.text = options[0]
            choiceButtonB.text = options[1]
            choiceButtonC.text = options[2]
            choiceButtonD.text = "Skip"
        }

        viewModel.score.observe(viewLifecycleOwner) { score ->
            // Update score if necessary
        }

        viewModel.quizFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                Toast.makeText(requireContext(), "Quiz Finished! Score: ${viewModel.score.value}", Toast.LENGTH_SHORT).show()
                val quizListFragment = AllQuiz()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_layout, quizListFragment)
                    .commit()
            }
        }
    }

    private fun onAnswerSelected(selectedAnswer: String) {
        viewModel.selectAnswer(selectedAnswer)
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
