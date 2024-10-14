package com.example.el3taba.customer.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.el3taba.R
import com.example.el3taba.databinding.FragmentMyOrdersBinding

class MyOrdersFragment : Fragment() {
    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadOrdersFromFirebase("delivered")

        binding.btnDelivered.setOnClickListener {
            highlightSelectedTab(it.id)
            loadOrdersFromFirebase("delivered")
        }

        binding.btnProcessing.setOnClickListener {
            highlightSelectedTab(it.id)
            loadOrdersFromFirebase("processing")
        }

        binding.btnCancelled.setOnClickListener {
            highlightSelectedTab(it.id)
            loadOrdersFromFirebase("cancelled")
        }

        binding.backButtonSettings.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize adapter (you need to create an adapter class for orders)
        // Example:
        // binding.ordersRecyclerView.adapter = OrdersAdapter(emptyList())
    }

    private fun loadOrdersFromFirebase(status: String) {
        // Load orders from Firebase according to the status
        // status can be "delivered", "processing", or "cancelled"
    }

    private fun highlightSelectedTab(selectedId: Int) {
        // Reset button styles
        binding.btnDelivered.setBackgroundColor(resources.getColor(android.R.color.white))
        binding.btnDelivered.setTextColor(resources.getColor(android.R.color.black))

        binding.btnProcessing.setBackgroundColor(resources.getColor(android.R.color.white))
        binding.btnProcessing.setTextColor(resources.getColor(android.R.color.black))

        binding.btnCancelled.setBackgroundColor(resources.getColor(android.R.color.white))
        binding.btnCancelled.setTextColor(resources.getColor(android.R.color.black))

        // Set selected button style
        when (selectedId) {
            binding.btnDelivered.id -> {
                binding.btnDelivered.setBackgroundColor(resources.getColor(R.color.black))
                binding.btnDelivered.setTextColor(resources.getColor(R.color.white))
            }

            binding.btnProcessing.id -> {
                binding.btnProcessing.setBackgroundColor(resources.getColor(R.color.black))
                binding.btnProcessing.setTextColor(resources.getColor(R.color.white))
            }

            binding.btnCancelled.id -> {
                binding.btnCancelled.setBackgroundColor(resources.getColor(R.color.black))
                binding.btnCancelled.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}