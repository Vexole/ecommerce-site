package com.group1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.stripe.android.PaymentConfiguration
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

const val BackendUrl = "http://10.0.2.2:4242/"

class CheckoutForm : AppCompatActivity() {
    private val httpClient = OkHttpClient()
    private lateinit var publishableKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_form)
        fetchPublishableKey()
        val btnProcessPayment = findViewById<Button>(R.id.btnProcessPayment)
        btnProcessPayment.setOnClickListener {
            val i = Intent(applicationContext, Payment::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            applicationContext.startActivity(i)
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