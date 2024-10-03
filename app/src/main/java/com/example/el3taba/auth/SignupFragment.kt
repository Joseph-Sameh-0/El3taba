package com.example.el3taba.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentSignupBinding
import com.example.el3taba.databinding.CustomerFragmentBagBinding

class SignupFragment : Fragment() {
    lateinit var binding: AuthFragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AuthFragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.SignupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Here you would sign up the user with Firebase or your sign-up logic
                    signUpUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle the "Already have an account? Sign In" click (navigate to LoginFragment)
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun signUpUser(email: String, password: String) {
        // Add your sign-up logic here, for example, Firebase Authentication or API call.
        // After successful sign-up, navigate to the main activity or home page

        // Example: Successful sign-up (this is just a placeholder)
        Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show()

        // Navigate to the login screen after sign-up or directly to the home screen
        // Example: findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
    }
}