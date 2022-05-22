package com.example.magmarket.data.remote.network

import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.utils.Constants.BASE_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MarketService {
    @GET("products")
    suspend fun getAllProduct(
        @QueryMap tokens: Map<String, String> = BASE_PARAM,
        @Query("page") page: Int = 1,
        @Query("orderby") orderby: String
    ): Response<List<ProductItem>>
}