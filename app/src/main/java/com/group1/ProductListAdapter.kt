package com.group1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.group1.model.Cart
import com.group1.model.LineItem
import com.group1.model.Product
import com.group1.utils.CartUtils

class ProductListAdapter(private val context: Context, options: FirebaseRecyclerOptions<Product>):
    FirebaseRecyclerAdapter<Product, ProductListAdapter.MyViewHolder>(options) {

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.product, parent, false)) {
        var cvProductList: CardView = itemView.findViewById(R.id.cvProductList)
        var imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        var tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        var tvProductDescription:TextView = itemView.findViewById(R.id.tvProductDescription)
        var tvPrice:TextView = itemView.findViewById(R.id.tvPrice)
        var tvStatus:TextView = itemView.findViewById(R.id.tvStatus)
        var btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Product) {
        holder.tvProductTitle.text = model.productName
        holder.tvProductDescription.text = model.productDescription
        holder.tvPrice.text = StringBuilder().append("$ ").append(model.price).toString()
        holder.tvStatus.text = model.productStatus
        holder.cvProductList.setOnClickListener {
            val i = Intent(context, ProductDetail::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("productId", model.productId)
            context.startActivity(i)
        }

        holder.btnAddToCart.setOnClickListener {
            val context = it.context
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", "@gmail.com")!!

            if (userId == "@gmail.com") {
                val i = Intent(context, Login::class.java)
                context.startActivity(i)
            }

            CartUtils.getCart(userId)
                .get()
                .addOnSuccessListener { storedCart ->
                    run {
                        val cart: Cart =
                            storedCart.getValue(Cart::class.java) ?: Cart(userId, mutableListOf(), 0.00, "In-Progress")
                        val cartItem = cart.lineItemList.filter { item -> item.itemId == model.productId }
                        if ((cart.lineItemList.isEmpty() || cartItem.isEmpty()) && cart.status == "Complete") {
                            val item = LineItem(model.productId, model.price, 1)
                            val list = cart.lineItemList.toMutableList()
                            list.add(item)
                            cart.lineItemList = list
                        } else {
                            cartItem[0].qty += 1
                        }
                        cart.total += model.price
                        CartUtils.getCart(userId)
                            .setValue(cart)
                        Toast.makeText(context, "Item Added to Cart", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(context, "Failed Fetching Cart", Toast.LENGTH_LONG).show()
                }
        }

        val storeRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.imageName)
        Glide.with(holder.imgProduct.context).load(storeRef).into(holder.imgProduct)
    }
}