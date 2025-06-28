package com.example.kotlin_250_project.ui
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.EducationalInfoBinding // Auto-generated binding class

class EducationInfoActivity : AppCompatActivity() {

    // Declare binding variable
    private lateinit var binding: EducationalInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the binding
        binding = EducationalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
       binding.backArrow.setOnClickListener {
           finish()
       }
        // Set click listeners using binding
        binding.cardProgress.setOnClickListener {
            // TODO: Replace with actual activity
             startActivity(Intent(this, ProgressActivity::class.java))
        }

        binding.cardReport.setOnClickListener {
             startActivity(Intent(this, ReportCardActivity::class.java))
        }

        binding.performancegraph.setOnClickListener {
             startActivity(Intent(this, PerformanceGraphActivity::class.java))
        }

        binding.cardAi.setOnClickListener {
            startActivity(Intent(this, AiChatActivity::class.java))
        }

        binding.cardAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementActivity::class.java))
        }

        binding.cardNotes.setOnClickListener {
             startActivity(Intent(this, NotesActivity::class.java))
        }
    }
}
