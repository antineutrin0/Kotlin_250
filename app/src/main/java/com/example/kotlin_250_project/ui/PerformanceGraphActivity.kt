package com.example.kotlin_250_project.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class PerformanceGraphActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var tabDay: TextView
    private lateinit var tabWeek: TextView
    private lateinit var tabMonth: TextView
    private lateinit var performanceSummary: TextView
    private lateinit var backbtn: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_graph)

        // Initialize views
        backbtn=findViewById(R.id.backarrow)
        lineChart = findViewById(R.id.performanceChart)
        tabDay = findViewById(R.id.tab_day)
        tabWeek = findViewById(R.id.tab_week)
        tabMonth = findViewById(R.id.tab_month)
        performanceSummary = findViewById(R.id.performance_summary)

        setupChart()
        loadWeeklyData()

        // Tab click listeners
        tabDay.setOnClickListener {
            activateTab(tabDay)
            deactivateTabs(tabWeek, tabMonth)
            loadDailyData()
        }
        backbtn.setOnClickListener{
            finish();
        }

        tabWeek.setOnClickListener {
            activateTab(tabWeek)
            deactivateTabs(tabDay, tabMonth)
            loadWeeklyData()
        }

        tabMonth.setOnClickListener {
            activateTab(tabMonth)
            deactivateTabs(tabDay, tabWeek)
            loadMonthlyData()
        }
    }

    private fun setupChart() {
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun loadDailyData() {
        val entries = listOf(
            Entry(1f, 60f),
            Entry(2f, 65f),
            Entry(3f, 63f),
            Entry(4f, 68f),
            Entry(5f, 70f)
        )
        updateChart(entries, "Today's Progress", Color.BLUE)
        performanceSummary.text = "Today: +5% improvement 🚀"
    }

    private fun loadWeeklyData() {
        val entries = listOf(
            Entry(1f, 50f),
            Entry(2f, 55f),
            Entry(3f, 60f),
            Entry(4f, 70f),
            Entry(5f, 75f),
            Entry(6f, 78f),
            Entry(7f, 80f)
        )
        updateChart(entries, "Weekly Progress", Color.GREEN)
        performanceSummary.text = "This week: +12% improvement 🎯"
    }

    private fun loadMonthlyData() {
        val entries = listOf(
            Entry(1f, 40f),
            Entry(5f, 50f),
            Entry(10f, 60f),
            Entry(15f, 70f),
            Entry(20f, 75f),
            Entry(25f, 80f),
            Entry(30f, 85f)
        )
        updateChart(entries, "Monthly Progress", Color.MAGENTA)
        performanceSummary.text = "This month: +20% improvement 📈"
    }

    private fun updateChart(entries: List<Entry>, label: String, color: Int) {
        val dataSet = LineDataSet(entries, label)
        dataSet.color = color
        dataSet.valueTextColor = Color.BLACK
        dataSet.setCircleColor(color)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 50

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.invalidate()
    }

    private fun activateTab(tab: TextView) {
        tab.setBackgroundResource(R.drawable.tab_active_background)
        tab.setTextColor(Color.WHITE)
    }

    private fun deactivateTabs(vararg tabs: TextView) {
        for (tab in tabs) {
            tab.setBackgroundResource(R.drawable.tab_inactive_background)
            tab.setTextColor(resources.getColor(R.color.tab_inactive_text, theme))
        }
    }
}
