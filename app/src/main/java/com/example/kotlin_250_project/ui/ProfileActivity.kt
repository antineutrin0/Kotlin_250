package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.ProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loadUserProfile()
        setupClickListeners()
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("fullName") ?: "N/A"
                    val email = document.getString("email") ?: "N/A"
                    val number = document.getString("number") ?: "N/A"
                    val profileImageUrl = document.getString("profileUrl")
                    val institute=document.getString("institute")?:"N/A"
                    // Set data to views
                    binding.userName.text = name
                    binding.userEmail.text = "Email : $email"
                    binding.etinstitute.text=" $institute"
                    binding.number.text=" $number"
                    // Load profile image (Optional if you store profile image URL)
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(profileImageUrl)
                            .into(binding.profileImage)
                    }
                } else {
                    Toast.makeText(this, "Profile data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupClickListeners() {
        binding.logoutLayout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.educationInfoLayout.setOnClickListener {
            Toast.makeText(this, "Education Info Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Open Education Info Activity
        }

        binding.myresult.setOnClickListener {
            Toast.makeText(this, "My Result Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Open Result Activity
        }

        binding.Mycourses.setOnClickListener {
            Toast.makeText(this, "My Courses Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Open My Courses Activity
        }
        binding.editIcon.setOnClickListener{
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }
        binding.notificationIcon.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)

        }

    }
}
