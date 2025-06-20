package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.HomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: HomeScreenBinding
    private var currentFragmentTag: String = "home" // Track which tab is active

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load default fragment (Home)
        loadFragment(HomeFragment(), "home")
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), "home")
                    currentFragmentTag = "home"
                }
                R.id.nav_exam -> {
                    loadFragment(ExamFragment(), "exam")
                    currentFragmentTag = "exam"
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment(), "profile")
                    currentFragmentTag = "profile"
                }
                R.id.nav_bookmark -> {
                    loadFragment(BookmarkFragment(), "bookmark")
                    currentFragmentTag = "bookmark"
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment, tag)
            .commit()
    }

    override fun onBackPressed() {
        if (currentFragmentTag != "home") {
            // Go back to home if not already there
            binding.bottomNavigation.selectedItemId = R.id.nav_home
            loadFragment(HomeFragment(), "home")
            currentFragmentTag = "home"
        } else {
            // Default back behavior (exit app)
            super.onBackPressed()
        }
    }
}
