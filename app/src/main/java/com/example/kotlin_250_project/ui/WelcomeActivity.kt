package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.ActivityWelcomeBinding
class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Initialize ViewBinding
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Directly navigate to MainActivity for testing phase
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
//        finish()
//        // Set up button click listeners
//        binding.btnLogIn.setOnClickListener {
//            // Navigate to the login activity (if you have it)
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.btnSignUp.setOnClickListener {
//            // Navigate to the sign-up activity (if you have it)
//            val intent = Intent(this, SignupActivity::class.java)
//            startActivity(intent)
//        }
    }
}
