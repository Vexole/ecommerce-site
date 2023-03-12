package com.group1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.FirebaseDatabase
import com.group1.model.Product

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vPager = findViewById<ViewPager2>(R.id.vPager)
        val adapter = ViewPagerAdapter(
            this, arrayListOf(
                SplashScreen(
                    "Shopping Made Easy!",
                    "SHOP FROM HOME AND GET IT DELIVERED TO YOUR DOOR STEPS.",
                    R.drawable.phones3
                ),
                SplashScreen(
                    "Shopping Made Easy!",
                    "100% GENUINE PRODUCTS GUARANTEED.",
                    R.drawable.phones4
                ),
                SplashScreen("Shopping Made Easy!", "HASSLE FREE RETURNS", R.drawable.phones5)
            )
        )
        vPager.adapter = adapter
        seedDatabase()
    }

    private fun seedDatabase() {
        val ref = FirebaseDatabase.getInstance().getReference("products")
        ref.get().addOnSuccessListener {
            if (it.exists() && (it.value == "" || it.value == null)) {
                val productList = listOf(
                    Product(
                        "S1000",
                        "iPhone 14",
                        "It is a high-end 5G-capable smartphone with a 6.1-inch OLED display and dual 12 MP main cameras.  It works with iOS 16, embeds the Apple A15 Bionic SoC, has 6 GB of RAM and 128, 256 or 512 GB of storage. It is available in 3 colors (Blue, Black and Red).",
                        "In-Stock",
                        899.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/iphone14.png",
                        "IOS",
                        "iPhone",
                        "Purple",
                        "128 GB"
                    ),
                    Product(
                        "S1001",
                        "iPhone 11",
                        "The iPhone 11 is splash, water, and dust resistant . It has been laboratory tested under controlled conditions and has obtained the IP68 protection rating defined by the International Electrotechnical Commission (IEC) standard 60529 (maximum depth of 2 meters for a maximum of 30 minutes).",
                        "In-Stock",
                        599.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/iphone11.png",
                        "IOS",
                        "iPhone",
                        "Red",
                        "256 GB"
                    ),
                    Product(
                        "S1002",
                        "Samsung Galaxy S20",
                        "Samsung Galaxy offers, with the Galaxy s20, a premium smartphone that incorporates high-end performance whether for its camera or its software. Available in 4G and 5G, the Galaxy S20 is one of the first smartphones to support the new connection speed.",
                        "In-Stock",
                        683.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/samsungs20.png",
                        "Android",
                        "Samsung",
                        "Black",
                        "512 GB"
                    ),
                    Product(
                        "S1003",
                        "Samsung Galaxy A51",
                        "The Samsung A51 has nothing to envy to its competitors! It has 4 photo sensors on its back to adapt to all situations, an Exynos 9611 processor equivalent to a Snapdragon 712 and a 6.5-inch AMOLED screen with full HD+ definition.",
                        "In-Stock",
                        159.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/galaxya51.png",
                        "Android",
                        "Samsung",
                        "Prism Crush Blue",
                        "128 GB"
                    ),
                    Product(
                        "S1004",
                        "Tecno Spark 8 Pro",
                        "\n" + "The front camera has an 8 MP (wide) sensor with a Quad-LED flash. The sensors available in the device include Fingerprint (rear-mounted), accelerometer, and proximity. The smartphone is fueled by a Non-removable Li-Ion 5000 mAh battery + Charging 10W while the phone runs on Android 11 + HIOS 7.6 operating system.",
                        "In-Stock",
                        229.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/tecno_spark_8_pro.png",
                        "Android",
                        "Tecno",
                        "Teal",
                        "128 GB"
                    ),
                    Product(
                        "S1005",
                        "Samsung Galaxy S23 Ultra",
                        "Meet the new Galaxy S23 Ultra, designed for better sustainability and equipped with a built-in S Pen, Nightography camera and powerful chip for epic gaming.",
                        "In-Stock",
                        989.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/samsung_galaxy_s23_ultra.png",
                        "Android",
                        "Samsung",
                        "Cream",
                        "128 GB",
                    ),
                    Product(
                        "S1006",
                        "Google Pixel 7",
                        "The Pixel 7 builds on the Pixel 6's success with stellar cameras and a more advanced Tensor G2 chipset. It's not going to dethrone the iPhone 14 or Galaxy S22, but the Pixel 7 is a great value.",
                        "In-Stock",
                        574.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/google_pixel_7.png",
                        "Android",
                        "Google",
                        "Hazel",
                        "128 GB"
                    ),
                    Product(
                        "S1007",
                        "iPhone SE 3rd Gen",
                        "Lightning-fast A15 Bionic chip and fast 5G.1 Big-time battery life and a superstar camera. Plus, the toughest glass in a smartphone and a Home button with secure Touch ID. This device supports 5G UW mid-band (C-band) only, 5G Nationwide and 4G LTE.",
                        "In-Stock",
                        799.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/iphone_se_3rd_gen.png",
                        "IOS",
                        "iPhone",
                        "Midnight",
                        "64 GB"
                    ),
                    Product(
                        "S1008",
                        "iPhone 12 Pro Max",
                        "The iPhone 12 Pro Max is the larger of two pro-level phones in Apple's 2020 iPhone lineup. It has a glass and stainless-steel design with flat edges. The camera system is best-in-class with better stabilization and larger sensors.",
                        "In-Stock",
                        899.99,
                        "gs://mobilecrunchers.appspot.com/modelImages/iphone_12_pro_max.png",
                        "IOS",
                        "iPhone",
                        "White",
                        "128 GB"
                    )
                )
                productList.forEach { product ->
                    ref.child(product.modelId).setValue(product)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to connect to DB", Toast.LENGTH_LONG).show()
        }
    }
}