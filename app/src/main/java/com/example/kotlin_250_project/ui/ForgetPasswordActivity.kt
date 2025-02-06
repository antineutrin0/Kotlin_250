package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.auth.FireBaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var authManager: FireBaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        authManager = FireBaseAuth(this)
    }
}