package com.example.el3taba.customer.profile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle change password button click
        binding.changePassword.setOnClickListener {
            val changePasswordBottomSheet = ChangePasswordBottomSheet()
            changePasswordBottomSheet.show(childFragmentManager, "ChangePassword")
        }

        // Handle saving settings
        binding.saveButton.setOnClickListener {
            // Function to save user settings and notifications to Firebase
            saveUserSettings()
        }

        binding.backButtonSettings.setOnClickListener{
        findNavController().navigateUp()
        }

        // Get data from Firebase (mock function)
        getUserDataFromFirebase()
    }

    private fun saveUserSettings() {
        // Implement saving logic to Firebase here
        Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    @SuppressLint("SetTextI18n")
    private fun getUserDataFromFirebase() {
        // Fetch user data from Firebase and populate the UI
        binding.fullNameInput.setText("Sample User")
        binding.dateOfBirthInput.setText("12/12/1989")
        binding.passwordInput.setText("********")
        // Load notification preferences...
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}