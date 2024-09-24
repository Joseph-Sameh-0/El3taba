package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.FragmentShopSubCategoryBinding

class ShopSubCategoryFragment : Fragment() {

    private var _binding: FragmentShopSubCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopSubCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to ShopItemListFragment when the subcategory button is clicked
        binding.itemList.setOnClickListener {
            findNavController().navigate(R.id.action_subCategory_to_items)
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
