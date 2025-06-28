package com.example.kotlin_250_project.ui

import Course
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CourseAdapter
    private val courseList = mutableListOf<Course>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var enrolledCourses: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set button listeners
        binding.mycourses.setOnClickListener {
            startActivity(Intent(requireContext(), MyCoursesActivity::class.java))
        }

        binding.mynotes.setOnClickListener {
            startActivity(Intent(requireContext(), NotesActivity::class.java))
        }

        binding.myreports.setOnClickListener {
            startActivity(Intent(requireContext(), ReportCardActivity::class.java))
        }

        binding.myresults.setOnClickListener {
            startActivity(Intent(requireContext(), ResultActivity::class.java))
        }

        binding.myprogress.setOnClickListener {
            startActivity(Intent(requireContext(), ProgressActivity::class.java))
        }

        binding.myacheievements.setOnClickListener {
            startActivity(Intent(requireContext(), AchievementActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coursesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadUserEnrolledCourses()
    }

    private fun loadUserEnrolledCourses() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Users").document(uid).get()
            .addOnSuccessListener { document ->
                enrolledCourses = document.get("courses") as? List<String> ?: emptyList()
                loadCourses()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load enrolled courses", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadCourses() {
        firestore.collection("Courses").get()
            .addOnSuccessListener { result ->
                courseList.clear()
                for (doc in result) {
                    val course = doc.toObject(Course::class.java).copy(id = doc.id)
                    courseList.add(course)
                }

                adapter = CourseAdapter(
                    courses = courseList,
                    enrolledCourseIds = enrolledCourses,
                    onPlayClick = { course ->
                        if (enrolledCourses.contains(course.id)) {
                            val intent = Intent(requireContext(), CoursePlayerActivity::class.java)
                            intent.putExtra("videoUrl", course.videoUrl)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please enroll to access this course",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    onEnrollClick = { courseId ->
                        val uid = auth.currentUser?.uid ?: return@CourseAdapter
                        firestore.collection("Users").document(uid)
                            .update("courses", FieldValue.arrayUnion(courseId))
                            .addOnSuccessListener {
                                enrolledCourses = enrolledCourses + courseId
                                adapter.updateEnrolledCourses(enrolledCourses)
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to enroll", Toast.LENGTH_SHORT).show()
                            }
                    }
                )

                binding.coursesRecyclerView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load courses", Toast.LENGTH_SHORT).show()
            }
    }

}
