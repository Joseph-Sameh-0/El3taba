package com.example.el3taba.core.dataClasses

// note: not the final class
data class Product(
    val name: String,
    val price: String,
    val oldPrice: String?,
    val rating: Float,
    val imageResId: Int
)
