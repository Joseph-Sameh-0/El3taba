package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.R
import com.example.el3taba.core.adapters.ShopItemAdapter
import com.example.el3taba.core.dataClasses.Product
import com.example.el3taba.databinding.FragmentShopItemListBinding

class ShopItemListFragment : Fragment() {

    private var _binding: FragmentShopItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var shopItemAdapter: ShopItemAdapter

    private val sampleProducts = listOf(
        Product("Laptop 1", "12$", "21$", 1F,R.drawable.phones_image),
        Product("Laptop 2", "20$", "40$", 2.5F,R.drawable.phones_image),
        Product("Laptop 3", "40$", "60$", 3.5F,R.drawable.phones_image),
        Product("Laptop 4 Laptop 4 Laptop 4 Laptop 4 Laptop 4 Laptop 4 Laptop 4 Laptop 4 Laptop 4 ", "14$", "21$", 5F,R.drawable.phones_image),
        Product("Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 Laptop 5 ", "14$", "21$", 2.5F,R.drawable.phones_image),
        Product("Laptop 6", "15$", null, 4.5F,R.drawable.phones_image),
        // Add more products...
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Navigate to ProductItemFragment when an item is clicked
        binding.product.setOnClickListener {
            findNavController().navigate(R.id.action_items_to_product)
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        shopItemAdapter = ShopItemAdapter(sampleProducts)

        binding.shopItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shopItemAdapter
        }
//        binding.shopItemRecyclerView.apply {
//            layoutManager = GridLayoutManager(requireContext(), 2)
//            adapter = shopItemAdapter
//        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
