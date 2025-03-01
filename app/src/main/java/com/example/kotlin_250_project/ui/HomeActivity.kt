package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.adapters.ViewPagerAdapter
import com.example.kotlin_250_project.databinding.ActivityHomeBinding
import com.example.kotlin_250_project.models.CardModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewPager2 Carousel
        setupViewPager()

        // Setup Bottom Navigation
//        setupBottomNavigation()
    }

    // 🔹 Function to setup ViewPager2 with a carousel effect
    private fun setupViewPager() {
        val cardList = listOf(
            CardModel("Math Class", "Learn algebra, geometry, and calculus.", "Join Now"),
            CardModel("Science Class", "Explore physics, chemistry, and biology.", "Explore"),
            CardModel("History Class", "Dive into ancient and modern history.", "Start"),
            CardModel("Programming", "Master coding with Java, Python, and more.", "Enroll")
        )

        adapter = ViewPagerAdapter(cardList)
        binding.viewPager.adapter = adapter

        //  Enable Carousel Effect
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.viewPager.setPageTransformer(CarouselPageTransformer())
    }

    // 🔹Function to setup Bottom Navigation
//    private fun setupBottomNavigation() {
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.menu_home -> true
//                R.id.menu_profile -> true
//                R.id.menu_settings -> true
//                else -> false
//            }
//        }
//    }

    // 🔹 Custom CarouselPageTransformer Function
    private class CarouselPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val scaleFactor = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.alpha = 0.5f + (1 - kotlin.math.abs(position)) * 0.5f
        }
    }
}
