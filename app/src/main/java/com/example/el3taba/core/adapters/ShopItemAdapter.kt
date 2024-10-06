package com.example.el3taba.core.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.el3taba.core.dataClasses.Product
import com.example.el3taba.databinding.ItemShopProductBinding

class ShopItemAdapter(
    private val products: List<Product>
) : RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>() {

    inner class ShopItemViewHolder(val binding: ItemShopProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productPrice.text = product.price
            binding.productOldPrice.text = product.oldPrice
            binding.productRating.rating = product.rating
            binding.productImage.setImageResource(product.imageResId)
            val textView = binding.productOldPrice
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            // Handle favorite icon click if necessary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ItemShopProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
