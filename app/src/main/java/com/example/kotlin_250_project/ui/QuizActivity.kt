package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var adapter: QuestionAdapter
    private lateinit var filteredQuestions: List<Question>
    private val bookmarkedQuestionIds = mutableListOf<String>()

    private val userId: String by lazy {
        FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private var countDownTimer: CountDownTimer? = null
    private var totalTimeInMillis: Long = 0L // total time based on marks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchUserBookmarks {
            setupQuiz()
        }
    }

    private fun fetchUserBookmarks(onReady: () -> Unit) {
        FirebaseFirestore.getInstance().collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val bookmarks = document["bookmarks"] as? List<String>
                if (bookmarks != null) {
                    bookmarkedQuestionIds.addAll(bookmarks)
                }
                onReady()
            }
            .addOnFailureListener {
                onReady()
            }
    }

    private fun setupQuiz() {
        val subject = intent.getStringExtra("subject") ?: "All"
        val marks = intent.getIntExtra("marks", 10)

        val allQuestions = loadQuestionsFromJson()
        filteredQuestions = filterAndDistributeQuestions(allQuestions, subject, marks)

        adapter = QuestionAdapter(
            questions = filteredQuestions.toMutableList(),
            bookmarkedIds = bookmarkedQuestionIds.toMutableList(),
            onBookmarkToggle = { question, isBookmarked ->
                updateBookmark(question.id, isBookmarked)
            },
            showCorrectAnswer = false
        )

        binding.rvQuestions.layoutManager = LinearLayoutManager(this)
        binding.rvQuestions.adapter = adapter

        setupTimer(marks)
        setupSubmitButton()
    }

    private fun setupTimer(marks: Int) {
        // Each mark = 1 minute
        totalTimeInMillis = marks * 60 * 1000L

        countDownTimer?.cancel() // cancel any existing timer
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                Toast.makeText(this@QuizActivity, "Time is up! Submitting automatically.", Toast.LENGTH_LONG).show()
                submitQuiz()
            }
        }.start()
    }

    private fun submitQuiz() {
        val subject = intent.getStringExtra("subject") ?: "All"
        val marks = intent.getIntExtra("marks", 10)
        val selectedAnswers = adapter.selectedAnswers
        var score = 0

        for (question in filteredQuestions) {
            val selected = selectedAnswers[question.id]
            if (selected != null && selected == question.correctAnswer) {
                score++
            }
        }

        //  Build list of wrong questions
        val wrongQuestions = filteredQuestions.filter { question ->
            val selected = selectedAnswers[question.id]
            selected == null || selected != question.correctAnswer
        }

        val intent = Intent(this, ShowResultActivity::class.java)
        intent.putExtra("subject", subject)
        intent.putExtra("totalMarks", marks)
        intent.putExtra("obtainedMarks", score)
        intent.putExtra("totalQuestions", filteredQuestions.size)
        intent.putExtra("correctAnswers", score)
        intent.putExtra("wrongQuestions", Gson().toJson(wrongQuestions))
        intent.putExtra("selectedAnswers", Gson().toJson(selectedAnswers))
        startActivity(intent)
    }


    private fun setupSubmitButton() {
        binding.btnSubmitAll.setOnClickListener {
            val selectedAnswers = adapter.selectedAnswers

            if (selectedAnswers.size < filteredQuestions.size) {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            countDownTimer?.cancel() // stop timer when user submits manually
            submitQuiz()
        }
    }

    private fun loadQuestionsFromJson(): List<Question> {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Question>>() {}.type
        val questions: List<Question> = Gson().fromJson(reader, type)
        reader.close()
        return questions
    }

    private fun filterAndDistributeQuestions(
        allQuestions: List<Question>,
        subject: String,
        marks: Int
    ): List<Question> {
        return if (!subject.equals("All", ignoreCase = true)) {
            val subjectQuestions = allQuestions.filter {
                it.subject.equals(subject, ignoreCase = true)
            }
            subjectQuestions.shuffled().take(marks)
        } else {
            val grouped = allQuestions.groupBy { it.subject }
            val totalSubjects = grouped.size
            val perSubject = marks / totalSubjects
            val remainder = marks % totalSubjects

            val selected = mutableListOf<Question>()
            var extra = 0

            for ((_, subjectQuestions) in grouped) {
                val count = perSubject + if (extra < remainder) 1 else 0
                extra++
                selected += subjectQuestions.shuffled().take(count)
            }

            selected.shuffled()
        }
    }

    private fun updateBookmark(questionId: String, add: Boolean) {
        val userRef = FirebaseFirestore.getInstance().collection("Users").document(userId)

        val update = if (add) {
            mapOf("bookmarks" to FieldValue.arrayUnion(questionId))
        } else {
            mapOf("bookmarks" to FieldValue.arrayRemove(questionId))
        }

        userRef.update(update)
            .addOnSuccessListener {
                // Success
            }
            .addOnFailureListener {
                Toast.makeText(this, "Bookmark update failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
