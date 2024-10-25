package com.example.el3taba

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.el3taba.core.dataClasses.FinalProduct
import com.example.el3taba.databinding.ActivityMainBinding
import com.example.el3taba.seller.myProducts.MyProductsViewModel
import com.example.el3taba.seller.myProducts.Product
import com.example.el3taba.core.dataClasses.Review
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
////////////////////////////////////////////////////////////////////////////////////
//    val productViewModel: MyProductsViewModel by viewModels()
//
//    // Prepare static data for a Product
//    val product = FinalProduct(
//        title = "Dell Laptop",
//        name = "very good Dell laptop for gaming",
//        description = "Description Description Description Description Description Description Description Description Description Description Description ",
//        stock = 100,
//        price = 199.99,
//        mfgDate = "2023-01-01",
//        expDate = "N/A",
//        state = "New",  // State of the product (new or used)
//        sellerID = "csyCB5rGbfYoSEMvHuAFN9qaYwE2",  // Name of the seller
//        sellerName = "Seller Name",  // Name of the seller
//        categoryID = "Electronics",  // Product category
//        subcategoryID = "Rr2Ce7NQzXkmwNzrcyDY",  // Product subcategory
//        imageUrl = "https://m.media-amazon.com/images/I/71QmKIgj+BL._AC_SL1500_.jpg",
//
////        imageUrls = mutableListOf<String>(
////            "https://m.media-amazon.com/images/I/71QmKIgj+BL._AC_SL1500_.jpg",
////            "https://m.media-amazon.com/images/I/71QS1tJQ9IL._AC_SL1500_.jpg",
////            "https://m.media-amazon.com/images/I/71oofzP5DlL._AC_SL1500_.jpg",
////            "https://m.media-amazon.com/images/I/61Lt8Th+ZUL._AC_SL1500_.jpg",
////            "https://m.media-amazon.com/images/I/71lMSVCVnwL._AC_SL1500_.jpg",
////        ),
//
//        specs = mapOf(  // Specifications of the product
//            "Color" to "Black",
//            "Storage" to "128GB",
//            "Battery" to "4000mAh",
//            "gggggggg" to "hhhhhh",
//            "jjjjjjjj" to "kkkkkkk",
//            "llllllll" to "mmmmmmm",
//        )
//    )
//        val productImageUri: Uri? = null // Your product image Uri
//        val subcategoryId = "1PvzgwRSSpFe2VP7DRMr" // Your subcategory ID
//        val categoryId = "Electronics" // Your subcategory ID
//
//        val addProductResult: LiveData<Boolean> =
//            productViewModel.addProduct(product, productImageUri, categoryId, subcategoryId)
//        addProductResult.observe(this, Observer { isSuccess ->
//            if (isSuccess) {
//                // Product added successfully
//                Log.d("productttt","Product added successfully!")
//            } else {
//                // Failed to add product
//                Log.d("productttt","Failed to add product.")
//            }
//        })
////////////////////////////////////////////////////////////////////////////////////
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
////        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController


        val isUserLoggedIn = checkUserLoggedIn()

        var graphId = R.navigation.auth_navigation
        var menuId = R.menu.auth_bottom_nav_menu


        Log.d("MainActivity", "isUserLoggedIn is $isUserLoggedIn")
        if (isUserLoggedIn) {
            // Get the user's role from SharedPreferences, Firebase, or your backend
            val userRole = getUserRole()
            when (userRole) {
                "customer" -> {
                    Log.d("MainActivity", "user is customer")
                    graphId = R.navigation.customer_navigation
                    menuId = R.menu.customer_bottom_nav_menu
                }

                "seller" -> {
                    Log.d("MainActivity", "user is seller")
                    graphId = R.navigation.seller_navigation
                    menuId = R.menu.seller_bottom_nav_menu
                }

                "admin" -> {
                    Log.d("MainActivity", "user is admin")
                    // Redirect to AdminActivity
                    graphId = R.navigation.admin_navigation
                    menuId = R.menu.admin_bottom_nav_menu
                }

                else -> {
                    Log.d("MainActivity", "user is Unknown")
                    // Unknown role, log out or show error
                    showErrorMessage("Unknown role!")
                    logout()

//                    navController.navigate(
//                        R.id.loginFragment, null, NavOptions.Builder().setPopUpTo(
//                            navController.graph.startDestinationId,
//                            true
//                        ).build()
//                    )
//                    return
                }
            }
        } else {
            Log.d("MainActivity", "user is not logged in")

            lifecycleScope.launch {
                kotlinx.coroutines.delay(2000)
                navController.navigate(
                    R.id.loginFragment, null, NavOptions.Builder().setPopUpTo(
                        navController.graph.startDestinationId,
                        true
                    ).build()
                )
            }
            binding.navView.visibility = View.GONE
            return ///
        }

        navView.menu.clear()
        navView.inflateMenu(menuId)

        try {
            // 2. Set the navigation graph
            navController.setGraph(graphId)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error inflating navigation graph: ${e.message}")
            showErrorMessage("Failed to load the navigation graph.")
            logout()
            return // Stop further execution
        }

//        navController.graph = navController.navInflater.inflate(graphId) // this line causes an error
        binding.navView.visibility = View.GONE
        val appBarConfiguration: AppBarConfiguration
        when (graphId) {
            R.navigation.auth_navigation -> {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.loginFragment,
                        R.id.signupFragment,
                        R.id.forgetPasswordFragment,
                        R.id.setNewPasswordFragment
                    )
                )
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(2000)
                    navController.navigate(
                        R.id.action_welcomeFragment_to_loginFragment2,
                        null,
                        NavOptions.Builder().setPopUpTo(
                            navController.graph.startDestinationId,
                            true
                        ).build()
                    )
                    binding.navView.visibility = View.VISIBLE
                }
            }

            R.navigation.customer_navigation -> {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.customer_navigation_home,
                        R.id.customer_navigation_shop,
                        R.id.customer_navigation_bag,
                        R.id.customer_navigation_favorites,
                        R.id.customer_navigation_profile
                    )
                )
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(2000)
                    navController.navigate(
                        R.id.customer_navigation_home, null, NavOptions.Builder().setPopUpTo(
                            navController.graph.startDestinationId,
                            true
                        ).build()
                    )
                    binding.navView.visibility = View.VISIBLE

                }
            }

            R.navigation.seller_navigation -> {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.seller_navigation_dashboard,
                        R.id.seller_navigation_my_products,
                        R.id.seller_navigation_add_product,
                        R.id.seller_navigation_profile,
                    )
                )
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(2000)
                    navController.navigate(
                        R.id.seller_navigation_dashboard, null, NavOptions.Builder().setPopUpTo(
                            navController.graph.startDestinationId,
                            true
                        ).build()
                    )
                    binding.navView.visibility = View.VISIBLE

                }
            }

            R.navigation.admin_navigation -> {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.admin_navigation_home,
                        R.id.admin_navigation_dashboard,
                        R.id.admin_navigation_notifications
                    )
                )
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(2000)
                    navController.navigate(
                        R.id.admin_navigation_home, null, NavOptions.Builder().setPopUpTo(
                            navController.graph.startDestinationId,
                            true
                        ).build()
                    )
                    binding.navView.visibility = View.VISIBLE
                }
            }

            else -> {
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.loginFragment,
                        R.id.signupFragment,
                        R.id.forgetPasswordFragment,
                        R.id.setNewPasswordFragment
                    )
                )
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(2000)
                    navController.navigate(
                        R.id.action_welcomeFragment_to_loginFragment2,
                        null,
                        NavOptions.Builder().setPopUpTo(
                            navController.graph.startDestinationId,
                            true
                        ).build()
                    )
                    binding.navView.visibility = View.VISIBLE
                }
            }
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
        this.recreate()
        // go to login
    }
}