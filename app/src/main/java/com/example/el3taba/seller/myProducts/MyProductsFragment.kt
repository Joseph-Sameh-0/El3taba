package com.example.el3taba.seller.myProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.SellerFragmentMyProductsBinding
import com.example.el3taba.seller.addProduct.db
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProductsFragment : Fragment() {

    private var _binding: SellerFragmentMyProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MyProductsAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SellerFragmentMyProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        fetchProductsFromFirestore()

        return root
    }

    private fun setupRecyclerView() {
        adapter = MyProductsAdapter(productList, ::editProduct, ::deleteProduct)
        binding.recyclerViewMyProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMyProducts.adapter = adapter
    }

    private fun fetchProductsFromFirestore() {
        val db = Firebase.firestore
        db.collection("products").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val product = document.toObject(Product::class.java).apply {
                        id = document.id
                    }
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    private fun editProduct(product: Product) {
        val bundle = Bundle().apply {
            putParcelable("product", product)  // Pass the product as Parcelable
        }
        val navController = findNavController()
        navController.navigate(R.id.action_myProductsFragment_to_editProductFragment, bundle)
    }

    private fun deduceProductCount() {
        val dashboardDoc = db.collection("dashboard").document("productCount")

        dashboardDoc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Increment the product count
                val currentCount = documentSnapshot.getLong("count") ?: 0
                dashboardDoc.update("count", currentCount - 1)
            } else {
                // If the document doesn't exist, create one with count = 1
                dashboardDoc.set(hashMapOf("count" to 1))
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Error updating product count: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProduct(product: Product) {
        val db = Firebase.firestore
        db.collection("products").document(product.id).delete()
            .addOnSuccessListener {
                productList.remove(product)
                deduceProductCount()
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
