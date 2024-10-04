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
    private var tempPassword: String? = null
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

        // Get the temporary password from the arguments
        tempPassword = arguments?.getString("tempPassword")

        binding.SubmitButton.setOnClickListener {
            // Retrieve the text from input fields
            val enteredTempPassword = binding.tempPasswordEditText.text.toString()
            val newPassword = binding.newPasswordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Validate the inputs and check if passwords match
            if (validateInputs(enteredTempPassword, newPassword, confirmPassword)) {
                // Proceed with password reset
                resetPassword(newPassword)
                findNavController().popBackStack(R.id.loginFragment, false)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Function to validate the inputs
    private fun validateInputs(
        enteredTempPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        return when {
            enteredTempPassword.isEmpty() -> {
                binding.newPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                binding.tempPasswordEditTextLayout.error = "Temporary password is required"
                false
            }

            newPassword.isEmpty() -> {
                binding.tempPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                binding.newPasswordEditTextLayout.error = "New password is required"
                false
            }

            confirmPassword.isEmpty() -> {
                binding.tempPasswordEditTextLayout.isErrorEnabled = false
                binding.newPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.error = "Confirm password is required"
                false
            }

            newPassword.length < 8 -> {
                binding.tempPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                binding.newPasswordEditTextLayout.error = "Password must be at least 8 characters"
                false
            }

            newPassword != confirmPassword -> {
                binding.tempPasswordEditTextLayout.isErrorEnabled = false
                binding.newPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.error = "Passwords do not match"
                false
            }

            (enteredTempPassword != tempPassword) -> {
                binding.newPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                binding.tempPasswordEditTextLayout.error = "Incorrect temporary password"
                false
            }

            else -> {
                // Clear any previous error messages
                binding.tempPasswordEditTextLayout.isErrorEnabled = false
                binding.newPasswordEditTextLayout.isErrorEnabled = false
                binding.confirmPasswordEditTextLayout.isErrorEnabled = false
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