package com.example.el3taba.seller.myProducts

data class Product(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var stock: Int = 0,
    var price: Float = 0f,
    var category: String = "",
    var imageUrl: String = ""
)
