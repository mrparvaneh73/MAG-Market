package com.example.magmarket.data.remote.model

data class CategoryItem(
    val id: Int,
    val image: Image,
    val name: String,
    val parent:Int,
    val count:Int
)