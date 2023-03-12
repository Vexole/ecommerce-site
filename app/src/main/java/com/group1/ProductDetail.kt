package com.group1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.group1.model.Cart
import com.group1.model.LineItem
import com.group1.model.Product
import com.group1.utils.CartUtils

class ProductDetail : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

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

        val productId = intent.getStringExtra("modelId") ?: ""
        FirebaseDatabase.getInstance().getReference("products")
            .child(productId).get().addOnSuccessListener {
                val model = it.getValue(Product::class.java)!!
                val imgProduct = findViewById<ImageView>(R.id.imageView)
                val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)

                val tvModelId = findViewById<TextView>(R.id.tvModelId)
                val tvModelTitle = findViewById<TextView>(R.id.tvProductTitle)
                val tvProductDescription = findViewById<TextView>(R.id.tvProductDescription)
                val tvUnit = findViewById<TextView>(R.id.tvUnit)
                val tvStatus = findViewById<TextView>(R.id.tvStatus)
                val tvPrice = findViewById<TextView>(R.id.tvPrice)
                val tvOperatingSys = findViewById<TextView>(R.id.tvOperatingSys)
                val tvBrand = findViewById<TextView>(R.id.tvBrand)
                val tvColor = findViewById<TextView>(R.id.tvColor)
                val tvCapacity = findViewById<TextView>(R.id.tvCapacity)

                tvModelId.text = "Product ID: " + model.modelId
                tvModelTitle.text = model.modelName
                tvProductDescription.text = model.modelDescription
                tvUnit.text = "1 Unit"
                tvStatus.text = model.modelStatus
                tvPrice.text = "$ " + model.price
                tvOperatingSys.text = model.operatingSys
                tvBrand.text = model.brand
                tvColor.text = model.color
                tvCapacity.text = model.capacity
                val storeRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                    model.imageName
                )
                Glide.with(imgProduct.context).load(storeRef).into(imgProduct)
                btnAddToCart.setOnClickListener { btn ->
                    val context = btn.context
                    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                        "userInfo",
                        Context.MODE_PRIVATE
                    )
                    val userId = sharedPreferences.getString("userId", "@gmail")!!

                    if (userId == "@gmail") {
                        val i = Intent(context, Login::class.java)
                        context.startActivity(i)
                    } else {
                        CartUtils.getCart(userId)
                            .get()
                            .addOnSuccessListener { storedCart ->
                                run {
                                    val cartValue = storedCart.children
                                    var cart: Cart? =
                                        cartValue.lastOrNull()?.getValue(Cart::class.java)
                                    if (cart == null || cart.status == "Complete") {
                                        cart = Cart(userId, mutableListOf(), 0.00, "In-Progress")
                                        val item = LineItem(model.modelId, model.price, 1)
                                        val list = cart.lineItemList.toMutableList()
                                        list.add(item)
                                        cart.lineItemList = list
                                    } else {
                                        val cartItem =
                                            cart.lineItemList.filter { item -> item.itemId == model.modelId }
                                        if (cartItem.isEmpty()) {
                                            val item = LineItem(model.modelId, model.price, 1)
                                            val list = cart.lineItemList.toMutableList()
                                            list.add(item)
                                            cart.lineItemList = list
                                        } else {
                                            cartItem[0].qty += 1
                                        }
                                    }
                                    cart.total =
                                        String.format("%.2f", (cart.total + model.price)).toDouble()
                                    val list = mutableListOf<Cart>()
                                    storedCart.children.forEach { argCart ->
                                        run {
                                            val oldCart: Cart = argCart.getValue(Cart::class.java)!!
                                            if (oldCart.status != "In-Progress") {
                                                list.add(oldCart)
                                            }
                                        }
                                    }
                                    list.add(cart)
                                    CartUtils.getCart(userId)
                                        .setValue(list)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Item Added to Cart",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, "Failed Fetching Cart", Toast.LENGTH_LONG)
                                    .show()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Error Fetching Data", Toast.LENGTH_LONG).show()
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