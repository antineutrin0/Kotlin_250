package com.example.kotlin_250_project.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.HomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: HomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load default fragment
        loadFragment(HomeFragment())

        // Use the BottomNavigationView from HomeScreenBinding
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_exam -> ExamFragment()
                R.id.nav_profile-> ProfileFragment()
                R.id.nav_bookmark-> BookmarkFragment()
                else -> null
            }
            fragment?.let {
                loadFragment(it)
                true
            } ?: false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
