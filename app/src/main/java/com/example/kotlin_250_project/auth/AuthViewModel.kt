package com.example.kotlin_250_project.auth

import androidx.lifecycle.ViewModel

class AuthViewModel(private val userRepository: UserRepo) : ViewModel() {
    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        userRepository.registerUser(email, password, onComplete)
    }
}