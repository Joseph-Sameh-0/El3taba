package com.example.el3taba.core.dataClasses


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Review(
    val reviewID: String,
    val reviewerID: String,
    val productId: String,
    val rating: Float,
    val reviewText: String,
    val reviewDate: Date
) : Parcelable


