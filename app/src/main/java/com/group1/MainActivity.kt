package com.group1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vPager = findViewById<ViewPager2>(R.id.vPager)
        val adapter = ViewPagerAdapter(this, arrayListOf(
            SplashScreen("Splash 1", "Delivery 1", R.drawable.splash1),
            SplashScreen("Splash 2", "Delivery 2", R.drawable.splash2),
            SplashScreen("Splash 3", "Delivery 3", R.drawable.splash3)
        ))
        vPager.adapter = adapter
    }
}