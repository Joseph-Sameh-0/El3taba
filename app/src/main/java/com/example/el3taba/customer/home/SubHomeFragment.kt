package com.example.el3taba.customer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.databinding.FragmentSubHomeBinding
import com.example.el3taba.seller.myProducts.MyProductsViewModel

class SubHomeFragment : Fragment() {

    private lateinit var binding: FragmentSubHomeBinding
    private lateinit var recommendedAdapter: ProductAdapter
    private lateinit var newItemsAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubHomeBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        return binding.root
    }

    private fun setupRecyclerViews() {
        // Setup Recommended RecyclerView
        var viewModel = ViewModelProvider(this)[MyProductsViewModel::class.java]

        viewModel.getRandom10Products().observe(viewLifecycleOwner) { products ->

            recommendedAdapter = ProductAdapter(products) { product ->
                val action = SubHomeFragmentDirections
                    .actionSubHomeFragmentToProductItemFragment2(product.id)
                findNavController().navigate(action)
            } // 10 items
            binding.recommendedRecyclerview.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = recommendedAdapter
            }
        }

        viewModel.getRandom10Products().observe(viewLifecycleOwner) { products ->
            // Setup New Items RecyclerView
            newItemsAdapter = ProductAdapter(products) { product ->
                val action = SubHomeFragmentDirections
                    .actionSubHomeFragmentToProductItemFragment2(product.id)
                findNavController().navigate(action)
            } // 10 items
            binding.newRecyclerview.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = newItemsAdapter
            }
        }

    }

//    private fun Int.getDummyProducts(): List<Product> {
//        val products = mutableListOf<Product>()
//        for (i in 1..this) {
//            products.add(Product(i.toString(), "Product $i", "$${i * 10}", R.drawable.phones_image))
//        }
//        return products
//    }
}
