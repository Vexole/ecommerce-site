package com.group1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.group1.model.Product

class ProductList : AppCompatActivity() {
    private lateinit var adapter: ProductListAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        firebaseAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)

        getUserProfile()
        setAdapter()

        val btnSignOut = findViewById<Button>(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun getUserProfile() {
        val user = firebaseAuth.currentUser
        val welcomeMessage = findViewById<TextView>(R.id.txtWelcomeUser)
        welcomeMessage.text = "Welcome back, " + user?.email.toString() + "!"
    }

    private fun signOut() {
        firebaseAuth.signOut()
        val intent = Intent(applicationContext, Login::class.java)
        startActivity(intent)
    }

    private fun setAdapter() {
        val query = FirebaseDatabase.getInstance().getReference("products")
        val options = FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()

        adapter = ProductListAdapter(applicationContext, options)
        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        } else {
            recyclerView.layoutManager =
                GridLayoutManager(applicationContext, 3, GridLayoutManager.VERTICAL, false)
        }
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
}