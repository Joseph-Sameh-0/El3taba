package com.example.el3taba.customer.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.databinding.FragmentContactUsBinding

class ContactUsFragment : Fragment() {
    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButtonContactUs.setOnClickListener {
            findNavController().navigateUp()
        }
        // Fetch initial data from Firebase (without implementation)
        fetchUserData()
        fetchAddress()

        // Set click listener for the "Change" address button
        binding.changeAddressButton.setOnClickListener {
            // Navigate to the "Choose Address" fragment
            // (You'll implement navigation and address selection later)
        }

        // Set click listener for the "Send" button
        binding.sendButton.setOnClickListener {
            // Logic to send the contact message to Firebase (without implementation)
            sendContactMessage()
        }
    }

    private fun fetchUserData() {
        // Fetch full name, contact number from Firebase (without implementation)
    }

    private fun fetchAddress() {
        // Fetch address from Firebase (without implementation)
    }

    private fun sendContactMessage() {
        // Logic to send the contact message (without implementation)
        // You can retrieve data from EditTexts using:
        val fullName = binding.fullNameEditText.text.toString()
        val contactNumber = binding.contactNumberEditText.text.toString()
        val message = binding.messageEditText.text.toString()

        Toast.makeText(requireContext(), "Contact Message Sent", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}