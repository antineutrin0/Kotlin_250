package com.example.kotlin_250_project.auth

import android.content.Context
import android.widget.Toast

class FireBaseAuth(private val context: Context) {
    private val auth: FireBaseAuth = FireBaseAuth.getInstance()

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendVerificationEmail()
                    onComplete(true, "Registration successful. Please verify your email.")
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true) {
                        onComplete(true, "Login successful!")
                    } else {
                        onComplete(false, "Please verify your email before logging in.")
                    }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    private fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(context, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun logoutUser() {
        auth.signOut()
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    fun getCurrentUser(): String? {
        return auth.currentUser?.uid
    }
}
