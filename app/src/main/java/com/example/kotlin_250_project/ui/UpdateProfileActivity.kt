package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.UpdateProfileActivityBinding
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileActivity : AppCompatActivity() {

    // Declare ViewBinding and Firestore instance
    private lateinit var binding: UpdateProfileActivityBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = UpdateProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set OnClickListener for the Update button
        binding.btnUpdate.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val guardian = binding.etGuardian.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val institute = binding.etInstitute.text.toString().trim()
            val gender = binding.etGender.text.toString().trim()
            val sscBoard = binding.etSSCBoard.text.toString().trim()
            val sscYear = binding.etSSCYear.text.toString().trim()
            val hscBoard = binding.etHSCBoard.text.toString().trim()
            val hscYear = binding.etHSCYear.text.toString().trim()

            // Validate input fields
            if (fullName.isEmpty() || guardian.isEmpty() || email.isEmpty() || institute.isEmpty() ||
                gender.isEmpty() || sscBoard.isEmpty() || sscYear.isEmpty() || hscBoard.isEmpty() || hscYear.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Call function to update profile in Firestore
                updateProfile(fullName, guardian, email, institute, gender, sscBoard, sscYear, hscBoard, hscYear)
            }
        }
    }

    // Function to update the profile in Firebase Firestore
    private fun updateProfile(
        fullName: String, guardian: String, email: String, institute: String, gender: String,
        sscBoard: String, sscYear: String, hscBoard: String, hscYear: String
    ) {
        // Create a HashMap for user data
        val userProfile = hashMapOf(
            "fullName" to fullName,
            "guardian" to guardian,
            "email" to email,
            "institute" to institute,
            "gender" to gender,
            "sscBoard" to sscBoard,
            "sscYear" to sscYear,
            "hscBoard" to hscBoard,
            "hscYear" to hscYear
        )

        // Store data in Firestore (Collection: "Users", Document ID: user email)
        firestore.collection("Users").document(email)
            .set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                // Navigate to Home Page
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
