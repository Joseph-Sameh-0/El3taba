package com.example.el3taba.customer.shop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.core.adapters.ImagePagerAdapter
import com.example.el3taba.databinding.FragmentProductItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProductItemFragment : Fragment() {

    private var _binding: FragmentProductItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var Product: String
    private lateinit var viewPagerAdapter: ImagePagerAdapter
    private val imageUrls = mutableListOf<String>(
        "https://m.media-amazon.com/images/I/71QmKIgj+BL._AC_SL1500_.jpg",
        "https://m.media-amazon.com/images/I/71QS1tJQ9IL._AC_SL1500_.jpg",
        "https://m.media-amazon.com/images/I/71oofzP5DlL._AC_SL1500_.jpg",
        "https://m.media-amazon.com/images/I/61Lt8Th+ZUL._AC_SL1500_.jpg",
        "https://m.media-amazon.com/images/I/71lMSVCVnwL._AC_SL1500_.jpg",
    ) // List to hold image URLs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        categoryName = arguments?.getString("categoryName").orEmpty()

        Product = arguments?.getString("productName") ?: ""

        // Initialize ViewPager2 and its adapter
        viewPagerAdapter = ImagePagerAdapter(imageUrls)
        binding.productImagePager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.productImagePager)

        // Load product details from Firebase
//        loadProductDetails()

        // Handle 'Add to Bag' Button click
        binding.addToBagButton.setOnClickListener {
            // Logic to add the product to the shopping bag
        }

        // Navigate to RatingsReviewsFragment when the reviews button is clicked
        binding.ratingAndReviews.setOnClickListener {
            findNavController().navigate(R.id.action_product_to_reviews)
        }

        // Navigate back when the back button is clicked
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun loadProductDetails() {
        val db = FirebaseFirestore.getInstance()
        val productId = arguments?.getString("productId") // Get product ID

        db.collection("products").document(productId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.productTitle.text = document.getString("title")
                    binding.productDescription.text = document.getString("description")
                    binding.productPrice.text = "$${document.getDouble("price")}"

                    // Load image URLs
                    val images = document.get("images") as List<String>
                    imageUrls.clear()
                    imageUrls.addAll(images)
                    viewPagerAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error fetching product details", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
