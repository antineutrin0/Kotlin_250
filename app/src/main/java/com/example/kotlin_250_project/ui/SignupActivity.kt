package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.SignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var mAuth: FirebaseAuth

    // View Binding instance
    private lateinit var binding: SignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Set OnClickListener for Sign Up button
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
            else if(password.length<8)
                Toast.makeText(this, "password must be more then equal to 8 character", Toast.LENGTH_SHORT).show()

            else {
                signUpUser(email, password)
            }
        }
        binding.tvSignIn.setOnClickListener({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })
        binding.btnBack.setOnClickListener({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        })

    }

    // Sign up user using Firebase Authentication
    private fun signUpUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    Toast.makeText(baseContext, "Sign up successful! Welcome ${user?.email}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UpdateProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Sign up failed. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
