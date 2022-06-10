package com.example.magmarket.data.remote.model

import kotlinx.parcelize.Parcelize


data class ProductItem(
    val id: String,
    val name: String?,
    val description: String?,
    val price: String?,
    val categories: List<ProductCategory>?,
    val images: List<ProductImage>?,
    val regular_price: String? = "",
    val sale_price: String?,
    val related_ids: List<Int>? = emptyList()
)
