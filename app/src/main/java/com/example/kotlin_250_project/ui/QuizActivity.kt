package com.example.kotlin_250_project.ui

import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.databinding.ActivityQuizBinding
import java.io.InputStreamReader

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var questions: List<Question>
    private lateinit var adapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadQuestionsFromJson()
        setupRecyclerView()
        setupSubmitButton()
    }

    private fun loadQuestionsFromJson() {
        try {
            val inputStream = assets.open("questions.json")
            val reader = InputStreamReader(inputStream)

            val questionListType = object : com.google.gson.reflect.TypeToken<List<Question>>() {}.type
            questions = Gson().fromJson(reader, questionListType)

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading questions: ${e.message}", Toast.LENGTH_LONG).show()
        }
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
                Toast.makeText(
                    this,
                    "Please answer all questions",
                    Toast.LENGTH_SHORT
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

            Toast.makeText(
                this,
                "You scored $score out of ${questions.size}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
