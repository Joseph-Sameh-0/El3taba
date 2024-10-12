package com.example.el3taba.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.el3taba.databinding.ItemCategoryBinding
import com.example.el3taba.seller.myProducts.Category

class ShopCategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<ShopCategoryAdapter.ShopCategoryViewHolder>() {

    inner class ShopCategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.categoryTitle.text = category.name
            binding.root.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopCategoryViewHolder, position: Int) {
        holder.bind(categories[position])

        Glide.with(holder.itemView.context)
            .load(categories[position].imageUrl)
            .into(holder.binding.categoryImage)
    }

    override fun getItemCount(): Int = categories.size
}
