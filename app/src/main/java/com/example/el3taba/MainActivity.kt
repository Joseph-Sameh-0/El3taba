package com.example.el3taba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.el3taba.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val isUserLoggedIn = checkUserLoggedIn()

        var graphId = R.navigation.auth_navigation
        var menuId = R.menu.auth_bottom_nav_menu

        navView.menu.clear()
        navView.inflateMenu(menuId)

        navController.graph = navController.navInflater.inflate(graphId)

        var appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.welcomeFragment, R.id.loginFragment, R.id.signupFragment
            )
        )

        Log.d("MainActivity", "isUserLoggedIn is $isUserLoggedIn")
        if (isUserLoggedIn) {
            // Get the user's role from SharedPreferences, Firebase, or your backend
            val userRole = getUserRole()
            when (userRole) {
                "customer" -> {
                    Log.d("MainActivity", "user is customer")

                    graphId = R.navigation.customer_navigation
                    menuId = R.menu.customer_bottom_nav_menu

                    navView.menu.clear()
                    navView.inflateMenu(menuId)

                    navController.graph = navController.navInflater.inflate(graphId)

                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.customer_navigation_home,
                            R.id.customer_navigation_shop,
                            R.id.customer_navigation_bag,
                            R.id.customer_navigation_favorites,
                            R.id.customer_navigation_profile
                        )
                    )
                }

                "seller" -> {
                    Log.d("MainActivity", "user is seller")
                    graphId = R.navigation.seller_navigation
                    menuId = R.menu.seller_bottom_nav_menu

                    navView.menu.clear()
                    navView.inflateMenu(menuId)

                    navController.graph = navController.navInflater.inflate(graphId)

                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.seller_navigation_home,
                            R.id.seller_navigation_dashboard,
                            R.id.seller_navigation_notifications
                        )
                    )
                }

                "admin" -> {
                    Log.d("MainActivity", "user is admin")
                    // Redirect to AdminActivity
//                    binding.welcome.visibility = View.GONE
                    graphId = R.navigation.admin_navigation
                    menuId = R.menu.admin_bottom_nav_menu

                    navView.menu.clear()
                    navView.inflateMenu(menuId)

                    navController.graph = navController.navInflater.inflate(graphId)

                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.admin_navigation_home,
                            R.id.admin_navigation_dashboard,
                            R.id.admin_navigation_notifications
                        )
                    )
                }

                else -> {
                    Log.d("MainActivity", "user is Unknown")
                    // Unknown role, log out or show error
                    showErrorMessage("Unknown role!")
                    logout()
                }
            }
        } else {
            Log.d("MainActivity", "user is not logged in")

            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.loginFragment, null, NavOptions.Builder()
                .setPopUpTo(findNavController(R.id.nav_host_fragment_activity_main).graph.startDestinationId, true).build())
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // This function checks if the user is logged in
    private fun checkUserLoggedIn(): Boolean {
        Log.d("MainActivity", "in checkUserLoggedIn()")

        // Example: You can check shared preferences or Firebase Auth state here
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // This function retrieves the user role
    private fun getUserRole(): String? {
        Log.d("MainActivity", "in getUserRole")
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
        // go to login
    }
}