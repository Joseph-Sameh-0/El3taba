package com.example.el3taba.customer.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.el3taba.databinding.CustomerFragmentBagBinding

class BagFragment : Fragment() {

private var _binding: CustomerFragmentBagBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val bagViewModel =
            ViewModelProvider(this).get(BagViewModel::class.java)

    _binding = CustomerFragmentBagBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textBag
    bagViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}