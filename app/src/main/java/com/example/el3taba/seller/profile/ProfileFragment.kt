package com.example.el3taba.seller.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.el3taba.R
import com.example.el3taba.databinding.SellerFragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: SellerFragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Constant values for seller name and email
    private val sellerName = "Beshoy Asham"
    private val sellerEmail = "beshoyasham0120@gmail.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SellerFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the seller details to the respective views
        binding.profileName.text = sellerName
        binding.profileEmail.text = sellerEmail

        // Logout functionality
        binding.logoutButton1.setOnClickListener { _: View ->
            val sharedPreferences = requireActivity().getSharedPreferences("user_session", AppCompatActivity.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
