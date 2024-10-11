package com.example.el3taba.auth

import com.example.el3taba.core.AuthViewModel
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.core.dataClasses.User
import com.example.el3taba.databinding.AuthFragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : Fragment() {
    private lateinit var binding: AuthFragmentSignupBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthFragmentSignupBinding.inflate(inflater, container, false)
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
                    if (password.length < 8) {
                        binding.passwordEditTextLayout.error = "Password must be at least 8 characters"
                    } else if (password != confirmPassword) {
                        binding.confirmPasswordEditTextLayout.error = "Passwords do not match"
                    } else {
                        binding.passwordEditTextLayout.isErrorEnabled = false
                        binding.confirmPasswordEditTextLayout.isErrorEnabled = false
                        // Directly sign up the user as a customer without showing the role selection dialog
                        signUpUser(email, password, "customer")
                    }
                } else {
                    binding.emailEditTextLayout.error = "Not a valid email address. Should be your@email.com"
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun signUpUser(email: String, password: String, role: String?) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        // Send verification email
                        it.sendEmailVerification()
                            .addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign Up Successful. Please check your email for verification",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Save user role in the database
                                    val newUser = User(email = email, role = role ?: "")
                                    authViewModel.saveUserRole(newUser).observe(viewLifecycleOwner, { saveSuccess ->
                                        if (saveSuccess) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Role saved successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Failed to save role",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })

                                    findNavController().navigate(R.id.loginFragment)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to send verification email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
