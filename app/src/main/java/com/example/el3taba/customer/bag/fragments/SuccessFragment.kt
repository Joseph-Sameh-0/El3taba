package com.example.el3taba.customer.bag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.el3taba.R
import com.example.el3taba.databinding.BagFragmentSuccessBinding
import kotlinx.coroutines.launch

class SuccessFragment : Fragment() {
    private var _binding: BagFragmentSuccessBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = BagFragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to ShopItemListFragment when the subcategory button is clicked

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        lifecycleScope.launch {
            kotlinx.coroutines.delay(2000)
            findNavController().popBackStack(R.id.bagListItemsFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}