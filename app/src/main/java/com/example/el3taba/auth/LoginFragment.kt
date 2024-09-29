package com.example.el3taba.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
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
                performLogin(email, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.signupButton1.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        binding.signupButton2.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        binding.forgetPasswordButton.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}