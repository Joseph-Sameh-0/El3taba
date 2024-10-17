package com.example.el3taba.core.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.databinding.ItemShopProductBinding
import com.google.firebase.firestore.FirebaseFirestore

class ShopItemAdapter(
    private val products: List<FinalProduct>,
    private val onItemClick: (FinalProduct) -> Unit
) : RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>() {

    var productImg: String? = null

    inner class ShopItemViewHolder(val binding: ItemShopProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: FinalProduct) {
            binding.sellerName.text = product.sellerName
            binding.productName.text = product.name
            binding.productPrice.text = product.price.toString()
            binding.productRating.rating = product.avgRating
//            binding.productImage.setImageResource(product.imageResId)
            productImg = product.imageUrl
//            val textView = binding.productOldPrice
//            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.root.setOnClickListener {
                onItemClick(product)
            }

            // Handle favorite icon click if necessary
            binding.favoriteIcon.setOnClickListener {
//                binding.favoriteIcon.isSelected = !binding.favoriteIcon.isSelected
                //

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding =
            ItemShopProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(products[position])

        Glide.with(holder.itemView.context)
            .load(productImg)
            .into(holder.binding.productImage)
    }

    override fun getItemCount(): Int = products.size
}
