package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.ProgressCardBinding

class ProgressActivity : AppCompatActivity() {

    private lateinit var binding: ProgressCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProgressCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button navigation
        binding.backarrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
