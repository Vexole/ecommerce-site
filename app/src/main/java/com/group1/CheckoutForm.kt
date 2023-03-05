package com.group1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.group1.model.Cart
import com.group1.utils.CartUtils
import com.stripe.android.PaymentConfiguration
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

//const val BackendUrl = "http://10.0.2.2:4242/"
const val BackendUrl = "https://clear-tank-top-lion.cyclic.app/"

class CheckoutForm : AppCompatActivity() {
    private val httpClient = OkHttpClient()
    private lateinit var publishableKey: String

    private lateinit var etShippingAddressName : EditText
    private lateinit var etAddressLine1 : EditText
    private lateinit var etCity : EditText
    private lateinit var etState : EditText
    private lateinit var etPostalCode: EditText
    private lateinit var btnProcessPayment : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_form)
        fetchPublishableKey()
        etShippingAddressName = findViewById(R.id.etShippingAddressName)
        etAddressLine1 = findViewById(R.id.etAddressLine1)
        etCity = findViewById(R.id.etCity)
        etState = findViewById(R.id.etState)
        etPostalCode = findViewById(R.id.etPostalCode)
        btnProcessPayment = findViewById(R.id.btnProcessPayment)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("userInfo",
            Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "@gmail")!!

        btnProcessPayment.setOnClickListener {
            CartUtils.getCart(userId).get()
                .addOnSuccessListener {
                    val cart = it.children.lastOrNull()?.getValue(Cart::class.java)
                    if (cart == null || cart.status == "Complete") {
                        displayAlert("Error", "Cart is empty!")
                    } else {
                        val shippingAddressName = etShippingAddressName.text.toString().trim()
                        val addressLine1 = etAddressLine1.text.toString().trim()
                        val city = etCity.text.toString().trim()
                        val state = etState.text.toString().trim()
                        val postalCode = etPostalCode.text.toString().trim()

                        if (shippingAddressName.isEmpty()) {
                            etShippingAddressName.error = "Shipping address name is required."
                            return@addOnSuccessListener
                        }

                        if (addressLine1.isEmpty()) {
                            etAddressLine1.error = "Shipping address is required."
                            return@addOnSuccessListener
                        }

                        if (city.isEmpty()) {
                            etCity.error = "City is required."
                            return@addOnSuccessListener
                        }

                        if (state.isEmpty() || state.length > 2) {
                            etState.error = "State is required in two character format."
                            return@addOnSuccessListener
                        }

                        if (postalCode.isEmpty() || postalCode.length != 6) {
                            etPostalCode.error = "Postal code is required without spaces."
                            return@addOnSuccessListener
                        }
                        val i = Intent(applicationContext, Payment::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext.startActivity(i)
                    }
                }.addOnFailureListener{
                    Toast.makeText(applicationContext, "Failed Fetching Cart", Toast.LENGTH_LONG).show()
                    return@addOnFailureListener
                }
        }
    }

    private fun displayAlert(title: String, message: String) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)

            builder.setPositiveButton("Ok", null)
            builder.create().show()
        }
    }

    private fun fetchPublishableKey() {
        val request = Request.Builder().url(BackendUrl + "config").build()
        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                displayAlert("Request Failed", "Error: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    displayAlert("Request Failed", " Error: $response")
                } else {
                    val responseData = response.body?.string()
                    val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()
                    publishableKey = responseJson.getString("publishableKey")

                    PaymentConfiguration.init(applicationContext, publishableKey)
                }
            }
        })
    }
}