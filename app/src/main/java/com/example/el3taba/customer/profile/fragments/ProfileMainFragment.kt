package com.example.el3taba.customer.profile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.FragmentProfileMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileMainFragment : Fragment() {
    //binding
    private var _binding: FragmentProfileMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load user data from Firebase
        loadUserData()

        // Handle click events
        binding.myOrdersButton.setOnClickListener {
            navigateToOrders()
        }

        binding.shippingAddressesButton.setOnClickListener {
            navigateToShippingAddresses()
        }

        binding.settingsButton.setOnClickListener {
            navigateToSettings()
        }

        binding.contactUsButton.setOnClickListener {
            navigateToContactUs()
        }

        binding.logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun getUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the user document in Firestore
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Retrieve user data from the document
                    val email = document.getString("email") ?: "No email available"
                    val name = document.getString("fullName") ?: "No name available"
                    val ordersCount = document.getLong("ordersCount") ?: 0
                    val addressesCount = document.getLong("addressesCount") ?: 0

                    // Update UI with the retrieved data
                    binding.profileName.text = name
                    binding.profileEmail.text = email
                    binding.myOrdersDescription.text = "You have $ordersCount orders"
                    binding.shippingAddressesDescription.text = "$addressesCount addresses"
                } else {
                    // Document does not exist
                    binding.profileName.text = "User data not found"
                    binding.profileEmail.text = "No email available"
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                binding.profileName.text = "Error fetching user data"
                binding.profileEmail.text = "Error: ${exception.message}"
            }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        // Fetch user data from Firebase
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // User is logged in, fetch user data
            getUserData(currentUser.uid)
        } else {
            // User is not logged in
            binding.profileName.text = "User not logged in"
            binding.profileEmail.text = "No email available"
            binding.myOrdersDescription.text = "You have 0 orders"
            binding.shippingAddressesDescription.text = "0 addresses"
        }
    }

    private fun navigateToOrders() {
        findNavController().navigate(R.id.myOrdersFragment)
    }

    private fun navigateToShippingAddresses() {
        // Navigate to Shipping Addresses fragment
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.settingsFragment)
    }

    private fun navigateToContactUs() {
        // Navigate to Contact Us fragment
    }

    private fun performLogout() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        FirebaseAuth.getInstance().signOut()

        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
