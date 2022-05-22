package com.example.magmarket.data.remote.network

import com.example.magmarket.data.model.ProductItem
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getAllProduct(orderby:String): Response<List<ProductItem>>
}