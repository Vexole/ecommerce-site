package com.group1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(
    private val context: Context,
    private val splashScreens: List<SplashScreen>
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        var tvStoreDescription: TextView? = itemView.findViewById(R.id.tvStoreDescription)
        var tvDeliveredText: TextView = itemView.findViewById(R.id.tvDeliveredText)
        var imgSplashScreen: ImageView = itemView.findViewById(R.id.imgSplashScreen)
        var btnShopNow: Button = itemView.findViewById(R.id.btnShopNow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_splash_screen, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val splashScreen = splashScreens[position]
        holder.tvStoreDescription?.text = splashScreen.description
        holder.tvDeliveredText.text = splashScreen.delivery
        holder.imgSplashScreen.setImageResource(splashScreen.image)
        holder.btnShopNow.setOnClickListener {
            val i = Intent(context, ProductList::class.java)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int = splashScreens.size
}