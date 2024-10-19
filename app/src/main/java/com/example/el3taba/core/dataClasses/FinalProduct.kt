package com.example.el3taba.core.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinalProduct(
    val id: String = "",
    val title: String = "",
    val name: String = "",
    val description: String = "",
    val stock: Int = 0,
    val price: Double = 0.0,
    val avgRating: Float = 0.0f,
    val numberOfRatings: Int = 0,
    val reviews: List<Review> = emptyList(),
    val mfgDate: String = "",
    val expDate: String = "",
    val state: String = "",
    val sellerName: String = "",
    val sellerID: String = "",
    val category: String = "",
    val subcategory: String = "",
    val imageUrls: List<String> = emptyList(),
    val specs: Map<String, String> = emptyMap()
) : Parcelable

