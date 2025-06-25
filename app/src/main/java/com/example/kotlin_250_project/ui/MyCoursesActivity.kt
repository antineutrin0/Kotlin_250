package com.example.kotlin_250_project.ui

import Course
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kotlin_250_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MyCoursesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var coursesGrid: GridLayout
    private lateinit var scrollView: ScrollView
    private lateinit var emptyStateLayout: View
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_courses)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        coursesGrid = findViewById(R.id.courses_grid)
        scrollView = findViewById(R.id.courses_scroll_view)
        emptyStateLayout = findViewById(R.id.empty_state_layout)
        backButton = findViewById(R.id.btnBackAchievements)

        backButton.setOnClickListener { finish() }

        loadUserCourses()
    }

    private fun loadUserCourses() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("Users").document(uid).get()
            .addOnSuccessListener { document ->
                val courseIds = document.get("courses") as? List<String> ?: emptyList()

                if (courseIds.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                    scrollView.visibility = View.GONE
                    return@addOnSuccessListener
                }

                emptyStateLayout.visibility = View.GONE
                scrollView.visibility = View.VISIBLE

                firestore.collection("Courses").get()
                    .addOnSuccessListener { result ->
                        coursesGrid.removeAllViews()
                        for (doc in result) {
                            val course = doc.toObject(Course::class.java).copy(id = doc.id)
                            if (courseIds.contains(course.id)) {
                                addCourseToGrid(course)
                            }
                        }
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load courses", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addCourseToGrid(course: Course) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.course_card, coursesGrid, false)

        val title = cardView.findViewById<TextView>(R.id.courseTitle)
        val description = cardView.findViewById<TextView>(R.id.courseDescription)
        val thumbnail = cardView.findViewById<ImageView>(R.id.courseThumbnail)
        val enrollButton = cardView.findViewById<Button>(R.id.enrollButton)

        title.text = course.title
        description.text = course.description
        Glide.with(this).load(course.thumbnail).into(thumbnail)

        enrollButton.visibility = View.GONE

        // ✅ Set proper LayoutParams to avoid rowSpan issues
        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }
        cardView.layoutParams = params

        cardView.setOnClickListener {
            val intent = Intent(this, CoursePlayerActivity::class.java)
            intent.putExtra("videoUrl", course.videoUrl)
            startActivity(intent)
        }

        coursesGrid.addView(cardView)
    }

}
