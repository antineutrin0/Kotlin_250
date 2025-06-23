package com.example.kotlin_250_project.ui

import Course
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_250_project.R

class CourseAdapter(
    private var courses: List<Course>,
    private var enrolledCourseIds: List<String>,
    private val onPlayClick: (Course) -> Unit,
    private val onEnrollClick: (String) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    fun updateEnrolledCourses(updatedEnrolledCourses: List<String>) {
        enrolledCourseIds = updatedEnrolledCourses
        notifyDataSetChanged()
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.courseThumbnail)
        val title: TextView = itemView.findViewById(R.id.courseTitle)
        val description: TextView = itemView.findViewById(R.id.courseDescription)
        val enrollButton: Button = itemView.findViewById(R.id.enrollButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_card, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]

        // Set title and description
        holder.title.text = course.title
        holder.description.text = course.description

        // Load thumbnail using Glide
        Glide.with(holder.itemView.context)
            .load(course.thumbnail)
            .into(holder.thumbnail)

        // Check if course is already enrolled
        val isEnrolled = enrolledCourseIds.contains(course.id)
        holder.enrollButton.visibility = if (isEnrolled) View.GONE else View.VISIBLE

        // Handle Enroll button click
        holder.enrollButton.setOnClickListener {
            onEnrollClick(course.id)
        }

        // Play video on card click
        holder.itemView.setOnClickListener {
            onPlayClick(course)
        }
    }

    override fun getItemCount(): Int = courses.size
}
