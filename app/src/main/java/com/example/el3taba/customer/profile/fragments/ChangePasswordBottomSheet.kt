package com.example.el3taba.customer.profile.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.el3taba.core.AuthViewModel
import com.example.el3taba.databinding.BottomSheetChangePasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChangePasswordBinding? = null
    private val binding get() = _binding!!
    lateinit var customerEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle saving new password
        binding.saveNewPasswordButton.setOnClickListener {
            // Implement logic to save new password in Firebase
            saveNewPassword()
        }
        binding.forgotPassword2.setOnClickListener {
            customerEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
            sendPasswordResetEmail(customerEmail)
        }
    }

    private fun saveNewPassword() {
        // Firebase password change logic goes here
        val authViewModel: AuthViewModel by viewModels()

        val currentPassword = binding.oldPasswordEditText.text.toString()
        val newPassword = binding.newPasswordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        if (newPassword.length < 8) {
            binding.newPasswordLayout.error = "Password must be at least 8 characters"
        } else if (newPassword != confirmPassword) {
            binding.confirmPasswordLayout.error = "Passwords do not match"
            return
        } else {
            binding.confirmPasswordLayout.isErrorEnabled = false
            authViewModel.changePassword(currentPassword, newPassword)
                .observe(this) { success ->
                    if (success) {
                        Log.d("MainActivity", "Password changed successfully")
                        dismiss()
                    } else {
                        binding.oldPasswordLayout.error = "The old password is incorrect"
                        Log.d("MainActivity", "Failed to change password")
                    }
                }
        }

    }

    // Function to send password reset email using Firebase
    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Password reset email sent to $email",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to Login screen after the email is sent
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to send reset email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dismiss()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
