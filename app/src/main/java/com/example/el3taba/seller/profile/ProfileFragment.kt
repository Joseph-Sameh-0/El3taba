package com.example.el3taba.seller.profile

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.el3taba.databinding.SellerFragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: SellerFragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Retrieve the navigation arguments
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SellerFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sellerName = args.sellerName
        val sellerRating = args.sellerRating
        val sellerBio = args.sellerBio

        // Set these values to the respective views
        binding.sellerName.text = sellerName
        binding.sellerRatingBar.rating = sellerRating
        binding.sellerBio.text = sellerBio

        // Logout functionality
        binding.logoutButton.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("user_session", AppCompatActivity.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }

        // Set the listener for the edit button
        binding.editProfileButton.setOnClickListener {
            // Ensure that the navigation action is correct
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                sellerName = sellerName,
                sellerRating = sellerRating,
                sellerBio = sellerBio
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
