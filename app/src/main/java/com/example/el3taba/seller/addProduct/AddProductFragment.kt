package com.example.el3taba.seller.addProduct

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.el3taba.databinding.SellerFragmentAddProductBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// Add this in your fragment class
private lateinit var storage: StorageReference
private val IMAGE_PICK_CODE = 1000
private val PERMISSION_CODE = 1001
val db = Firebase.firestore

class AddProductFragment : Fragment() {

    private var _binding: SellerFragmentAddProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addProductViewModel =
            ViewModelProvider(this).get(AddProductViewModel::class.java)

        _binding = SellerFragmentAddProductBinding.inflate(inflater, container, false)
        storage = FirebaseStorage.getInstance().reference

        val root: View = binding.root

        val categories = listOf("Electronics", "Clothing", "Food", "Books", "Pets", "Home Appliances")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, categories)
        binding.categorySpinner.adapter = adapter

        binding.selectPictureButton.setOnClickListener {
            // Check for the new permissions for Android 13+
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_CODE)
            } else {
                // Permission already granted
                pickImageFromGallery()
            }
        }


        binding.submitButton.setOnClickListener {
            val productName = binding.name.text.toString()
            val description = binding.description.text.toString()
            val stock = binding.stock.text.toString().toIntOrNull() ?: 0
            val price = binding.price.text.toString().toFloatOrNull() ?: 0f
            val category = binding.categorySpinner.selectedItem.toString()

            if (productName.isNotEmpty() && description.isNotEmpty() && price > 0) {
                val imageUri = binding.addPictures.tag as? Uri
                if (imageUri != null) {
                    // Upload the product if the image is selected
                    uploadProductToFirestore(productName, description, stock, price, category)
                } else {
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


        val textView: TextView = binding.textAddProduct
        addProductViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.addPictures.setImageURI(uri)
            binding.addPictures.tag = uri // Save URI for later use
        }
    }

    private fun updateProductCount() {
        val dashboardDoc = db.collection("dashboard").document("productCount")

        dashboardDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Increment the product count
                val currentCount = documentSnapshot.getLong("count") ?: 0
                dashboardDoc.update("count", currentCount + 1)
            } else {
                // If the document doesn't exist, create one with count = 1
                dashboardDoc.set(hashMapOf("count" to 1))
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Error updating product count: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadProductToFirestore(name: String, description: String, stock: Int, price: Float, category: String) {
        val imageUri = binding.addPictures.tag as? Uri  // Assuming you saved the image URI earlier
        if (imageUri != null) {
            // Upload image to Firebase Storage
            val filePath = storage.child("products/${System.currentTimeMillis()}.jpg")
            filePath.putFile(imageUri)
                .addOnSuccessListener {
                    // Get the download URL for the image
                    filePath.downloadUrl.addOnSuccessListener { uri ->
                        // Add product data to Firestore with image URL
                        val productData = hashMapOf(
                            "name" to name,
                            "description" to description,
                            "stock" to stock,
                            "price" to price,
                            "category" to category,
                            "imageUrl" to uri.toString()
                        )
                        db.collection("products").add(productData)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show()
                                updateProductCount()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Error adding product: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}