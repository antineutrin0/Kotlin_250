package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.ResultShowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ShowResultActivity : AppCompatActivity() {

    private lateinit var binding: ResultShowBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var wrongQuestions: List<Question>
    private lateinit var selectedAnswers: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subject = intent.getStringExtra("subject") ?: "Unknown"
        val totalMarks = intent.getIntExtra("totalMarks", 0)
        val obtainedMarks = intent.getIntExtra("obtainedMarks", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val wrongAnswers = totalQuestions - correctAnswers
        val percentage = if (totalMarks != 0) (obtainedMarks * 100) / totalMarks else 0
        val wrongAnswersListJson = intent.getStringExtra("wrongQuestions")
        val selectedAnswersJson = intent.getStringExtra("selectedAnswers")
        saveResultToFirestore(subject, totalMarks, obtainedMarks, percentage)
        wrongQuestions = Gson().fromJson(wrongAnswersListJson, Array<Question>::class.java).toList()
        selectedAnswers = Gson().fromJson(selectedAnswersJson, Map::class.java) as Map<String, String>



        // UI update
        binding.examTitle.text = subject
        binding.totalQuestions.text = totalQuestions.toString()
        binding.totalMarks.text = totalMarks.toString()
        binding.obtainedMarks.text = obtainedMarks.toString()
        binding.percentageText.text = "$percentage%"
        binding.correctAnswersCount.text = correctAnswers.toString()
        binding.wrongAnswersCount.text = wrongAnswers.toString()

        // Back button
        binding.bButton.setOnClickListener {
            finish()
        }

        // Review Answers button
        binding.reviewAnswersBtn.setOnClickListener {
            val wrongQuestionsJson = intent.getStringExtra("wrongQuestions")
            val selectedAnswersJson = intent.getStringExtra("selectedAnswers")

            if (wrongQuestionsJson != null && selectedAnswersJson != null) {
                val intent = Intent(this, WrongAnswersActivity::class.java)
                intent.putExtra("wrongQuestions", wrongQuestionsJson)
                intent.putExtra("wrongAnswersMap", selectedAnswersJson)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No review data available", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveResultToFirestore(subject: String, totalMarks: Int, obtainedMarks: Int, percentage: Int) {
        val userId = auth.currentUser?.uid ?: return
        val timestamp = System.currentTimeMillis()

        val resultData = mapOf(
            "subject" to subject,
            "totalMarks" to totalMarks,
            "obtainedMarks" to obtainedMarks,
            "timestamp" to timestamp
        )

        // Save to Results
        firestore.collection("Users")
            .document(userId)
            .collection("Results")
            .add(resultData)
            .addOnSuccessListener {

                // If percentage >= 80, also save to Achievements
                if (percentage >= 80) {
                    saveAchievement(userId, subject, obtainedMarks, totalMarks, timestamp)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save result", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAchievement(userId: String, subject: String, obtainedMarks: Int, totalMarks: Int, timestamp: Long) {
        val achievementData = mapOf(
            "subject" to subject,
            "obtainedMarks" to obtainedMarks,
            "totalMarks" to totalMarks,
            "timestamp" to timestamp,
            "message" to "Scored $obtainedMarks out of $totalMarks in $subject! 🎉"
        )

        firestore.collection("Users")
            .document(userId)
            .collection("Achievements")
            .add(achievementData)
            .addOnSuccessListener {
                Toast.makeText(this, "🎉 Achievement unlocked!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save achievement", Toast.LENGTH_SHORT).show()
            }
    }
}
