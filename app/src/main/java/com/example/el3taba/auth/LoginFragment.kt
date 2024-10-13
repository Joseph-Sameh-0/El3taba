package com.example.el3taba.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: AuthFragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            Log.d("LoginFragment", "loginButton clicked")
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                if (validateEmail(email)) {
                    binding.emailEditTextLayout.isErrorEnabled = false
                    binding.emailEditTextLayout.error

                    if (password.length < 8) {
                        binding.passwordEditTextLayout.error =
                            "Password must be at least 8 characters"
                    } else {
                        binding.passwordEditTextLayout.isErrorEnabled = false
                        // Here you would login the user with Firebase or your sign-up logic
                        performLogin(email, password)
                    }
                } else {
                    // Show email error
                    binding.emailEditTextLayout.error =
                        "Not a valid email address. Should be your@email.com"
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        binding.signupButton2.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }
    }

    private fun performLogin(email: String, password: String) {
        Log.d("LoginFragment", "in performLogin")
        val userRole = getRoleFromBackend(email)

        val sharedPreferences =
            requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            putString("userRole", userRole)
            apply()
            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
        }

        // Instead of starting an activity, communicate with the host activity
        // using an interface or by sending data through the arguments

        activity?.recreate()
    }

    private fun getRoleFromBackend(email: String): String {
        Log.d("LoginFragment", "in getRoleFromBackend")
        return when (email) {
            "customer@example.com" -> "customer"
            "seller@example.com" -> "seller"
            "admin@example.com" -> "admin"
            else -> "customer"
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}