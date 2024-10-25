package com.example.el3taba.seller.myProducts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = ""
) : Parcelable
