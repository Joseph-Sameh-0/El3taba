package com.example.el3taba.core.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.databinding.ItemBagBinding

class BagItemsAdapter(
    private val productList: MutableList<Map<FinalProduct, Int>>,
    private val onItemMenuClick: (FinalProduct, View) -> Unit
) : RecyclerView.Adapter<BagItemsAdapter.BagItemViewHolder>() {

//    private val items = mutableListOf<FinalProduct>()

//    var productImg: String? = null

//    @SuppressLint("NotifyDataSetChanged")
//    fun submitList(newItems: List<FinalProduct>) {
//        productList.clear()
//        productList.addAll(newItems)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagItemViewHolder {
        val binding = ItemBagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BagItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BagItemViewHolder, position: Int) {
        holder.bind(productList[position].keys.first())
        holder.binding.itemQuantity.text = productList[position].values.first().toString()

//        Glide.with(holder.itemView.context)
//            .load(productImg)
//            .into(holder.binding.itemImage)
    }

    override fun getItemCount(): Int = productList.size

    inner class BagItemViewHolder(val binding: ItemBagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: FinalProduct) {
            binding.itemName.text = item.name
            binding.itemPrice.text = "${item.price}$"
//            productImg = item.imageUrls[0]
            // Load image using an image loader like Glide or Coil
            Glide.with(binding.itemImage.context).load(item.imageUrls[0]).into(binding.itemImage)

            binding.moreOptionsButton.setOnClickListener {
                onItemMenuClick(item, it)
            }
        }
    }
}
