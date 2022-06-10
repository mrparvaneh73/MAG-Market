package com.example.magmarket.data.remote.model

data class Cart(
    val id: Int = 0,
    val productId:Int=0,
    val name: String = "",
    val price: String = "",
    val images: String = "",
    val regular_price: String = "",
    val sale_price: String = "",
    val count: Int = 0,
    val off: Int = 0
)
