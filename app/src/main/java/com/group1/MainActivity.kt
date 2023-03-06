package com.group1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSignIn : Button = findViewById(R.id.btnMainSignIn)

        btnSignIn.setOnClickListener {
            Log.d("TAG", "click on the button!!!!!")
            var intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }
}