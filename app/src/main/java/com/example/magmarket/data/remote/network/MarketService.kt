package com.example.magmarket.data.remote.network

import com.example.magmarket.application.Constants.BASE_PARAM
import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.remote.model.coupon.CouponResponse
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.model.customer.CustomerResponse
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MarketService {
    @GET("products")
    suspend fun getAllProduct(
        @QueryMap tokens: Map<String, String> = BASE_PARAM,
        @Query("page") page: Int,
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
        @Query("category") categoryId: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ProductItem>>

    @POST("orders")
    suspend fun createOrder(
        @Query("customer_id") customer_id: Int,
        @Body order: Order,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseOrder>

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") orderId: Int,
        @Body order: UpdateOrder,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseOrder>

    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") orderId: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseOrder>

    @GET("orders/{id}")
    suspend fun getAnOrder(
        @Path("id") orderId: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseOrder>

    @GET("orders/{id}")
    suspend fun getCart(
        @Path("id") orderId: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    )

    @DELETE("orders/{id}")
    suspend fun deleteAnItemFromOrder(
        @Path("id") orderId: Int,
        @Body order: UpdateOrder,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseOrder>

    @GET("orders")
    suspend fun getPlacedOrders(
        @Query("customer") customer_id: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ResponseOrder>>

    @GET("products")
    suspend fun getSimilarProducts(
        @Query("include") include: String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ProductItem>>

    @GET("products")
    suspend fun searchProduct(
        @Query("search") search: String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ProductItem>>

    @POST("customers")
    suspend fun createCustomer(
        @Body customer: Customer,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<CustomerResponse>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<CustomerResponse>

    @PUT("customers/{id}")
    suspend fun updateCustomer(
        @Path("id") id: Int,
        @Body customer: Customer,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<CustomerResponse>


    @GET("products/reviews")
    suspend fun getProductComment(
        @Query("product") productId: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ResponseReview>>

    @GET("products/reviews")
    suspend fun getAllProductComment(
        @Query("product") productId: Int,
        @Query("page") page: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ResponseReview>>

    @POST("products/reviews")
    suspend fun sendUserComment(
        @Body review: Review,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseReview>

    @DELETE("products/reviews/{id}")
    suspend fun deleteUserComment(
        @Path("id") id: Int,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseReview>

    @PUT("products/reviews/{id}")
    suspend fun updateComment(
        @Path("id") id: Int,
        @Body review: Review,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<ResponseReview>


    @GET("coupons")
    suspend fun verifyCoupon(
        @Query("code") couponCode: String,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<CouponResponse>>

    @GET("products")
    suspend fun getSortedProduct(
        @Query("orderby") orderBy: String = "date",
        @Query("page") page: Int = 1,
        @QueryMap tokens: Map<String, String> = BASE_PARAM
    ): Response<List<ProductItem>>
}