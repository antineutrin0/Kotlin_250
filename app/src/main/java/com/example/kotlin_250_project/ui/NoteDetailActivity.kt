package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var dateText: TextView
    private lateinit var descriptionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        titleText = findViewById(R.id.tvDetailTitle)
        dateText = findViewById(R.id.tvDetailDate)
        descriptionText = findViewById(R.id.tvDetailDescription)

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val description = intent.getStringExtra("description")

        titleText.text = title
        dateText.text = "Date: $date"
        descriptionText.text = description
    }
}
