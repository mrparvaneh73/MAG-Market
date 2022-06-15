package com.example.magmarket.data.remote.remotedatasource

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
import com.example.magmarket.data.remote.network.MarketService
import com.example.magmarket.data.remote.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRemoteDataSource @Inject constructor(private val marketService: MarketService) : RemoteDataSource {
    override suspend fun getAllProduct(page:Int,orderby: String): Response<List<ProductRecyclerViewItem.ProductItem>> {
        return marketService.getAllProduct(page = page , orderby = orderby)
    }

    override suspend fun getAllCategories(): Response<List<CategoryItem>> {
        return marketService.getAllCategories()
    }

    override suspend fun getSubCategories(parent: Int): Response<List<CategoryItem>> {
        return marketService.getSubCategories(parent)
    }

    override suspend fun getproductOfCategory(categoryId: Int): Response<List<ProductItem>> {
        return marketService.getProductOfCategory(categoryId)
    }

    override suspend fun creatOrder(customer_id:Int,order: Order): Response<ResponseOrder> {
        return marketService.createOrder(customer_id, order)
    }

    override suspend fun getSimilarProducts(include: String): Response<List<ProductItem>> {
        return marketService.getSimilarProducts(include)
    }

    override suspend fun searchProduct(search: String): Response<List<ProductItem>> {
        return marketService.searchProduct(search)
    }

    override suspend fun getPlacedOrder(customer_id:Int): Response<List<ResponseOrder>> {
        return marketService.getPlacedOrders(customer_id)
    }

    override suspend fun createCustomer(customer: Customer): Response<CustomerResponse> {
        return marketService.createCustomer(customer)
    }

    override suspend fun getCustomer(id: Int): Response<CustomerResponse> {
        return marketService.getCustomer(id)
    }

    override suspend fun updateCustomer(id: Int, customer: Customer): Response<CustomerResponse> {
        return marketService.updateCustomer(id,customer)
    }

    override suspend fun updateOrder(OrderId: Int, order: UpdateOrder): Response<ResponseOrder> {
        return marketService.updateOrder(OrderId,order)
    }

    override suspend fun deleteAnItemFromOrder(
        orderId: Int,
        order: UpdateOrder
    ): Response<ResponseOrder> {
        return marketService.deleteAnItemFromOrder(orderId,order)
    }

    override suspend fun deleteOrder(orderId: Int): Response<ResponseOrder> {
        return marketService.deleteOrder(orderId)
    }

    override suspend fun getAnOrder(orderId: Int): Response<ResponseOrder> {
        return marketService.getAnOrder(orderId)
    }

    override suspend fun getProductComment(productId: Int): Response<List<ResponseReview>> {
        return marketService.getProductComment(productId)
    }

    override suspend fun getAllProductComment(
        productId: Int,
        page: Int
    ): Response<List<ResponseReview>> {
        return  marketService.getAllProductComment(productId,page)
    }

    override suspend fun sendUserComment(review: Review): Response<ResponseReview> {

        return marketService.sendUserComment(review)
    }

    override suspend fun deleteUserComment(id: Int): Response<ResponseReview> {
        return marketService.deleteUserComment(id)
    }

    override suspend fun updateComment(id: Int, review: Review): Response<ResponseReview> {
        return marketService.updateComment(id,review)
    }

    override suspend fun verifyCoupon(couponCode: String): Response<List<CouponResponse>> {
        return marketService.verifyCoupon(couponCode)
    }

    override suspend fun getProduct(id: String): Response<ProductItem> {
        return marketService.getProduct(id = id)
    }


}