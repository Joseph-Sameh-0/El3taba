package com.example.el3taba.auth

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentSignupBinding
import com.example.el3taba.databinding.DialogRoleSelectionBinding
import com.google.android.material.textfield.TextInputLayout


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

                if (validateEmail(email)) {
                    binding.emailEditTextLayout.isErrorEnabled = false
                    binding.emailEditTextLayout.error

                    if (password.length < 8) {
                        binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                        binding.passwordEditTextLayout.error =
                            "Password must be at least 8 characters"
                    } else
                        if (password != confirmPassword) {
                            binding.passwordEditTextLayout.isErrorEnabled = false
                            binding.confirmPasswordEditTextLayout.error = "Passwords do not match"
                        } else {
                            binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                            binding.passwordEditTextLayout.isErrorEnabled = false
                            // Here you would sign up the user with Firebase or your sign-up logic
                            showRoleSelectionDialog(email, password)
                        }
                } else {
                    // Show email error
                        binding.emailEditTextLayout.error = "Not a valid email address. Should be your@email.com"
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Handle the "Already have an account? Sign In" click (navigate to LoginFragment)
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun signUpUser(email: String, password: String, role: String?) {
        // Add your sign-up logic here, for example, Firebase Authentication or API call.
        // After successful sign-up, navigate to the main activity or home page

        // Example: Successful sign-up (this is just a placeholder)
        Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show()

        // Navigate to the login screen after sign-up or directly to the home screen
        // Example: findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
    }

    private fun showRoleSelectionDialog(email: String, password: String) {
        // Use View Binding to inflate the dialog layout
        val binding = DialogRoleSelectionBinding.inflate(layoutInflater)

        // Build the AlertDialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)  // Set the root view of the binding


        var selectedRoleId: Int
        var selectedRole: String?
        binding.SignupButton.setOnClickListener() {
            // Handle role selection
            selectedRoleId = binding.roleRadioGroup.checkedRadioButtonId
            selectedRole = when (selectedRoleId) {
                R.id.customerRadioButton -> "Customer"
                R.id.sellerRadioButton -> "Seller"
                else -> null
            }

            if (selectedRole != null) {
                signUpUser(email, password, selectedRole)
            } else {
                Toast.makeText(requireContext(), "Please select a role", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }


}