package com.example.kotlin_250_project.ui

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.databinding.FragmentQuizBinding
import java.io.InputStreamReader

class QuizFragment : AppCompatActivity() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var questions: List<Question>
    private lateinit var adapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadQuestionsFromJson()
        setupRecyclerView()
        setupSubmitButton()
    }

    private fun loadQuestionsFromJson() {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val questionListType = object : TypeToken<List<Question>>() {}.type
        questions = Gson().fromJson(reader, questionListType)
        reader.close()
    }

    private fun setupRecyclerView() {
        adapter = QuestionAdapter(questions)
        binding.rvQuestions.layoutManager = LinearLayoutManager(this)
        binding.rvQuestions.adapter = adapter
    }

    private fun setupSubmitButton() {
        binding.btnSubmitAll.setOnClickListener {
            val selectedAnswers = adapter.selectedAnswers

            if (selectedAnswers.size < questions.size) {
                android.widget.Toast.makeText(
                    this,
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
                this,
                "You scored $score out of ${questions.size}",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }
}
