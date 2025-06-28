package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProgressActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var overallPercentage: TextView
    private lateinit var correctAnswer: TextView
    private lateinit var wrongAnswer: TextView
    private lateinit var avgGrade: TextView
    private lateinit var overallProgressBar: ProgressBar

    private lateinit var mathProgress: ProgressBar
    private lateinit var physicsProgress: ProgressBar
    private lateinit var chemistryProgress: ProgressBar

    private lateinit var mathPercentage: TextView
    private lateinit var physicsPercentage: TextView
    private lateinit var chemistryPercentage: TextView
    private lateinit var btnback:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_card)

        // Initialize views
        overallPercentage = findViewById(R.id.overall_percentage)
        correctAnswer = findViewById(R.id.tv_correct_answers)
        wrongAnswer = findViewById(R.id.tv_wrong_answers)
        avgGrade = findViewById(R.id.tv_grade)
        overallProgressBar = findViewById(R.id.overallProgressBar)
        btnback=findViewById(R.id.backarrow)
        mathProgress = findViewById(R.id.mathPercentage)
        physicsProgress = findViewById(R.id.physicsPercentage)
        chemistryProgress = findViewById(R.id.chemistryPercentage)

        mathPercentage = findViewById(R.id.math_percentagetext)
        physicsPercentage = findViewById(R.id.physics_percentagetext)
        chemistryPercentage = findViewById(R.id.chemistry_percentagetext)

        fetchProgressData()

        btnback.setOnClickListener {
            finish()
        }
    }

    private fun fetchProgressData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("Users").document(userId)
            .collection("Results")
            .orderBy("timestamp")
            .limitToLast(7)
            .get()
            .addOnSuccessListener { result ->
                val results = result.documents.mapNotNull { it.toObject(Result::class.java) }

                if (results.isEmpty()) {
                    Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val totalObtained = results.sumOf { it.obtainedMarks }
                val totalMarks = results.sumOf { it.totalMarks }
                val correct = totalObtained
                val wrong = totalMarks - correct
                val percentage = if (totalMarks > 0) (totalObtained * 100 / totalMarks) else 0

                overallPercentage.text = "$percentage%"
                correctAnswer.text = "$correct"
                wrongAnswer.text = "$wrong"
                avgGrade.text = calculateGrade(percentage)
                overallProgressBar.progress = percentage

                updateSubjectProgress(results)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun calculateGrade(percent: Int): String {
        return when {
            percent >= 90 -> "A+"
            percent >= 80 -> "A"
            percent >= 70 -> "B"
            percent >= 60 -> "C"
            else -> "F"
        }
    }

    private fun updateSubjectProgress(results: List<Result>) {
        val subjectMap: Map<String, Pair<ProgressBar, TextView>> = mapOf(
            "Math" to (mathProgress to mathPercentage),
            "Physics" to (physicsProgress to physicsPercentage),
            "Chemistry" to (chemistryProgress to chemistryPercentage)
        )

        for ((subjectName, views) in subjectMap) {
            val (progressBar, percentageText) = views
            val filtered = results.filter { it.subject.equals(subjectName, ignoreCase = true) }
            val total = filtered.sumOf { it.totalMarks }
            val obtained = filtered.sumOf { it.obtainedMarks }
            val percent = if (total > 0) (obtained * 100 / total) else 0
            progressBar.progress = percent
            percentageText.text = "$percent%"
        }
    }

    data class Result(
        val subject: String = "",
        val obtainedMarks: Int = 0,
        val totalMarks: Int = 0,
        val timestamp: Long = 0
    )
}
