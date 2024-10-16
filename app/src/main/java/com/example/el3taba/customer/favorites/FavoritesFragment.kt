package com.example.el3taba.customer.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.el3taba.R
import com.example.el3taba.customer.home.Product
import com.example.el3taba.customer.home.ProductAdapter
import com.example.el3taba.customer.home.SubHomeFragmentDirections
import com.example.el3taba.databinding.CustomerFragmentFavoritesBinding


class FavoritesFragment : Fragment() {

    private lateinit var binding: CustomerFragmentFavoritesBinding
    private lateinit var favoritesAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomerFragmentFavoritesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupButtons()

        return binding.root
    }

    private fun setupRecyclerView() {
        // Setup RecyclerView with a GridLayoutManager (2 columns)
        favoritesAdapter = ProductAdapter(getDummyFavorites(10)) { product ->
            try {
                val action = SubHomeFragmentDirections
                    .actionSubHomeFragmentToProductItemFragment2(product.id)
                findNavController().navigate(action)
            } catch (e: Exception) {
                // Log the error if navigation fails
                e.printStackTrace()
            }
        }
        binding.recyclerViewFavorites.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoritesAdapter
        }
    }


    private fun setupButtons() {
        // Filter button click listener
        binding.btnFilter.setOnClickListener {
            // Implement filter action
        }

        // Sort button click listener
        binding.btnSort.setOnClickListener {
            // Toggle sorting between lowest to highest and highest to lowest
        }
    }

    private fun getDummyFavorites(count: Int): List<Product> {
        val products = mutableListOf<Product>()
        for (i in 1..count) {
            products.add(Product("Product $i", "$${i * 10}","Price $i", R.drawable.ic_bag))
        }
        return products
    }
}
