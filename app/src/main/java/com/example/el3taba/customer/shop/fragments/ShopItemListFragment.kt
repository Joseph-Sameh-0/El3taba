package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.R
import com.example.el3taba.core.adapters.ShopItemAdapter
import com.example.el3taba.core.dataClasses.Product
import com.example.el3taba.databinding.FragmentShopItemListBinding
import com.example.el3taba.seller.myProducts.Category
import com.example.el3taba.seller.myProducts.MyProductsViewModel

class ShopItemListFragment : Fragment() {

    private var _binding: FragmentShopItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var shopItemAdapter: ShopItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getParcelable<Category>("category") ?: Category()
        val subCategory = arguments?.getParcelable<Category>("subCategory") ?: Category()
//        binding.subCategoryTitle.text = subCategoryName

        val productViewModel: MyProductsViewModel by viewModels()

        productViewModel.getProducts(subCategory.id, category.id).observe(viewLifecycleOwner) { products ->
            Log.d("products",products.toString())
            shopItemAdapter = ShopItemAdapter(products) { product ->
                val action = ShopItemListFragmentDirections
                    .actionItemsToProduct(product.id)
                findNavController().navigate(action)
            }
            binding.shopItemRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = shopItemAdapter
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
