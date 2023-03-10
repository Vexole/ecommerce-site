package com.group1

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.group1.model.Model

class ProductList : AppCompatActivity() {
    private lateinit var adapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        setAdapter()
    }

    private fun setAdapter() {
        val query = FirebaseDatabase.getInstance().getReference("products")
        val options = FirebaseRecyclerOptions.Builder<Model>().setQuery(query, Model::class.java).build()

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