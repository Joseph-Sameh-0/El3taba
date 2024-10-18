package com.example.el3taba.customer.shop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.core.adapters.ImagePagerAdapter
import com.example.el3taba.databinding.FragmentProductItemBinding
import com.example.el3taba.seller.myProducts.MyProductsViewModel


class ProductItemFragment : Fragment() {

    private var _binding: FragmentProductItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var ProductID: String
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

        ProductID = arguments?.getString("productID") ?: ""
        val from = arguments?.getString("from") ?: ""
        Log.d("product ID", ProductID)

        // Initialize ViewPager2 and its adapter
        viewPagerAdapter = ImagePagerAdapter(imageUrls)
        binding.productImagePager.adapter = viewPagerAdapter
        binding.indicator.setViewPager(binding.productImagePager)


        // Load product details from Firebase
        loadProductDetails(ProductID)

        // Handle 'Add to Bag' Button click
        binding.addToBagButton.setOnClickListener {
            // Logic to add the product to the shopping bag
        }

        // Navigate to RatingsReviewsFragment when the reviews button is clicked
        binding.ratingAndReviews.setOnClickListener {
            if (from == "home") {
                findNavController().navigate(R.id.ratingsReviewsFragment2)
            } else if (from == "fav") {
                findNavController().navigate(R.id.ratingsReviewsFragment3)
            } else
                findNavController().navigate(R.id.action_product_to_reviews)
        }

        // Navigate back when the back button is clicked
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun loadProductDetails(ProductID: String) {
        val productViewModel: MyProductsViewModel by viewModels()

//
        productViewModel.getProductById(ProductID).observe(viewLifecycleOwner) { product ->
            if (product != null) {
                binding.productTitle.text = product.title
                binding.productDescription.text = product.description
                binding.productPrice.text = "$${product.price}"
                binding.sellerName.text = product.sellerName
                binding.productName.text = product.name
                binding.mfgDate.text = "Mfg Date: ${product.mfgDate}"
                binding.expDate.text = "Exp Date: ${product.expDate}"
                binding.state.text = "State: ${product.state}"
                binding.productRating.rating = product.avgRating
                binding.ratingNumber.text = "(${product.numberOfRatings})"
                binding.availablePieces.text = "Available Pieces: ${product.stock}"

                val specsTable = binding.specsTable
                for ((key, value) in product.specs) {
                    val row = TableRow(requireContext())
                    val specNameTextView = TextView(requireContext()).apply {
                        text = key
                        setPadding(8, 8, 8, 8)
                    }
                    val specValueTextView = TextView(requireContext()).apply {
                        text = value
                        setPadding(8, 8, 8, 8)
                    }
                    row.addView(specNameTextView)
                    row.addView(specValueTextView)
                    specsTable.addView(row)

                }
                binding.favoriteButton.setOnClickListener {
                    productViewModel.addProductToFavorites(
                        product.category,
                        product.subcategory,
                        product.id
                    ).observe(viewLifecycleOwner) { success ->
                        if (success)
                            Toast.makeText(
                                context,
                                "Product added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(context, "Product added already", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
                //            // Load image URLs
//            val images = product.get("images") as List<String>
//            imageUrls.clear()
//            imageUrls.addAll(images)
//            viewPagerAdapter.notifyDataSetChanged()
            } else {
                // Product not found or error occurred
                Toast.makeText(context, "Error fetching product details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
