package com.example.el3taba.core.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class newProduct(
    val id: String = "",
    val title: String = "",         // New field for product title
    val name: String = "",
    val description: String = "",
    val stock: Int = 0,
    val price: Double = 0.0,
    val avgRating: Float = 0.0f,    // New field for average rating
    val numberOfRatings: Int = 0,   // New field for number of ratings
    val reviews: List<Review> = emptyList(), // New field for list of reviews
    val mfgDate: String = "",        // New field for manufacturing date
    val expDate: String = "",        // New field for expiration date
    val state: String = "",          // New field for state (new/used)
    val seller: String = "",         // New field for seller name
    val category: String = "",       // New field for product category
    val subcategory: String = "",     // New field for product subcategory
    val imageUrl: String = "",       // Existing field for image URL
    val specs: Map<String, String> = emptyMap() // New field for specifications
) : Parcelable

