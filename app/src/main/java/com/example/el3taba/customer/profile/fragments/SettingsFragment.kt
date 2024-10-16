package com.example.el3taba.customer.profile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.el3taba.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle change password button click
        binding.changePassword.setOnClickListener {
            val changePasswordBottomSheet = ChangePasswordBottomSheet()
            changePasswordBottomSheet.show(childFragmentManager, "ChangePassword")
        }

        // Handle saving settings
        binding.saveButton.setOnClickListener {
            // Function to save user settings and notifications to Firebase
            saveUserSettings()
        }

        binding.backButtonSettings.setOnClickListener{
        findNavController().navigateUp()
        }

        // Get data from Firebase (mock function)
        getUserDataFromFirebase()
    }

    private fun saveUserSettings() {
        // Implement saving logic to Firebase here
        Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
    private fun getUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the user document in Firestore
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Retrieve user data from the document
                    val name = document.getString("fullName") ?: "No name available"
                    val phone = document.getString("phoneNumber") ?: "No name available"
                    binding.fullNameInput.setText(name)
                    binding.phoneNumberInput.setText(phone)

                } else {
                    // Document does not exist
                    binding.fullNameInput.setText("User data not found")
                    binding.phoneNumberInput.setText("User data not found")

                }
            }.addOnFailureListener {

            }

    }
    @SuppressLint("SetTextI18n")
    private fun getUserDataFromFirebase() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // User is logged in, fetch user data
            getUserData(currentUser.uid)
        }        // Fetch user data from Firebase and populate the UI
        //binding.fullNameInput.setText("Sample User")
        binding.passwordInput.setText("********")
        // Load notification preferences...
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}