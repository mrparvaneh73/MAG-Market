package com.example.magmarket.data.remote.model.review

data class Review(
    val product_id: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    val reviewer_email: String
)