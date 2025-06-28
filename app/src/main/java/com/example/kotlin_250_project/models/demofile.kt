//package com.example.kotlin_250_project.ui
//
//import android.app.AlertDialog
//import android.content.Intent
//import android.os.Bundle
//import android.text.InputType
//import android.util.Log
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.kotlin_250_project.databinding.LoginBinding
//import com.google.firebase.auth.FirebaseAuth
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var binding: LoginBinding
//
//    var ischaracter = 0
//    var i = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = LoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        mAuth = FirebaseAuth.getInstance()
//
//        binding.btnLogin.setOnClickListener {
//            val email = binding.etEmail.text.toString().trim()
//            val password = binding.etPassword.text.toString().trim()
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
//            } else {
//                signInUser(email, password)
//            }
//        }
//
//        binding.tvSignUp.setOnClickListener {
//            startActivity(Intent(this, SignupActivity::class.java))
//            finish()
//        }
//
//        binding.btnBack.setOnClickListener {
//            startActivity(Intent(this, WelcomeActivity::class.java))
//        }
//
//        binding.tvForgotPassword.setOnClickListener {
//            showForgotPasswordDialog()
//        }
//    }
//
//    private fun signInUser(email: String, password: String) {
//        mAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = mAuth.currentUser
//                    Toast.makeText(baseContext, "Welcome ${user?.email}", Toast.LENGTH_SHORT).show()
//
//                    val intent = Intent(this, HomeScreenActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Log.w("SignInActivity", "signInWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    private fun showForgotPasswordDialog() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Reset Password")
//
//        val input = EditText(this)
//        input.hint = "Enter your email"
//        input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
//        builder.setView(input)
//
//        builder.setPositiveButton("Send") { dialog, _ ->
//            val email = input.text.toString().trim()
//            if (email.isNotEmpty()) {
//                sendPasswordResetEmail(email)
//            } else {
//                Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT).show()
//            }
//            dialog.dismiss()
//        }
//
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.cancel()
//        }
//
//        builder.show()
//    }
//
//    private fun sendPasswordResetEmail(email: String) {
//        mAuth.sendPasswordResetEmail(email)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//    }
//}
