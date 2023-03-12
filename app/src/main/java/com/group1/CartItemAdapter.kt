package com.group1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group1.model.LineItem

class CartItemAdapter(private val context: Context, private val lineItems: List<LineItem>) :
    RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_cart_items, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lineItem = lineItems[position]
        holder.tvSN.text = (position + 1).toString()
        holder.tvProductName.text = lineItem.itemId
        holder.tvQty.text = lineItem.qty.toString()
        holder.tvUnitPrice.text = "$" + lineItem.price
        holder.tvTotal.text = "$" + String.format("%.2f", (lineItem.qty * lineItem.price))
    }

    override fun getItemCount(): Int {
        return lineItems.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvSN: TextView = itemView.findViewById(R.id.tvSN)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvQty: TextView = itemView.findViewById(R.id.tvQty)
        val tvUnitPrice: TextView = itemView.findViewById(R.id.tvUnitPrice)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    }
}