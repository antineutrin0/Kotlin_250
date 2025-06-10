package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.databinding.BookMarkBinding

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: BookMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = BookMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
