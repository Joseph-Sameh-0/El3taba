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

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        // Fetch user data from Firebase
        // Set user's name, email, number of orders, shipping addresses count, etc.
        // Example:
        // binding.profileName.text = firebaseUser.displayName
        // binding.profileEmail.text = firebaseUser.email

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val email = currentUser!!.email.toString()
        val name = currentUser.displayName.toString()
        binding.profileName.text = name   /////////////////////////////////////////////////////////////////////
        binding.profileEmail.text = email

        binding.myOrdersDescription.text = "You have 0 orders"
        binding.shippingAddressesDescription.text = "0 addresses"
    }

    private fun navigateToOrders() {
        // Navigate to My Orders fragment
        findNavController().navigate(R.id.myOrdersFragment)
    }

    private fun navigateToShippingAddresses() {
        // Navigate to Shipping Addresses fragment
    }

    private fun navigateToSettings() {
        // Navigate to Settings fragment
        findNavController().navigate(R.id.settingsFragment)
    }

    private fun navigateToContactUs() {
        // Navigate to Contact Us fragment
    }

    private fun performLogout() {
        // Perform logout action with Firebase Authentication
        // Example: FirebaseAuth.getInstance().signOut()

        val sharedPreferences =
            requireActivity().getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}