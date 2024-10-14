package com.example.el3taba.customer.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        dismiss()
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
