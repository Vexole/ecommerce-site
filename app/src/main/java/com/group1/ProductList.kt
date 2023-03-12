package com.group1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.group1.model.Product
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ProductList : AppCompatActivity() {
    private lateinit var adapter: ProductListAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        firebaseAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        val user = firebaseAuth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        if (user?.email == null) {
            val navMenu: Menu = navView.menu
            navMenu.findItem(R.id.goToCart).isVisible = false
            navMenu.findItem(R.id.checkout).isVisible = false
            navMenu.findItem(R.id.logOut).isVisible = false
        } else {
            val navMenu: Menu = navView.menu
            navMenu.findItem(R.id.login).isVisible = false
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.goToCart -> {
                    val intent = Intent(applicationContext, UserCart::class.java)
                    startActivity(intent)
                }
                R.id.checkout -> {
                    val intent = Intent(applicationContext, CheckoutForm::class.java)
                    startActivity(intent)
                }
                R.id.login -> {
                    val intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                }
                R.id.logOut -> {
                    signOut()
                    signOutGoogle()
                }
            }
            true
        }

        getUserProfile(user)
        setAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun getUserProfile(user: FirebaseUser?) {
        val welcomeMessage = findViewById<TextView>(R.id.txtWelcomeUser1)
        val emailUserMessage = findViewById<TextView>(R.id.txtWelcomeUser2)
        welcomeMessage.text =
            if (user?.email == null) "Welcome to MobileCrunchers!" else "Welcome back,"
        if (user?.email == null) {
            emailUserMessage.isVisible = false
        } else {
            emailUserMessage.isVisible = true
            emailUserMessage.text = user.email.toString() + "!"
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.apply()
        val intent = Intent(applicationContext, ProductList::class.java)
        startActivity(intent)
    }

    private fun signOutGoogle() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
                    "userInfo",
                    Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.remove("userId")
                editor.apply()
                val intent = Intent(applicationContext, ProductList::class.java)
                startActivity(intent)
            }
    }

    private fun setAdapter() {
        val query = FirebaseDatabase.getInstance().getReference("products")
        val options =
            FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()

        adapter = ProductListAdapter(applicationContext, options)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
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
        adapter.startListening()
    }
}