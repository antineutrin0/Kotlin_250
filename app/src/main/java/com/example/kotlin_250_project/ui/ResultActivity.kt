package com.example.kotlin_250_project.ui

import ExamResult
import ResultAdapter
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResultActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val resultsList = mutableListOf<ExamResult>()
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity) // your XML layout file with recycler_results RecyclerView

        recyclerView = findViewById(R.id.activity_results)
        adapter = ResultAdapter(resultsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener { finish() }

        fetchResults()
    }

    private fun fetchResults() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("Users")
            .document(userId)
            .collection("Results")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { snapshot ->
                resultsList.clear()
                for (doc in snapshot.documents) {
                    val result = doc.toObject(ExamResult::class.java)
                    if (result != null) {
                        resultsList.add(result)
                    }
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // handle failure e.g. Toast
            }
    }
}
