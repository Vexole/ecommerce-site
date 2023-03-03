package com.group1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class CheckoutForm : AppCompatActivity() {
    private lateinit var etName : EditText
    private lateinit var etAddress : EditText
    private lateinit var etCity : EditText
    private lateinit var etState : EditText
    private lateinit var etPostalCode: EditText
    private lateinit var etCountry : EditText
    private lateinit var etCreditCardNumber : EditText
    private lateinit var etMonth : EditText
    private lateinit var etYear : EditText
    private lateinit var etVerificationValue : EditText
    private lateinit var tvError : TextView
    private lateinit var btnProcess : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_form)

        etName = findViewById(R.id.etShippingAddressName)
        etAddress = findViewById(R.id.etAddressLine1)
        etCity = findViewById(R.id.etCity)
        etState = findViewById(R.id.etState)
        etPostalCode = findViewById(R.id.etPostalCode)
        etCountry = findViewById(R.id.etCountry)
        etCreditCardNumber = findViewById(R.id.etCreditCardNumber)
        etMonth = findViewById(R.id.etExpirationMonth)
        etYear = findViewById(R.id.etExpirationYear)
        etVerificationValue = findViewById(R.id.etCVV)
        tvError = findViewById(R.id.tvError)
        btnProcess = findViewById(R.id.btnProcessPayment)

        btnProcess.setOnClickListener{
            val addressName = etName.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val city = etCity.text.toString().trim()
            val state = etState.text.toString().trim()
            val postalCode = etPostalCode.text.toString().trim()
            val creditCardNumber = etCreditCardNumber.toString().trim()
            val month = etMonth.toString().trim()
            val year = etMonth.toString().trim()
            val cvv = etVerificationValue.toString().trim()

            fun resetForm(){
                etName.text = null
                etAddress.text = null
                etName.text = null
                etCity.text = null
                etState.text = null
                etPostalCode.text = null
                etCreditCardNumber.text = null
                etMonth.text = null
                etYear.text = null
                etVerificationValue.text = null
                tvError.text = null
            }
            if(addressName.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || postalCode.isEmpty() || creditCardNumber.isEmpty() || month.isEmpty() || year.isEmpty() || cvv.isEmpty()){
                tvError.error = "Please enter all required!"
                return@setOnClickListener
            }
            else if (creditCardNumber.length != 19){
                etCreditCardNumber.error = "Please follow the format"
                return@setOnClickListener
            }
            else if (month.toInt() > 12 || month.toInt() < 1 ){
                etMonth.error = "Value should be between 1 to 12"
            }
            else if (year.toInt() < 2023){
                etYear.error = "Expired credit card!"
            }
            else{
                resetForm()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}