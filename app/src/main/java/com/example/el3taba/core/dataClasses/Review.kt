package com.example.el3taba.core.dataClasses

import com.google.type.Date

data class Review(
    val reviewID: String,
    val reviewerID: String,
    val productId: String,
    val rating: Float,
    val reviewText: String,
    val reviewDate: Date
)