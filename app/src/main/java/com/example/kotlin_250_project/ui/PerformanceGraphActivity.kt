package com.example.kotlin_250_project.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class PerformanceGraphActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var summaryText: TextView

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_graph)

        lineChart = findViewById(R.id.performanceChart)
        summaryText = findViewById(R.id.performance_summary)
        findViewById<ImageView>(R.id.backarrow).setOnClickListener {
            finish()
        }

        fetchPerformanceData(7)
    }

    private fun fetchPerformanceData(days: Int) {
        val userId = auth.currentUser?.uid ?: return
        val sinceTimestamp = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000L

        firestore.collection("Users").document(userId)
            .collection("Results")
            .whereGreaterThan("timestamp", sinceTimestamp)
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val results = result.documents.mapNotNull { it.toObject(Result::class.java) }

                if (results.isEmpty()) {
                    summaryText.text = "No performance data found."
                    lineChart.clear()
                    return@addOnSuccessListener
                }

                val entries = mutableListOf<Entry>()
                val labels = mutableListOf<String>()

                results.forEachIndexed { index, res ->
                    val percent = if (res.totalMarks > 0)
                        res.obtainedMarks * 100f / res.totalMarks
                    else 0f
                    entries.add(Entry(index.toFloat(), percent))
                    labels.add(dateFormatter.format(Date(res.timestamp)))
                }

                drawLineChart(entries, labels)

                val improvement = (entries.last().y - entries.first().y).toInt()
                val trend = if (improvement >= 0) "+$improvement%" else "$improvement%"
                summaryText.text = "Change: $trend performance"
            }
            .addOnFailureListener {
                summaryText.text = "Failed to load performance data."
            }
    }

    private fun drawLineChart(entries: List<Entry>, labels: List<String>) {
        val dataSet = LineDataSet(entries, "Exam Scores").apply {
            color = Color.BLUE
            circleRadius = 5f
            setCircleColor(Color.RED)
            lineWidth = 2f
            valueTextSize = 10f
            setDrawFilled(true)
            fillAlpha = 100
            fillColor = Color.CYAN
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(true)
        lineChart.description.text = ""
        lineChart.invalidate()
    }

    data class Result(
        val subject: String = "",
        val obtainedMarks: Int = 0,
        val totalMarks: Int = 0,
        val timestamp: Long = 0
    )
}
