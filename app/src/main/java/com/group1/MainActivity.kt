package com.group1

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.group1.model.Product
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vPager = findViewById<ViewPager2>(R.id.vPager)
        val adapter = ViewPagerAdapter(this, arrayListOf(
            SplashScreen("Shopping Made Easy!", "SHOP FROM HOME AND GET IT DELIVERED TO YOUR DOOR STEPS.", R.drawable.phones3),
            SplashScreen("Splash 2", "Delivery 2", R.drawable.phones4),
            SplashScreen("Splash 3", "Delivery 3", R.drawable.phones5)
        ))
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
                        "DKNY Mens Modern Fit High Performance Suit Separates",
                        "DKNY's active tailoring system incorporates elastic yarns into this high performance 4-way stretch fabric creating a suit where one can truly stretch in. This specialized fabric provides the performance attributes of athletic clothing, but now in a tailored suit for that added mobility, comfort, and bounce back resiliency to keep you sharp throughout the day or that special occasion.",
                        "In-Stock",
                        199.99,
                        "s1000_dkny.jpg",
                        "68% Polyester, 28% Viscose, 4% Spandex",
                        "DKNY",
                        "DKNY",
                        "Mens",
                        1,
                        "Dry Clean Only"
                    ),
                    Product(
                        "S1001",
                        "Calvin Klein Mens Slim Fit Navy Blue Suit Separates Sale",
                        "This Calvin Klein Men’s Slim Fit Stretch Suit Separates gives you the option of customizing your jacket and pant suit size since the two pieces are sold separately. Dressing to impress has never been easier!",
                        "In-Stock",
                        299.99,
                        "s1001_ck.jpg",
                        "100% Wool",
                        "CK",
                        "Calvin Klein Tailored",
                        "Mens",
                        1,
                        "Dry Clean Only"
                    ),
                    Product(
                        "S1002",
                        "COOFANDY Men's Shiny Sequins Blazer One Button Tuxedo for Party",
                        "COOFANDY provides the finest, stylish and trendy Clothing which can be worn in every seasons, adapts to your ever-changing lifestyle, ensures you'll find the perfect fit and enjoy every moment.",
                        "In-Stock",
                        83.99,
                        "s1002_coofandy.jpg",
                        "Polyester and Sequins",
                        "COOFANDY",
                        "COOFANDY",
                        "Mens",
                        1,
                        "Hand Wash Only"
                    ),
                    Product(
                        "S1003",
                        "Marycrafts Women's 2 Buttons Business Blazer Pant Suit Set for Work",
                        "You can have both comfortable feelings and impeccable profesional look for business profestional attire and business formal attire in this immaculately tidy two-piece suit. Rendered in various elegant colors, made from 4 way stretchy fabric, enhanced by a pair of pull one pants with elastic waistband, this pant suit is really a gift for any office women.",
                        "In-Stock",
                        159.99,
                        "s1003_marycrafts.jpg",
                        "67% rayon, 28% nylon, 5% elastane. Machine wash cold with like colors, lay flat to dry or line dry.",
                        "Marycrafts",
                        "Marycrafts",
                        "Womens",
                        1,
                        "Machine Wash"
                    ),
                    Product(
                        "S1004",
                        "MAGE MALE Men's 3 Pieces Suit Elegant Solid One Button Slim Fit",
                        "One Button Closure,Single Breasted,Notch Lapel,Side Vents,Slim Fit Style;Vests:Four Button,V-Neck,Smooth Back and Adjustable Back Tie;Pants:Flat Front,Zippered Fly,Button End and Adjustable Waist Tab.",
                        "In-Stock",
                        229.99,
                        "s1004_magemale.jpg",
                        "Cotton Polyester Blended",
                        "MAGE MALE",
                        "MAGE MALE",
                        "Mens",
                        1,
                        "Dry Clean Only"
                    ),
                    Product(
                        "S1005",
                        "LISUEYNE Women’s Two Pieces Blazer Office Lady Suit Set",
                        "Notched lapel collar,and comfortable thick shoulder pads.Exquisite and stick double-breasted closure fasten the suit.Practical and real front pockets with covered placket.Classic long sleeve cuffs design,unique solid pattern design,waisted,regular and neat blazer hem.",
                        "In-Stock",
                        89.99,
                        "s1005_lisueyne.jpg",
                        "Made of high quality polyester&spandex fabric.Breathable and comfortable,wrinkle resistant,no fading.Soft lining is friendly to your skin that gives you comfortable wearing experience",
                        "LISUEYNE",
                        "LISUEYNE",
                        "Womens",
                        1,
                        "Soak in cold water for 15 min, then washing with normal detergent . The temperature of the lotion should not exceed 45 °C. After washing, ventilate and dry, do not expose to the sun."
                    ),
                    Product(
                        "S1006",
                        "Marycrafts Women's Business Blazer Pant Suit Set for Work",
                        "Elevate your everyday with our impeccably tailored blazer pant suits for women, rendered in various elegant colors. 4 ways stretch, very comfortable to wear day to day.",
                        "In-Stock",
                        174.99,
                        "s1006_marycrafts.jpg",
                        "onte de roma 67% viscose, 28% nylon, 5% elastane. Lining: 97% polyester 3% elastane.",
                        "Marycrafts",
                        "Marycrafts",
                        "Womens",
                        1,
                        "DMachine Wash"
                    ),
                    Product(
                        "S1007",
                        "Boys Plaid Suit Blue Formal Slim Fit, Bow Tie Kids Tuxedo",
                        "Classic plaid pattern, single-breasted with one button blazer, elastic waistband dress pants, fully lined vest with a cute red adjustable bow tie",
                        "In-Stock",
                        79.99,
                        "s1007_lontakids.jpg",
                        " Cotton. This checkered suits for boys ensures comfy and warm",
                        "lontakids",
                        "lontakids",
                        "Boys",
                        1,
                        "Dry Clean Only"
                    ),
                    Product(
                        "S1008",
                        "FEOYA Boys 4 Piece Formal Suit, Slim Fit with Straps",
                        "Adjustable suspenders strap for your preference, stylish bowtie, slim fit pants with double side pockets, the long sleeves lapel collar dress shirt with pleated front. They are soft, comfortable and breathable, perfect even for all-day wear",
                        "In-Stock",
                        699.99,
                        "s1008_feoya.jpg",
                        "Pant - 80% Polyester + 20% Viscose, Shirt-100% Polyester,Bowtie-Polyester. Excellent Fabric, Smooth, Wrinkle-resistant,Skin-friendly, no added harmful substances",
                        "FEOYA",
                        "FEOYA",
                        "Boys",
                        1,
                        "Dry Clean Only"
                    )
                )
                productList.forEach { product ->
                    val storageRef = Firebase.storage.reference
//                    val productImageRef = storageRef.child(product.imageName)
//                    var file = Uri.parse("android.resource://" + packageName + "/" + R.drawable.splash1)
//                    productImageRef.putFile(file)
                    product.imageName = "gs://mobilecrunchers.appspot.com/cart-hero.jpeg"

                    ref.child(product.productId).setValue(product)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Failed to connect to DB", Toast.LENGTH_LONG).show()
        }
    }
}