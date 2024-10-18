package com.example.el3taba.customer.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.el3taba.databinding.CustomerFragmentFavoritesBinding


class FavoritesFragment : Fragment() {

    private lateinit var binding: CustomerFragmentFavoritesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomerFragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
}
