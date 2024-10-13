package com.example.el3taba.auth

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private var _binding: AuthFragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var isPasswordVisible = false // Track the password visibility state

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clien_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (validateEmail(email)) {
                    performLogin(email, password)
                } else {
                    binding.emailEditTextLayout.error = "Not a valid email address."
                }
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imageButton.setOnClickListener {
            togglePasswordVisibility()
        }
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.googlebtn.setOnClickListener {
            signInWithGoogle()
        }
    }
    private fun togglePasswordVisibility() {
        // Toggle the password visibility state
        isPasswordVisible = !isPasswordVisible

        if (isPasswordVisible) {
            binding.passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.imageButton.setImageResource(R.drawable.ic_eye) // Show the password icon
        } else {
            binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.imageButton.setImageResource(R.drawable.ic_eye_slash) // Hide the password icon
        }

        // Move the cursor to the end of the password text
        binding.passwordEditText.text?.let { binding.passwordEditText.setSelection(it.length) }
    }
    private fun performLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        if (it.isEmailVerified) {
                            // Email is verified, proceed with login
                            val uid = it.uid
                            FirebaseFirestore.getInstance().collection("users").document(uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document != null && document.exists()) {
                                        val userRole = document.getString("role")
                                        saveUserSession(userRole.toString() ?: "")
                                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()

                                        // Reload the activity
                                        requireActivity().recreate()
                                    } else {
                                        Toast.makeText(requireContext(), "Failed to retrieve user role", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "Error getting user role", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // Email is not verified, prompt the user
                            Toast.makeText(requireContext(), "Please verify your email before logging in", Toast.LENGTH_LONG).show()
                            showResendVerificationDialog() // Show option to resend verification email
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.w("LoginFragment", "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val uid = it.uid
                        val email = it.email ?: ""  // Fetch user email if needed

                        // Reference to Firestore collection
                        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(uid)

                        // Check if the user role exists in Firestore
                        userDocRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val userRole = document.getString("role")
                                    if (userRole.isNullOrEmpty()) {
                                        // Role does not exist, set it to "customer"
                                        saveUserRole(uid, email, "customer")
                                    } else {
                                        // Role exists, save it in session
                                        saveUserSession(userRole)
                                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    // Document doesn't exist, save user with role "customer"
                                    saveUserRole(uid, email, "customer")
                                }
                                requireActivity().recreate()
                            }
                            .addOnFailureListener { exception ->
                                Log.w("firebaseAuthWithGoogle", "Error fetching user role", exception)
                                Toast.makeText(requireContext(), "Error fetching user role", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Google sign-in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserRole(uid: String, email: String, role: String) {
        val user = hashMapOf(
            "email" to email,
            "role" to role
        )

        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                saveUserSession(role)
                Toast.makeText(requireContext(), "User role saved as $role", Toast.LENGTH_SHORT).show()

                // Optionally, reload the activity or navigate
                requireActivity().recreate()
            }
            .addOnFailureListener { exception ->
                Log.w("saveUserRole", "Error saving user role", exception)
                Toast.makeText(requireContext(), "Failed to save user role", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveUserSession(userRole: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            putString("userRole", userRole)
            apply()
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showResendVerificationDialog() {
        val user = auth.currentUser
        user?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Email Verification Required")
            builder.setMessage("Your email is not verified. Would you like to resend the verification email?")
            builder.setPositiveButton("Resend") { _, _ ->
                resendVerificationEmail()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }

    private fun resendVerificationEmail() {
        val user = auth.currentUser
        user?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Verification email sent", Toast.LENGTH_SHORT).show()

                        // After sending the verification email, sign out the user
                        auth.signOut()
                    } else {
                        Toast.makeText(requireContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
