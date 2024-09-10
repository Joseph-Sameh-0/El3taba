package com.example.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.auth.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLogin(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        // Call your backend API or Firebase Auth for login
        // For this example, let's assume the login was successful and you got a user role
        // Simulating login success:

        val userRole = getRoleFromBackend(email) // Example: customer, seller, or admin

        // Save the session and user role in SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            putString("userRole", userRole)
            apply()
        }

        // Redirect to MainActivity after login
        startActivity(Intent(this, Class.forName("com.example.MainActivity")))
        finish()
    }

    private fun getRoleFromBackend(email: String): String {
        // Example function to get user role from backend, hardcoded for now
        return when (email) {
            "customer@example.com" -> "customer"
            "seller@example.com" -> "seller"
            "admin@example.com" -> "admin"
            else -> "customer"
        }
    }
}
