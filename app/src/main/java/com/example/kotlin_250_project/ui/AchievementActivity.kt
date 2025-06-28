package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AchievementActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyAchievementsLayout: LinearLayout

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val achievements = mutableListOf<Achievement>()
    private lateinit var adapter: AchievementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.achievements)

        recyclerView = findViewById(R.id.recyclerAchievements)
        emptyAchievementsLayout = findViewById(R.id.emptyAchievementsLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AchievementAdapter(achievements)
        recyclerView.adapter = adapter

        findViewById<View>(R.id.btnBackAchievements).setOnClickListener {
            finish()
        }

        fetchAchievements()
    }

    private fun fetchAchievements() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        firestore.collection("Users")
            .document(userId)
            .collection("Achievements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                achievements.clear()

                for (doc in result.documents) {
                    try {
                        val item = doc.toObject(Achievement::class.java)
                        if (item != null) {
                            achievements.add(item)
                        }
                    } catch (e: Exception) {
                        Log.e("AchievementActivity", "Error parsing achievement", e)
                    }
                }

                if (achievements.isEmpty()) {
                    emptyAchievementsLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyAchievementsLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load achievements", Toast.LENGTH_SHORT).show()
                Log.e("AchievementActivity", "Firestore error", e)
            }
    }
}
