package com.example.magmarket.data.repository

import android.util.Log
import com.example.magmarket.data.local.entities.LastProduct
import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.data.remote.safeApiCall
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarkLocalDataBase
import com.example.magmarket.di.MarketRemoteDataSource

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    @MarketRemoteDataSource private val marketremoteDataSource: RemoteDataSource,
    @MarkLocalDataBase private val markLocalDataBase: LocalDataBase
) {
     fun getSimilarProducts(include: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getSimilarProducts(include)
    }


     fun getRemoteProductList(page: Int, orderby: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getAllProduct(orderby = orderby, page = page)
    }

     fun getProduct(id: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getProduct(id)
    }

     fun getAllCategories() = safeApiCall(dispatcher) {
        marketremoteDataSource.getAllCategories()
    }

     fun getSearchProduct(search: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.searchProduct(search)
    }

     fun searchingSortedProduct(
        order: String?,
        orderBy: String?,
        search: String?
    ) = safeApiCall(dispatcher) {
        marketremoteDataSource.searchingSortedProduct(order, orderBy, search)
    }

     fun creatOrder(customer_id: Int, order: Order) = safeApiCall(dispatcher) {
        marketremoteDataSource.creatOrder(customer_id, order)
    }


     fun updateOrder(orderId: Int, order: UpdateOrder) = safeApiCall(dispatcher) {
        marketremoteDataSource.updateOrder(orderId, order)
    }

     fun deleteAnItemFromOrder(
        orderId: Int,
        order: UpdateOrder
    ) = safeApiCall(dispatcher) {
        marketremoteDataSource.deleteAnItemFromOrder(orderId, order)
    }

     fun deleteOrder(orderId: Int) = safeApiCall(dispatcher) {
        marketremoteDataSource.deleteOrder(orderId)
    }

     fun getAnOrder(orderId: Int) = safeApiCall(dispatcher) {
        Log.d("getorder", "repo:" + orderId)
        marketremoteDataSource.getAnOrder(orderId)
    }

     fun getProductComment(productId: Int) = safeApiCall(dispatcher) {
        marketremoteDataSource.getProductComment(productId)
    }

   suspend  fun getAllProductComment(productId: Int, page: Int) =
        marketremoteDataSource.getAllProductComment(productId, page)


     fun sendUserComment(review: Review) = safeApiCall(dispatcher) {
        marketremoteDataSource.sendUserComment(review)
    }

    suspend fun deleteUserComment(id: Int) = safeApiCall(dispatcher) {
        marketremoteDataSource.deleteUserComment(id)
    }

    suspend fun updateComment(id: Int, review: Review) = safeApiCall(dispatcher) {
        marketremoteDataSource.updateComment(id, review)
    }

     fun getSortedProducts() = flow {
        val response = marketremoteDataSource.getSortedProduct().body()
        emit(response!!)
    }

    suspend fun insertLastProductToLocal(product: LastProduct) {
        markLocalDataBase.insertLastProduct(product)
    }

    fun getLastProductFromLocal(): Flow<List<LastProduct>> {
        return markLocalDataBase.getLastProduct()
    }

    suspend fun deleteLastPreviewsProductFromLocal(product: LastProduct) {
        markLocalDataBase.deleteLastPreviewsProduct(product)
    }
}