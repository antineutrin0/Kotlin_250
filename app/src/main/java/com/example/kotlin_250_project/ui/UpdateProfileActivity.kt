package com.example.kotlin_250_project.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.EditProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class UpdateProfileActivity : AppCompatActivity() {

    // Declare ViewBinding and Firestore instance
    private lateinit var binding: EditProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set OnClickListener for the Update button
        binding.btnUpdate.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val number = binding.etnumber.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val institute = binding.etInstitute.text.toString().trim()
            val profileUrl = binding.etprofileUrl.text.toString().trim()


            // Get selected gender from RadioGroup
            val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
            val gender = when (selectedGenderId) {
                R.id.rbMale -> "Male"
                R.id.rbFemale -> "Female"
                else -> ""
            }

            val sscBoard = binding.etSSCBoard.text.toString().trim()
            val sscYear = binding.etSSCYear.text.toString().trim()
            val hscBoard = binding.etHSCBoard.text.toString().trim()
            val hscYear = binding.etHSCYear.text.toString().trim()

            // Validate input fields
            if (fullName.isEmpty() || number.isEmpty() || email.isEmpty() || institute.isEmpty() ||
                gender.isEmpty() || sscBoard.isEmpty() || sscYear.isEmpty() || hscBoard.isEmpty() || hscYear.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Call function to update profile in Firestore
                updateProfile(fullName, number, email, institute, profileUrl, gender, sscBoard, sscYear, hscBoard, hscYear)
            }
        }
        binding.btnBack.setOnClickListener {
            finish() // Finishes current activity and returns to previous one
        }
    }

    // Function to update the profile in Firebase Firestore
    private fun updateProfile(
        fullName: String, number: String, email: String, institute: String, profileUrl: String, gender: String,
        sscBoard: String, sscYear: String, hscBoard: String, hscYear: String
    ) {
        // Create a HashMap for user data
        val userProfile = hashMapOf(
            "fullName" to fullName,
            "number" to number,
            "email" to email,
            "institute" to institute,
            "profileUrl" to profileUrl,
            "gender" to gender,
            "sscBoard" to sscBoard,
            "sscYear" to sscYear,
            "hscBoard" to hscBoard,
            "hscYear" to hscYear
        )

        val userId = auth.currentUser!!.uid
        // Store data in Firestore (Collection: "Users", Document ID: userId)
        firestore.collection("Users").document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
