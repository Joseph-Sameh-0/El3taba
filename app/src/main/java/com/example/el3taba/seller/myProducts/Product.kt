package com.example.el3taba.seller.myProducts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val stock: Int = 0,
    val price: Double = 0.0,
    val imageUrl: String = "",
) : Parcelable
