package com.example.magmarket.data.remote.network

import com.example.magmarket.application.Constants
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RemoteDataSource {
    suspend fun getAllProduct(page: Int,orderby: String): Response<List<ProductRecyclerViewItem.ProductItem>>
    suspend fun getProduct(id: String): Response<ProductItem>

    suspend fun getAllCategories(): Response<List<CategoryItem>>
    suspend fun getSubCategories(parent: Int): Response<List<CategoryItem>>
    suspend fun getproductOfCategory(categoryId: Int): Response<List<ProductItem>>
    suspend fun creatOrder(customer_id: Int, order: Order): Response<ResponseOrder>
    suspend fun getSimilarProducts(include: String): Response<List<ProductItem>>
    suspend fun searchProduct(search: String): Response<List<ProductItem>>
    suspend fun getPlacedOrder(customer_id: Int): Response<List<ResponseOrder>>
    suspend fun createCustomer(customer: Customer): Response<CustomerResponse>
    suspend fun getCustomer(id: Int): Response<CustomerResponse>
    suspend fun updateCustomer(id: Int, customer: Customer): Response<CustomerResponse>
    suspend fun updateOrder(OrderId: Int, order: UpdateOrder): Response<ResponseOrder>
    suspend fun deleteAnItemFromOrder(orderId: Int, order: UpdateOrder): Response<ResponseOrder>
    suspend fun deleteOrder(orderId: Int): Response<ResponseOrder>
    suspend fun getAnOrder(orderId: Int): Response<ResponseOrder>

    suspend fun getProductComment(productId: Int): Response<List<ResponseReview>>
    suspend fun sendUserComment(review: Review): Response<ResponseReview>
    suspend fun deleteUserComment(id: Int): Response<ResponseReview>
    suspend fun updateComment(id: Int, review: Review): Response<ResponseReview>


    suspend fun verifyCoupon(couponCode: String): Response<List<CouponResponse>>
}