package com.example.magmarket.data.remote.network

import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.utils.Constants.BASE_PARAM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MarketService {
    @GET("products")
    suspend fun getAllProduct(
        @QueryMap tokens: Map<String, String> = BASE_PARAM,
        @Query("page") page: Int = 1,
        @Query("orderby") orderby: String
    ): Response<List<ProductRecyclerViewItem.ProductItem>>

    @GET("products/categories")
    suspend fun getAllCategories(
        @QueryMap tokens: Map<String, String> = BASE_PARAM,
    ): Response<List<CategoryItem>>

    @GET("products/categories")
    suspend fun getSubCategories(
        @Query("parent")  parent:Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM

    ): Response<List<CategoryItem>>

    @GET("products/{id}")
    suspend fun getProduct( @Path("id") id:String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM

    ): Response<ProductItem>
}