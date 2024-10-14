package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.el3taba.core.GridSpacingItemDecoration
import com.example.el3taba.core.adapters.ShopCategoryAdapter
import com.example.el3taba.databinding.FragmentShopSubCategoryBinding
import com.example.el3taba.seller.myProducts.Category
import com.example.el3taba.seller.myProducts.MyProductsViewModel
import com.example.el3taba.seller.myProducts.Subcategory

class ShopSubCategoryFragment : Fragment() {

    private var _binding: FragmentShopSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var subCategoryAdapter: ShopCategoryAdapter
    private lateinit var category: Category


    private lateinit var subCategories: List<Category>
    val productViewModel: MyProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSubCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the category name passed from ShopCategoryFragment
        category = arguments?.getParcelable<Category>("category") ?: Category()

        // Display the category name or load relevant subcategories
        binding.subcategoryTitle.text = category.name

        productViewModel.getSubcategories(category.id)
            .observe(viewLifecycleOwner) { _subCategories ->
                subCategories = _subCategories

                for (c in subCategories)
                {
                    Log.d("Subcategory", c.name)
                }
                // Setup RecyclerView
                subCategoryAdapter = ShopCategoryAdapter(subCategories) { category ->
                    val action = ShopSubCategoryFragmentDirections
                        .actionSubCategoryToItems(category.name)
                    findNavController().navigate(action)
//            findNavController().navigate(R.id.action_subCategory_to_items)

                    // Handle category click event here
//            Toast.makeText(requireContext(), "Clicked: ${category.name}", Toast.LENGTH_SHORT).show()
                }

// Adding spacing between items using GridSpacingItemDecoration
                val spanCount = 2 // Number of columns
                val spacing = 40 // 20px space between items
                val includeEdge = false
                binding.subCategoryRecyclerView.apply {
                    layoutManager = GridLayoutManager(requireContext(), spanCount)
                    addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
                    adapter = subCategoryAdapter
                }
                binding.backButton.setOnClickListener {
                    findNavController().popBackStack()
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
