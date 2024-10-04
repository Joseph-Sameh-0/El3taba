package com.example.el3taba.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentForgetPasswordBinding
import com.google.android.material.textfield.TextInputLayout


class ForgetPasswordFragment : Fragment() {
    private var _binding: AuthFragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AuthFragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Send button click
        binding.sendButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()

            if (validateEmail(email)) {
                // Call the function to send the temporary password
                sendTemporaryPassword(email)
            } else {
                // Show email error
                    binding.emailInputLayout.error = "Not a valid email address. Should be your@email.com"
            }
        }
    }

    // Function to validate email format
    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }


    // Function to send the temporary password (logic here to send email)
    private fun sendTemporaryPassword(email: String) {
        // Logic for sending the email with the temporary password
        val tempPassword = generateTemporaryPassword()
        sendGeneratedPasswordEmail(email, tempPassword)

        // You may want to navigate to the "Set New Password" screen here after sending the email
        val action = ForgetPasswordFragmentDirections.actionForgotPasswordFragmentToSetNewPasswordFragment(tempPassword)
        findNavController().navigate(action)

    }

    // Function to generate a random temporary password
    private fun generateTemporaryPassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }

    // Function to send the generated password to the user's email
    private fun sendGeneratedPasswordEmail(email: String, tempPassword: String) {
        // You need to use an email service (like SendGrid, Mailgun, or your own backend)
        // Example: Using your backend API to send the email
        Toast.makeText(requireContext(), "Temporary password sent to $email", Toast.LENGTH_SHORT).show()
    }

}