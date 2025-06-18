package com.example.kotlin_250_project.ui

import ResultModel
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R


class ResultActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)

        recyclerView = findViewById(R.id.recycler_results)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val resultList = listOf(
            ResultModel("Physics", "80 / 100", "A++", "PASS"),
            ResultModel("English", "75 / 100", "A+", "PASS"),
            ResultModel("Bangla", "90 / 100", "A++", "PASS"),
            ResultModel("Chemistry", "75 / 100", "A+", "PASS"),
            ResultModel("Math", "82 / 100", "A+", "PASS"),
            ResultModel("Biology", "75 / 100", "A+", "PASS")
        )

        adapter = ResultAdapter(resultList)
        recyclerView.adapter = adapter

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }
    }
}