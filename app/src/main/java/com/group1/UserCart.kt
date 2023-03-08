package com.group1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group1.model.Cart
import com.group1.model.LineItem
import com.group1.utils.CartUtils

class UserCart : AppCompatActivity() {
    private lateinit var cartItems: List<LineItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cart)

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
}