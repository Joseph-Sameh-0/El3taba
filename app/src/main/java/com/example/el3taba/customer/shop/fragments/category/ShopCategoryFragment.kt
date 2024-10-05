package com.example.el3taba.customer.shop.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.R
import com.example.el3taba.core.dataClasses.Category
import com.example.el3taba.databinding.FragmentShopCategoryBinding

class ShopCategoryFragment : Fragment() {

    private var _binding: FragmentShopCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: ShopCategoryAdapter

    private val categories = listOf(
        Category("Electronics", R.drawable.electronics_image),
        Category("Supermarket", R.drawable.electronics_image),
        Category("Clothes", R.drawable.electronics_image),
        Category("Pets", R.drawable.electronics_image)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        categoryAdapter = ShopCategoryAdapter(categories) { category ->
            findNavController().navigate(R.id.action_category_to_subCategory)
            // Handle category click event here
            Toast.makeText(requireContext(), "Clicked: ${category.name}", Toast.LENGTH_SHORT).show()
        }
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
        }

        // Navigate to SubCategoryFragment when the category button is clicked
        binding.subCategories.setOnClickListener {
            findNavController().navigate(R.id.action_category_to_subCategory)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
