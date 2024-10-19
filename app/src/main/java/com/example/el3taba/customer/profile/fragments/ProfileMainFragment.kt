package com.example.el3taba.customer.profile.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.el3taba.R
import com.example.el3taba.databinding.BottomSheetProfileOptionsBinding
import com.example.el3taba.databinding.FragmentProfileMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileMainFragment : Fragment() {
    //binding
    private var _binding: FragmentProfileMainBinding? = null
    private val binding get() = _binding!!

    private val pickImageRequestCode = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load user data from Firebase
        loadUserData()

        // Handle click events
        binding.myOrdersButton.setOnClickListener {
            navigateToOrders()
        }

        binding.shippingAddressesButton.setOnClickListener {
            navigateToShippingAddresses()
        }

        binding.settingsButton.setOnClickListener {
            navigateToSettings()
        }

        binding.contactUsButton.setOnClickListener {
            navigateToContactUs()
        }

        binding.logoutButton.setOnClickListener {
            performLogout()
        }

        binding.profileImage.setOnClickListener {
            // Show bottom sheet
            showBottomSheetDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the user document in Firestore
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Retrieve user data from the document
                    val email = document.getString("email") ?: "No email available"
                    val name = document.getString("fullName") ?: "No name available"
                    val ordersCount = document.getLong("ordersCount") ?: 0
                    val addressesCount = document.getLong("addressesCount") ?: 0
                    val imageUrl = document.getString("profileImage")

                    // Update UI with the retrieved data
                    binding.profileName.text = name
                    binding.profileEmail.text = email
                    binding.myOrdersDescription.text = "You have $ordersCount orders"
                    binding.shippingAddressesDescription.text = "$addressesCount addresses"

                    // Load profile image
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(imageUrl)
                            .into(binding.profileImage)
                    }
                } else {
                    // Document does not exist
                    binding.profileName.text = "User data not found"
                    binding.profileEmail.text = "No email available"
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                binding.profileName.text = "Error fetching user data"
                binding.profileEmail.text = "Error: ${exception.message}"
            }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        // Fetch user data from Firebase
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // User is logged in, fetch user data
            getUserData(currentUser.uid)
        } else {
            // User is not logged in
            binding.profileName.text = "User not logged in"
            binding.profileEmail.text = "No email available"
            binding.myOrdersDescription.text = "You have 0 orders"
            binding.shippingAddressesDescription.text = "0 addresses"
        }
    }

    private fun navigateToOrders() {
        findNavController().navigate(R.id.myOrdersFragment)
    }

    private fun navigateToShippingAddresses() {
        findNavController().navigate(R.id.shippingAddressesFragment)
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.settingsFragment)
    }

    private fun navigateToContactUs() {
        findNavController().navigate(R.id.contactUsFragment)
    }

    private fun performLogout() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_session", Activity.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        FirebaseAuth.getInstance().signOut()

        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    private fun showBottomSheetDialog() {
        val bottomSheetBinding = BottomSheetProfileOptionsBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Set click listeners for Edit and Delete options
        bottomSheetBinding.tvEdit.setOnClickListener {
            // Open gallery to select an image
            bottomSheetDialog.dismiss()
            openGallery()
        }

        bottomSheetBinding.tvDelete.setOnClickListener {
            // Delete the profile image
            deleteProfileImage()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }
    private fun deleteProfileImage() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Reference to the image in Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("profileImages/${currentUser.uid}.jpg")

            // Delete image from Firebase Storage
            storageRef.delete().addOnSuccessListener {
                // Successfully deleted image from storage
                removeImageUrlFromFirestore()
                binding.profileImage.setImageResource(R.drawable.ic_profile_image_placeholder) // Set a default image in the UI
                Toast.makeText(requireContext(), "Profile image deleted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                // Failed to delete the image
                Toast.makeText(requireContext(), "Failed to delete image: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeImageUrlFromFirestore() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUser.uid)
                .update("profileImage", "")
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile image URL removed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to update Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                uploadImageToFirebase(imageUri)
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef = firebaseStorage.reference.child("profileImages/${FirebaseAuth.getInstance().currentUser?.uid}.jpg")

        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                saveImageUriToFirestore(uri.toString())
                binding.profileImage.setImageURI(imageUri) // Update UI with selected image
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to upload image: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageUriToFirestore(imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUser.uid)
                .update("profileImage", imageUrl)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile image updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to update Firestore: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
