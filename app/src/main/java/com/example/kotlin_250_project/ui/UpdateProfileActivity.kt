package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.UpdateProfileActivityBinding

class UpdateProfileActivity : AppCompatActivity() {

    // Declare ViewBinding instance
    private lateinit var binding: UpdateProfileActivityBinding

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

            // Validate input fields
            if (fullName.isEmpty() || guardian.isEmpty() || email.isEmpty() || institute.isEmpty() ||
                gender.isEmpty() || sscBoard.isEmpty() || sscYear.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                updateProfile(fullName, guardian, email, institute, gender, sscBoard, sscYear)
            }
        }
    }

    // Function to update the profile
    private fun updateProfile(
        fullName: String, guardian: String, email: String, institute: String, gender: String,
        sscBoard: String, sscYear: String
    ) {
        // Here, you can update the profile in your database or API
        // For now, we just show a success message
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // You can also redirect the user to another activity if needed
        // For example:
        // startActivity(Intent(this, SomeOtherActivity::class.java))
        // finish()
    }
}
