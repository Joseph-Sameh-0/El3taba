package com.example.el3taba.seller.myProducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.el3taba.R
import com.example.el3taba.seller.myProducts.Product
import android.widget.ImageView
import com.bumptech.glide.Glide

class MyProductsAdapter(
    private val products: List<Product>,
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<MyProductsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productDescription: TextView = view.findViewById(R.id.productDescription)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        fun bind(product: Product) {
            productName.text = product.name
            productDescription.text = product.description
            productPrice.text = "$${product.price}"

            Glide.with(itemView.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder_image) // Add a placeholder image resource in your drawable folder
                .into(productImage)

            editButton.setOnClickListener { onEdit(product) }
            deleteButton.setOnClickListener { onDelete(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
