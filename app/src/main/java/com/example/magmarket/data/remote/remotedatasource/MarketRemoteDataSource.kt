package com.example.magmarket.data.remote.remotedatasource

import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
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

    override suspend fun creatOrder(order: Order): Response<ResponseOrder> {
        return marketService.createOrder(order)
    }

    override suspend fun getAllOrders(include: String): Response<List<ProductItem>> {
        return marketService.getAllOrders(include)
    }

    override suspend fun searchProduct(search: String): Response<List<ProductItem>> {
        return marketService.searchProduct(search)
    }

    override suspend fun getPlacedOrder(include: String): Response<List<ResponseOrder>> {
        return marketService.getPlacedOrders(include)
    }

    override suspend fun getProduct(id: String): Response<ProductItem> {
        return marketService.getProduct(id = id)
    }


}