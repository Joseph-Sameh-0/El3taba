package com.example.el3taba.customer.shop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.el3taba.databinding.BottomSheetWriteReviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewBottomSheetFragment() : BottomSheetDialogFragment() {

    private var _binding: BottomSheetWriteReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var productID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWriteReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productID = arguments?.getString("product_id").toString()

        // Set click listener for the send button
        binding.sendReviewButton.setOnClickListener {
            val rating = binding.ratingBar.rating
            val reviewText = binding.reviewEditText.text.toString()

            // Handle sending the review data to Firebase or your backend
            sendReviewToFirebase(productID, rating, reviewText)

            // Dismiss the BottomSheet after submission
            dismiss()
        }
    }

    private fun sendReviewToFirebase(productID: String, rating: Float, reviewText: String) {
        // Logic to send the review to Firebase or another backend
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Function to create a new instance and pass parameters
        fun newInstance(productID: String): ReviewBottomSheetFragment {
            val fragment = ReviewBottomSheetFragment()
            val args = Bundle()
            args.putString("product_id", productID)
            fragment.arguments = args
            return fragment
        }
    }

}
