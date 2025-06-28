package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R

class ReportCardActivity : AppCompatActivity() {

    private lateinit var examsListContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reportcard)
        findViewById<ImageView>(R.id.backarrow).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}
