package com.example.el3taba.customer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.el3taba.R
import com.example.el3taba.databinding.CustomerFragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: CustomerFragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomerFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_profile) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.customer_profile_nav)
        navController.graph = navGraph
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}