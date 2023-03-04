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
import com.group1.model.Product

class ProductDetail : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productId = intent.getStringExtra("productId") ?: ""
        FirebaseDatabase.getInstance().getReference("products")
                .child(productId).get().addOnSuccessListener {
                    val product = it.getValue(Product::class.java)
                    val imgProduct = findViewById<ImageView>(R.id.imageView)
                    val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)

                    val tvProductTitle = findViewById<TextView>(R.id.tvProductTitle)
                    val tvProductDescription = findViewById<TextView>(R.id.tvProductDescription)
                    val tvUnit = findViewById<TextView>(R.id.tvUnit)
                    val tvProductId = findViewById<TextView>(R.id.tvProductId)
                    val tvPrice = findViewById<TextView>(R.id.tvPrice)
                    val tvMaterial = findViewById<TextView>(R.id.tvMaterial)
                    val tvBrand = findViewById<TextView>(R.id.tvBrand)
                    val tvManufacturer = findViewById<TextView>(R.id.tvManufacturer)
                    val tvDepartment = findViewById<TextView>(R.id.tvDepartment)
                    val tvMinQtyPurchase = findViewById<TextView>(R.id.tvMinQtyPurchase)
                    val tvWashingInstruction = findViewById<TextView>(R.id.tvWashingInstruction)

                    tvProductTitle.text = product?.productName
                    tvProductDescription.text = product?.productDescription
                    tvUnit.text = "1 Unit"
                    tvProductId.text = "Product ID: " + product?.productId
                    tvPrice.text = "$ " + product?.price
                    tvMaterial.text = product?.material
                    tvBrand.text = product?.brand
                    tvManufacturer.text = product?.manufacturer
                    tvDepartment.text = product?.department
                    tvMinQtyPurchase.text = product?.minQtyPurchase.toString()
                    tvWashingInstruction.text = product?.washingInstruction
                    val storeRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(product?.imageName ?: "")
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