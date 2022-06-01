package com.example.magmarket.data.remote.network

import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.utils.Constants.BASE_PARAM
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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
        @Query("parent") parent: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM

    ): Response<List<CategoryItem>>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ProductItem>

    @GET("products")
    suspend fun getProductOfCategory(
        @Query("category") categoryId:Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ProductItem>>

    @POST("orders")
    suspend fun createOrder(
        @Body order: Order,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ):Response<ResponseOrder>

    @GET("orders")
    suspend fun getPlacedOrders(
        @Query("include") include:String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ):Response<List<ResponseOrder>>

    @GET("products")
    suspend fun getAllOrders(
        @Query("include") include:String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ):Response<List<ProductItem>>

    @GET("products")
   suspend fun searchProduct(
        @Query("search") search: String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ProductItem>>
}