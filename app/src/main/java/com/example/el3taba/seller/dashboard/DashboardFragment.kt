package com.example.el3taba.seller.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.el3taba.databinding.SellerFragmentDashboardBinding
import com.example.el3taba.seller.addProduct.db

class DashboardFragment : Fragment() {

    private var _binding: SellerFragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = SellerFragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Fetch the product count and update the first box in GridLayout
        fetchProductCount()

        // Handle navigation to ProfileFragment on button click
        binding.goToProfileButton.setOnClickListener {
            // Pass the arguments (sellerName, sellerRating, sellerBio)
            val action = DashboardFragmentDirections.actionDashboardFragmentToProfileFragment(
                sellerName = "John Doe",   // Replace with actual seller name
                sellerRating = 4.5f,       // Replace with actual seller rating
                sellerBio = "Top-rated seller with years of experience."  // Replace with actual bio
            )
            // Navigate to ProfileFragment with the passed arguments
            findNavController().navigate(action)
        }

        return root
    }

    private fun fetchProductCount() {
        val dashboardDoc = db.collection("dashboard").document("productCount")

        dashboardDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val productCount = documentSnapshot.getLong("count") ?: 0
                // Update the first box with the product count
                binding.stat1Number.text = "$productCount"
            } else {
                binding.stat1Number.text = "0"
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Error fetching product count: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
