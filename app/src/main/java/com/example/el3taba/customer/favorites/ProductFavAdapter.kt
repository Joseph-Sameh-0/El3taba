package com.example.el3taba.customer.favorites

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.databinding.FragmentItemProductBinding

class ProductFavAdapter(
    private val productList: MutableList<FinalProduct>,
    private val onItemClick: (FinalProduct) -> Unit,
    private val onRemoveFromFavClick: (
        categoryId: String,
        subcategoryId: String,
        productId: String
    ) -> Unit
) :
    RecyclerView.Adapter<ProductFavAdapter.ProductViewHolder>() {

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
            .load(productList[position].imageUrl)
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
            binding.removeFromFavButton.setOnClickListener {
                Log.d("product.categoryID", product.category)
                Log.d("product.subcategoryID", product.subcategory)
                Log.d("product.id", product.id)
                onRemoveFromFavClick(
                    product.category,
                    product.subcategory,
                    product.id
                )
                productList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyItemRangeChanged(adapterPosition, productList.size)
            }
            binding.removeFromFavButton.visibility = View.VISIBLE
        }
    }
}