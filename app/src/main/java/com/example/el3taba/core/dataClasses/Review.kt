package com.example.el3taba.core.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Define Review class as needed
@Parcelize
data class Review(
    val reviewerName: String = "",
    val rating: Float = 0.0f,
    val comment: String = ""
) : Parcelable
