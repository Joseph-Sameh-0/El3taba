package com.example.el3taba.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.el3taba.R
import com.example.el3taba.databinding.AuthFragmentForgetPasswordBinding


class ForgetPasswordFragment : Fragment() {
    private var _binding: AuthFragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = AuthFragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Sign-up button click
        binding.SubmitButton.setOnClickListener {
            // Here you can retrieve the input values and handle sign-up logic
            val email = binding.emailEditText.text.toString()
            val oldPassword = binding.oldPasswordEditText.text.toString()
            val newPassword = binding.newPasswordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Perform validation and other logic here
        }
    }
}