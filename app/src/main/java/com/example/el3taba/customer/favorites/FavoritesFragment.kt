package com.example.el3taba.customer.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.customer.home.ProductAdapter
import com.example.el3taba.customer.home.SubHomeFragmentDirections
import com.example.el3taba.databinding.CustomerFragmentFavoritesBinding
import com.example.el3taba.seller.myProducts.MyProductsViewModel


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

        var viewModel = ViewModelProvider(this)[MyProductsViewModel::class.java]

        viewModel.getRandom10Products().observe(viewLifecycleOwner) { products -> //////////////////////edit
            favoritesAdapter = ProductAdapter(products) { product ->
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

}
