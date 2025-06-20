package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.kotlin_250_project.R
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kotlin_250_project.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loadUserProfile()
        setupClickListeners()

        return binding.root
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("fullName") ?: "N/A"
                    val email = document.getString("email") ?: "N/A"
                    val number = document.getString("number") ?: "N/A"
                    val institute = document.getString("institute") ?: "N/A"
                    val profileImageUrl = document.getString("profileUrl")

                    binding.userName.text = name
                    binding.userEmail.text = "Email : $email"
                    binding.etinstitute.text = " $institute"
                    binding.number.text = " $number"

                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(profileImageUrl)
                            .into(binding.profileImage)
                    }
                } else {
                    Toast.makeText(requireContext(), "Profile data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to load profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupClickListeners() {
        binding.logoutLayout.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            requireActivity().finish()
        }

        binding.educationInfoLayout.setOnClickListener {
            startActivity(Intent(requireContext(),EducationInfoActivity::class.java))
        }

        binding.myresult.setOnClickListener {
            startActivity(Intent(requireContext(), ResultActivity::class.java))
        }

        binding.Mycourses.setOnClickListener {
            Toast.makeText(requireContext(), "My Courses Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.editIcon.setOnClickListener {
            startActivity(Intent(requireContext(), UpdateProfileActivity::class.java))
        }

        binding.notificationIcon.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        // You may remove bottomNavigationView handling from here if it's being handled globally
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
