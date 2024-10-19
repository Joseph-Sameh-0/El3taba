package com.example.el3taba.customer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.databinding.FragmentItemProductBinding

class ProductAdapter(
    private val productList: List<FinalProduct>,
    private val onItemClick: (FinalProduct) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = FragmentItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
        Glide.with(holder.itemView.context)
            .load(productList[position].imageUrls[0])
            .into(holder.binding.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(val binding: FragmentItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: FinalProduct) {
            binding.productName.text = product.name
            binding.productPrice.text = product.price.toString()
            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }
    }
}