package com.example.el3taba.seller.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.el3taba.databinding.SellerFragmentAddProductBinding

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
        val root: View = binding.root

        val textView: TextView = binding.textAddProduct
        addProductViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}