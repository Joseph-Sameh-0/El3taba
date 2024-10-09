package com.example.el3taba.customer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.el3taba.databinding.FragmentSubHomeBinding

class SubHomeFragment : Fragment() {

    private var _binding: FragmentSubHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSubHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}