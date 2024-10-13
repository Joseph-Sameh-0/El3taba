package com.example.el3taba.seller.editProduct

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.el3taba.databinding.SellerFragmentEditProductBinding
import com.example.el3taba.seller.myProducts.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.parcelize.Parcelize

class EditProductFragment : Fragment() {

    private var _binding: SellerFragmentEditProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var storage: StorageReference
    private var selectedProduct: Product? = null
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SellerFragmentEditProductBinding.inflate(inflater, container, false)
        storage = FirebaseStorage.getInstance().reference

        val categories = listOf("Electronics", "Clothing", "Food", "Books", "Pets", "Home Appliances")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.categorySpinner.adapter = adapter

        // Get the product data from the bundle
        selectedProduct = arguments?.getParcelable("product")

        // Pre-fill the data
        selectedProduct?.let { product ->
            binding.name.setText(product.name)
            binding.description.setText(product.description)
            binding.stock.setText(product.stock.toString())
            binding.price.setText(product.price.toString())
            binding.categorySpinner.setSelection(categories.indexOf(product.category))
            // Load the image
            Glide.with(this).load(product.imageUrl).into(binding.addPictures)
        }

        binding.selectPictureButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                ActivityCompat.requestPermissions(requireActivity(), permissions, 1001)
            } else {
                pickImageFromGallery()
            }
        }

        binding.submitButton.text = "Edit"
        binding.submitButton.setOnClickListener {
            val productName = binding.name.text.toString()
            val description = binding.description.text.toString()
            val stock = binding.stock.text.toString().toIntOrNull() ?: 0
            val price = binding.price.text.toString().toFloatOrNull() ?: 0f
            val category = binding.categorySpinner.selectedItem.toString()

            if (productName.isNotEmpty() && description.isNotEmpty() && price > 0) {
                updateProductInFirestore(productName, description, stock, price, category)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.addPictures.setImageURI(uri)
            imageUri = uri
        }
    }

    private fun updateProductInFirestore(name: String, description: String, stock: Int, price: Float, category: String) {
        val product = selectedProduct ?: return

        if (imageUri != null) {
            val filePath = storage.child("products/${product.id}.jpg")
            filePath.putFile(imageUri!!)
                .addOnSuccessListener {
                    filePath.downloadUrl.addOnSuccessListener { uri ->
                        val updatedData = mapOf(
                            "name" to name,
                            "description" to description,
                            "stock" to stock,
                            "price" to price,
                            "category" to category,
                            "imageUrl" to uri.toString()
                        )
                        updateFirestore(product.id, updatedData)
                    }
                }
        } else {
            val updatedData = mapOf(
                "name" to name,
                "description" to description,
                "stock" to stock,
                "price" to price,
                "category" to category
            )
            updateFirestore(product.id, updatedData)
        }
    }

    private fun updateFirestore(productId: String, data: Map<String, Any>) {
        Firebase.firestore.collection("products").document(productId)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error updating product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
