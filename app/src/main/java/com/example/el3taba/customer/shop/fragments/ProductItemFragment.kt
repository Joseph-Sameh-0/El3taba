package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.FragmentProductItemBinding

class ProductItemFragment : Fragment() {

    private var _binding: FragmentProductItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to RatingsReviewsFragment when the reviews button is clicked
        binding.reviews.setOnClickListener {
            findNavController().navigate(R.id.action_product_to_reviews)
        }

        // Navigate back when the back button is clicked
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
