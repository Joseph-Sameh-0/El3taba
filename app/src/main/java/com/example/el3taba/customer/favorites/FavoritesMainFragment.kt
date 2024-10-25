package com.example.el3taba.customer.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.el3taba.databinding.FragmentFavoritesMainBinding
import com.example.el3taba.seller.myProducts.MyProductsViewModel

class FavoritesMainFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesMainBinding
    private lateinit var favoritesAdapter: ProductFavAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesMainBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupButtons()

        return binding.root
    }

    private fun setupRecyclerView() {
        // Setup RecyclerView with a GridLayoutManager (2 columns)

        val viewModel = ViewModelProvider(this)[MyProductsViewModel::class.java]

        viewModel.getFavProducts()
            .observe(viewLifecycleOwner) { products ->
                Log.d("products", products.toString())
                favoritesAdapter = ProductFavAdapter(
                    products,
                    onItemClick = { product ->
                        try {
                            val action = FavoritesMainFragmentDirections
                                .actionFavoritesMainFragmentToProductItemFragment3(product.id)
                            findNavController().navigate(action)
                        } catch (e: Exception) {
                            // Log the error if navigation fails
                            e.printStackTrace()
                        }
                    },
                    onRemoveFromFavClick = { categoryId, subcategoryId, productId ->
                        viewModel.removeProductFromFavorites(categoryId, subcategoryId, productId)
                    },
                )
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