package com.group1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.group1.model.Model

class ProductDetail : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productId = intent.getStringExtra("modelId") ?: ""
        FirebaseDatabase.getInstance().getReference("model")
                .child(productId).get().addOnSuccessListener {
                    val model = it.getValue(Model::class.java)
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

                    tvModelId.text = "Model ID: " + model?.modelId
                    tvModelTitle.text = model?.modelName
                    tvProductDescription.text = model?.modelDescription
                    tvUnit.text = "1 Unit"
                    tvStatus.text = model?.modelStatus
                    tvPrice.text = "$ " + model?.price
                    tvOperatingSys.text = model?.operatingSys
                    tvBrand.text = model?.brand
                    tvColor.text = model?.color
                    tvCapacity.text = model?.capacity
                    val storeRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model?.imageName ?: "")
                    Glide.with(imgProduct.context).load(storeRef).into(imgProduct)
                    btnAddToCart.setOnClickListener {
                        val i = Intent(applicationContext, CheckoutForm::class.java)
                        startActivity(i)
                    }
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Error Fetching Data", Toast.LENGTH_LONG).show()
            }
    }
}