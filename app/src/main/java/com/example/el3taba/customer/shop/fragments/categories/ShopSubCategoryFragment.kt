package com.example.el3taba.customer.shop.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.el3taba.R
import com.example.el3taba.core.GridSpacingItemDecoration
import com.example.el3taba.core.dataClasses.Category
import com.example.el3taba.databinding.FragmentShopSubCategoryBinding

class ShopSubCategoryFragment : Fragment() {

    private var _binding: FragmentShopSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var subCategoryAdapter: ShopCategoryAdapter
    private lateinit var categoryName: String


    private val categories = listOf(
        Category("Laptops", R.drawable.phones_image),
        Category("Phones", R.drawable.phones_image),
        Category("Cameras", R.drawable.phones_image),
        Category("Tablets", R.drawable.phones_image),
        Category("Airpods", R.drawable.phones_image),
        Category("Smart Watches", R.drawable.phones_image)
    )

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
        categoryName = arguments?.getString("categoryName").orEmpty()

        // Display the category name or load relevant subcategories
        binding.subcategoryTitle.text = categoryName

        // Setup RecyclerView
        subCategoryAdapter = ShopCategoryAdapter(categories) { category ->
//            val action = ShopCategoryFragmentDirections
//                .actionCategoryToSubCategory(category.name)
//            findNavController().navigate(action)
            findNavController().navigate(R.id.action_subCategory_to_items)

            // Handle category click event here
            Toast.makeText(requireContext(), "Clicked: ${category.name}", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
