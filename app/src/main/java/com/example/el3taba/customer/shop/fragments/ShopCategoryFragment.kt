package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.core.adapters.ShopCategoryAdapter
import com.example.el3taba.databinding.FragmentShopCategoryBinding
import com.example.el3taba.seller.myProducts.Category
import com.example.el3taba.seller.myProducts.MyProductsViewModel

class ShopCategoryFragment : Fragment() {

    private var _binding: FragmentShopCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: ShopCategoryAdapter

    private var categories: List<Category>? = null
    val productViewModel: MyProductsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopCategoryBinding.inflate(inflater, container, false)
//        lifecycleScope.launch {
//            productViewModel.getAllCategories().observe(viewLifecycleOwner) { _categories ->
//                categories = _categories
//                Log.d("Categories 3", categories.toString())
//            }
//            Log.d("Categories 2", categories.toString())
//        }
//        Log.d("Categories", categories.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        productViewModel.getAllCategories().observe(viewLifecycleOwner) { _categories ->
            categories = _categories
            categoryAdapter = ShopCategoryAdapter(categories ?: listOf()) { category ->
                val action = ShopCategoryFragmentDirections
                    .actionCategoryToSubCategory(category.name)
                findNavController().navigate(action)
//            findNavController().navigate(R.id.action_category_to_subCategory)
                // Handle category click event here
//            Toast.makeText(requireContext(), "Clicked: ${category.name}", Toast.LENGTH_SHORT).show()
            }
            binding.categoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = categoryAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
