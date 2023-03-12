package com.group1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.group1.model.Cart
import com.group1.model.LineItem
import com.group1.utils.CartUtils

class UserCart : AppCompatActivity() {
    private lateinit var cartItems: List<LineItem>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cart)

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

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )
        val userId = sharedPreferences.getString("userId", "@gmail")!!
        CartUtils.getCart(userId)
            .get()
            .addOnSuccessListener { storedCart ->
                run {
                    val cartValue = storedCart.children
                    val cart: Cart? = cartValue.lastOrNull()?.getValue(Cart::class.java)
                    val cLayout = findViewById<ConstraintLayout>(R.id.cLayout)
                    val noItemLayout = findViewById<ConstraintLayout>(R.id.noItemLayout)
                    if (cart == null || cart.status == "Complete") {
                        cLayout.isVisible = false
                        noItemLayout.isVisible = true
                    } else {
                        cLayout.isVisible = true
                        noItemLayout.isVisible = false
                        cartItems = cart.lineItemList

                        val tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)
                        tvTotalAmount.text = "$" + cart.total
                        val rView = findViewById<RecyclerView>(R.id.rView)
                        rView.layoutManager = LinearLayoutManager(applicationContext)
                        val adapter = CartItemAdapter(applicationContext, cartItems)
                        rView.adapter = adapter
                    }
                }
            }

        val btnCheckout = findViewById<Button>(R.id.btnCheckout)
        btnCheckout.setOnClickListener {
            val i = Intent(applicationContext, CheckoutForm::class.java)
            startActivity(i)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
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
                    MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.remove("userId")
                editor.apply()
                val intent = Intent(applicationContext, ProductList::class.java)
                startActivity(intent)
            }
    }
}