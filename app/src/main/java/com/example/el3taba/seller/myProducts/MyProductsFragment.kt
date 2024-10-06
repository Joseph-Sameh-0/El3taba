package com.example.el3taba.seller.myProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.databinding.SellerFragmentMyProductsBinding
import com.example.el3taba.seller.myProducts.Product
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
        // Handle edit functionality
    }

    private fun deleteProduct(product: Product) {
        val db = Firebase.firestore
        db.collection("products").document(product.id).delete()
            .addOnSuccessListener {
                productList.remove(product)
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


/*
package com.example.el3taba.seller.myProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.el3taba.databinding.SellerFragmentMyProductsBinding
import com.example.el3taba.seller.myProducts.Product

class MyProductsFragment : Fragment() {

    private var _binding: SellerFragmentMyProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myProductsViewModel =
            ViewModelProvider(this).get(MyProductsViewModel::class.java)

        _binding = SellerFragmentMyProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMyProducts
        myProductsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

 */