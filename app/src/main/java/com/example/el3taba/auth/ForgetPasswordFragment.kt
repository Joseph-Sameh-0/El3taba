package com.example.el3taba.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.databinding.AuthFragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : Fragment() {
    private var _binding: AuthFragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Send button click
        binding.sendButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()

            if (validateEmail(email)) {
                // Use Firebase to send password reset email
                sendPasswordResetEmail(email)
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

    // Function to send password reset email using Firebase
    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                    // Navigate to Login screen after the email is sent
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "Failed to send reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
