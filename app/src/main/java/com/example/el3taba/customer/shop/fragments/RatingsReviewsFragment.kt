package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.databinding.FragmentRatingsReviewsBinding

class RatingsReviewsFragment : Fragment() {
    private var _binding: FragmentRatingsReviewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingsReviewsBinding.inflate(inflater, container, false)

        // Back button logic
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Write review button logic
        binding.writeReviewButton.setOnClickListener {
            showReviewBottomSheet("")
        }

        return binding.root
    }

    private fun showReviewBottomSheet(productID: String) {
        val bottomSheetFragment = ReviewBottomSheetFragment.newInstance(productID)
        bottomSheetFragment.show(childFragmentManager, "ReviewBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
