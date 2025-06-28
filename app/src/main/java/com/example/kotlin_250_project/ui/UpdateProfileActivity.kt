package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.EditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: EditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loadUserData()

        binding.btnUpdate.setOnClickListener {
            updateUserData()
        }

        binding.btnBack.setOnClickListener {
            finish() // go back to profile
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Set basic info
                    binding.etFullName.setText(document.getString("fullName"))
                    binding.etnumber.setText(document.getString("number"))
                    binding.etInstitute.setText(document.getString("institute"))
                    binding.etprofileUrl.setText(document.getString("profileUrl"))

                    // Set gender
                    when (document.getString("gender")) {
                        "Male" -> binding.rbMale.isChecked = true
                        "Female" -> binding.rbFemale.isChecked = true
                    }

                    // Set education info
                    binding.etSSCBoard.setText(document.getString("sscBoard"))
                    binding.etSSCYear.setText(document.getString("sscYear"))
                    binding.etHSCBoard.setText(document.getString("hscBoard"))
                    binding.etHSCYear.setText(document.getString("hscYear"))
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserData() {
        val userId = auth.currentUser?.uid ?: return

        val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
        val selectedGender = if (selectedGenderId != -1) {
            findViewById<RadioButton>(selectedGenderId).text.toString()
        } else {
            ""
        }

        val updatedData = mapOf(
            "fullName" to binding.etFullName.text.toString(),
            "number" to binding.etnumber.text.toString(),
            "institute" to binding.etInstitute.text.toString(),
            "profileUrl" to binding.etprofileUrl.text.toString(),
            "gender" to selectedGender,
            "sscBoard" to binding.etSSCBoard.text.toString(),
            "sscYear" to binding.etSSCYear.text.toString(),
            "hscBoard" to binding.etHSCBoard.text.toString(),
            "hscYear" to binding.etHSCYear.text.toString()
        )

        val userDocRef = firestore.collection("Users").document(userId)

        // First check if document exists
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Update existing document
                    userDocRef.update(updatedData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK) // let the caller know update happened
                            finish()

                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Create new document
                    userDocRef.set(updatedData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeScreenActivity::class.java)
                            // Optional: clear back stack so user can't go back to login
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to create profile: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
