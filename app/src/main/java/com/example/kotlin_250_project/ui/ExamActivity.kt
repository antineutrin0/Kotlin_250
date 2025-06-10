package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.ExamActivityBinding

class ExamActivity : AppCompatActivity() {

    private lateinit var binding: ExamActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ExamActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
