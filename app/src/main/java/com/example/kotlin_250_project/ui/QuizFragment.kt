package com.example.kotlin_250_project.ui

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.databinding.FragmentQuizBinding
import java.io.InputStreamReader

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var questions: List<Question>
    private lateinit var adapter: QuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)

        loadQuestionsFromJson()
        setupRecyclerView()
        setupSubmitButton()

        return binding.root
    }

    private fun loadQuestionsFromJson() {
        val inputStream = requireContext().assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val questionListType = object : TypeToken<List<Question>>() {}.type
        questions = Gson().fromJson(reader, questionListType)
        reader.close()
    }

    private fun setupRecyclerView() {
        adapter = QuestionAdapter(questions)
        binding.rvQuestions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuestions.adapter = adapter
    }

    private fun setupSubmitButton() {
        binding.btnSubmitAll.setOnClickListener {
            val selectedAnswers = adapter.selectedAnswers

            if (selectedAnswers.size < questions.size) {
                android.widget.Toast.makeText(
                    requireContext(),
                    "Please answer all questions",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var score = 0
            for (question in questions) {
                val selected = selectedAnswers[question.id]
                if (selected == question.correctAnswer) {
                    score++
                }
            }

            android.widget.Toast.makeText(
                requireContext(),
                "You scored $score out of ${questions.size}",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
