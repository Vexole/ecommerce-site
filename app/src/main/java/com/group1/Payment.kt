package com.group1

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.group1.model.Cart
import com.group1.model.LineItem
import com.group1.model.Product
import com.group1.payment.ApiClient
import com.group1.utils.CartUtils
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardInputWidget

class Payment : AppCompatActivity() {
    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("userInfo",
            Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "@gmail.com")!!

        try {
            stripe =
                Stripe(this, PaymentConfiguration.getInstance(applicationContext).publishableKey)
            CartUtils.getCart(userId).get()
            .addOnSuccessListener {
                val cart: Cart = it.getValue(Cart::class.java) ?: Cart("test", listOf(), 0.00, "In-Progress")
                var tvYouPay = findViewById<TextView>(R.id.tvYouPay)
                tvYouPay.text = "Your Payment Total is ${cart.total}"
                startCheckout(cart.total)
            }.addOnFailureListener{
                Toast.makeText(applicationContext, "Failed Fetching Cart", Toast.LENGTH_LONG).show()
            }
        } catch (ex: Exception) {
            Toast.makeText(applicationContext, "Issue Processing Payment: " + ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun displayAlert(title: String, message: String, i: Intent? = null) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)

            if (i == null)
                builder.setPositiveButton("Ok", null)
            else
                builder.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->  applicationContext.startActivity(i)})
            builder.create().show()
        }
    }

    private fun startCheckout(total: Double) {
        ApiClient().createPaymentIntent("card", "CAD", total, completion = {
            paymentIntentClientSecret, error ->
            run {
                paymentIntentClientSecret?.let {
                    this.paymentIntentClientSecret = it
                }
                error?.let {
                    displayAlert("Failed to Load PaymentIntent", "Error: $error")
                }
            }
        })

        // Confirm the PaymentIntent with the payment widget
        val cardInputWidget = findViewById<CardInputWidget>(R.id.cardInputWidget)
        val payButton = findViewById<Button>(R.id.payButton)
        payButton.setOnClickListener {
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle the results of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object: ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                if (paymentIntent.status == StripeIntent.Status.Succeeded) {
//                    val gson = GsonBuilder().setPrettyPrinting().create()
                    CartUtils.getCart(userId)
                        .get()
                        .addOnSuccessListener {
                            val cart: Cart = it.getValue(Cart::class.java) ?: Cart("test", listOf(), 0.00, "In-Progress")
                            cart.status = "Complete"
                            CartUtils.getCart(userId)
                                .setValue(cart)
                        }.addOnFailureListener{
                            Toast.makeText(applicationContext, "Failed Checkout Process", Toast.LENGTH_LONG).show()
                        }
                    val i = Intent(applicationContext, ProductList::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    displayAlert("Payment Succeeded", "We have received your payment. Thank you for the order!", i)
                } else if (paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
                    displayAlert("Payment Failed", paymentIntent.lastPaymentError?.message.orEmpty())
                }
            }

            override fun onError(e: Exception) {
                displayAlert("Error", e.toString())
            }
        })
    }
}