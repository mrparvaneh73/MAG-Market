package com.example.magmarket.data.remote.remotedatasource

import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.model.customer.CustomerResponse
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.remote.network.MarketService
import com.example.magmarket.data.remote.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRemoteDataSource @Inject constructor(private val marketService: MarketService) :
    RemoteDataSource {
    override suspend fun getAllProduct(orderby: String): Response<List<ProductRecyclerViewItem.ProductItem>> {
        return marketService.getAllProduct(orderby = orderby)
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

    override suspend fun getProduct(id: String): Response<ProductItem> {
        return marketService.getProduct(id = id)
    }


}