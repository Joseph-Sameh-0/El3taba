package com.example.el3taba.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentSetNewPasswordBinding


class SetNewPasswordFragment : Fragment() {
    private var _binding: AuthFragmentSetNewPasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = AuthFragmentSetNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SubmitButton.setOnClickListener {
            // Retrieve the text from input fields
            val tempPassword = binding.tempPasswordEditText.text.toString()
            val newPassword = binding.newPasswordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Validate the inputs and check if passwords match
            if (validateInputs(tempPassword, newPassword, confirmPassword)) {
                // Proceed with password reset
                resetPassword(newPassword)
                findNavController().popBackStack(R.id.loginFragment, false)
            }
            else{
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Function to validate the inputs
    private fun validateInputs(tempPassword: String, newPassword: String, confirmPassword: String): Boolean {
        return when {
            tempPassword.isEmpty() -> {
                binding.newPasswordEditTextLayout.error = null
                binding.confirmPasswordEditTextLayout.error = null
                binding.tempPasswordEditTextLayout.error = "Temporary password is required"
                false
            }
            newPassword.isEmpty() -> {
                binding.tempPasswordEditTextLayout.error = null
                binding.confirmPasswordEditTextLayout.error = null
                binding.newPasswordEditTextLayout.error = "New password is required"
                false
            }
            confirmPassword.isEmpty() -> {
                binding.tempPasswordEditTextLayout.error = null
                binding.newPasswordEditTextLayout.error = null
                binding.confirmPasswordEditTextLayout.error = "Confirm password is required"
                false
            }
            newPassword.length < 8 -> {
                binding.tempPasswordEditTextLayout.error = null
                binding.confirmPasswordEditTextLayout.error = null
                binding.newPasswordEditTextLayout.error = "Password must be at least 8 characters"
                false
            }
            newPassword != confirmPassword -> {
                binding.tempPasswordEditTextLayout.error = null
                binding.newPasswordEditTextLayout.error = null
                binding.confirmPasswordEditTextLayout.error = "Passwords do not match"
                false
            }
            else -> {
                // Clear any previous error messages
                binding.tempPasswordEditTextLayout.error = null
                binding.newPasswordEditTextLayout.error = null
                binding.confirmPasswordEditTextLayout.error = null
                true
            }
        }
    }

    // Function to reset the password (mock function)
    private fun resetPassword(newPassword: String) {
        // Here you would typically call your backend API to reset the password
        // For demonstration purposes, we'll just show a toast message
        Toast.makeText(requireContext(), "Password reset successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to login screen or wherever appropriate
        // findNavController().navigate(R.id.action_setNewPasswordFragment_to_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}