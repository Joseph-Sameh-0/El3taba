package com.example.el3taba.seller.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.el3taba.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    // Retrieve arguments passed from ProfileFragment
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate the fields with the current profile data
        binding.editTextSellerName.setText(args.sellerName)
        binding.ratingBarSellerRating.rating = args.sellerRating
        binding.editTextSellerBio.setText(args.sellerBio)

        // Save button functionality
        binding.buttonSave.setOnClickListener {
            val updatedName = binding.editTextSellerName.text.toString()
            val updatedRating = binding.ratingBarSellerRating.rating
            val updatedBio = binding.editTextSellerBio.text.toString()

            // Here you can send updated values back to ProfileFragment or a ViewModel for persistence
            // Navigation or logic to handle updated profile data goes here

            // Example: Pop back to ProfileFragment (you can also pass updated arguments back)
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
