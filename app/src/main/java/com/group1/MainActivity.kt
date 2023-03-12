package com.group1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
        seedDatabase()
    }

    private fun seedDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference("model")
        ref.get().addOnSuccessListener {
            if (it.exists() && (it.value == "" || it.value == null)) {
                val productList = listOf(ref.database)
                productList.forEach { model ->
                    val storageRef = Firebase.storage.reference
                    ref.child(model.toString()).setValue(model)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to connect to DB", Toast.LENGTH_LONG).show()
        }
    }
}