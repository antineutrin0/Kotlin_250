package com.example.kotlin_250_project.ui

data class Question(
    val id: String,
    val subject: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)
