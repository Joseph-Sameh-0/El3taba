package com.example.el3taba.customer.bag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.R
import com.example.el3taba.core.adapters.BagItemsAdapter
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.databinding.BagFragmentBagListItemsBinding
import com.example.el3taba.seller.myProducts.MyProductsViewModel

class BagListItemsFragment : Fragment() {
    private var _binding: BagFragmentBagListItemsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BagItemsAdapter // RecyclerView adapter for the bag items

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = BagFragmentBagListItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch data from Firebase (without implementation)
        fetchBagItems()
        calculateTotalAmount()

        // Navigate to ShopItemListFragment when the subcategory button is clicked
        binding.checkout.setOnClickListener {
            findNavController().navigate(R.id.checkOutFragment)
        }
    }

    // Placeholder function to fetch bag items from Firebase
    private fun fetchBagItems() {
        // Fetch bag items and update the adapter (without implementation)
        // For example:
        // val bagItems = getBagItemsFromFirebase()
        // adapter.submitList(bagItems)
        Log.d("1", "1")
        binding.bagItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("1", "2")
//        adapter = BagItemsAdapter { item, view -> showPopupMenu(item, view) }
//        binding.bagItemsRecyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this)[MyProductsViewModel::class.java]

        Log.d("1", "3")
        viewModel.getBagProducts()
            .observe(viewLifecycleOwner) { products ->
                Log.d("bag_products", products.toString())
                Log.d("1", "4")
                adapter = BagItemsAdapter(products) { item, view ->
                    showPopupMenu(item, view)
                }
                Log.d("1", "5")

                binding.bagItemsRecyclerView.adapter = adapter
                Log.d("1", "6")
            }
    }

    // Placeholder function to calculate total amount
    private fun calculateTotalAmount() {
        // Calculate total amount based on items in the bag (without implementation)
        // For example:
        // val totalAmount = calculateTotal()
        // binding.totalPrice.text = "$${totalAmount}"
    }

    // Show the popup menu for each item in the bag (Add to favorites, Delete from list)
    private fun showPopupMenu(item: FinalProduct, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.bag_item_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_to_favorites -> {
                    // Add to favorites logic (without implementation)
                    true
                }

                R.id.delete_from_list -> {
                    // Delete item from the list logic (without implementation)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}