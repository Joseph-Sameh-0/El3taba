package com.example.el3taba

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Check if the user is logged in (You can store session data in SharedPreferences or check FirebaseAuth state)
        val isUserLoggedIn = checkUserLoggedIn()

        if (isUserLoggedIn) {
            // Get the user's role from SharedPreferences, Firebase, or your backend
            val userRole = getUserRole()

            when (userRole) {
                "customer" -> {
                    // Redirect to CustomerActivity
                    startActivity(Intent(this, CustomerActivity::class.java))
                    finish()
                }
                "seller" -> {
                    // Redirect to SellerActivity
                    startActivity(Intent(this, SellerActivity::class.java))
                    finish()
                }
                "admin" -> {
                    // Redirect to AdminActivity
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                }
                else -> {
                    // Unknown role, log out or show error
                    showErrorMessage("Unknown role!")
                    logout()
                }
            }
        } else {
            // If user is not logged in, redirect to the login screen from the Auth Module
            startActivity(Intent(this, Class.forName("com.example.auth.LoginActivity")))
            finish()
        }
    }

    // This function checks if the user is logged in
    private fun checkUserLoggedIn(): Boolean {
        // Example: You can check shared preferences or Firebase Auth state here
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // This function retrieves the user role
    private fun getUserRole(): String? {
        // Example: Retrieve from shared preferences or Firebase
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        return sharedPreferences.getString("userRole", null)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun logout() {
        // Clear session and redirect to login
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        startActivity(Intent(this, Class.forName("com.example.auth.LoginActivity")))
        finish()
    }
}