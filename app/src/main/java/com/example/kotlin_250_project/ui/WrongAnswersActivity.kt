package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class WrongAnswersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wrongAnswersAdapter: WrongAnswersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrong_answers)

        recyclerView = findViewById(R.id.rv_wrong_questions)

        // Get wrong answers map from Intent
        val wrongAnswersJson = intent.getStringExtra("wrongAnswersMap")
        val gson = Gson()
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        val userSelectedMap: Map<String, String> = gson.fromJson(wrongAnswersJson, mapType)

        val questions = loadQuestionsFromJson()
        val wrongQuestions = questions.filter { question ->
            val userAnswer = userSelectedMap[question.id]
            userAnswer != null && userAnswer != question.correctAnswer
        }

        wrongAnswersAdapter = WrongAnswersAdapter(wrongQuestions, userSelectedMap)
        recyclerView.adapter = wrongAnswersAdapter
    }

    private fun loadQuestionsFromJson(): List<Question> {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Question>>() {}.type
        val questions: List<Question> = Gson().fromJson(reader, type)
        reader.close()
        return questions
    }
}
