package com.example.magmarket.data.repository


import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.data.remote.safeApiCall
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarkLocalDataBase
import com.example.magmarket.di.MarketRemoteDataSource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    @MarketRemoteDataSource private val marketremoteDataSource: RemoteDataSource,
    @MarkLocalDataBase private val markLocalDataBase: LocalDataBase
) {
    suspend fun getProductFromRemote(include: String)= safeApiCall(dispatcher) {
         marketremoteDataSource.getSimilarProducts(include)
    }


    suspend fun creatOrder(customer_id:Int,order: Order) = safeApiCall(dispatcher) {
        marketremoteDataSource.creatOrder(customer_id,order)
    }

    suspend fun getPlacedOrder(customer_id:Int) = safeApiCall(dispatcher) {
        marketremoteDataSource.getPlacedOrder(customer_id)
    }

    suspend fun getCustomer(id:Int)= safeApiCall(dispatcher){
        marketremoteDataSource.getCustomer(id)
    }


    suspend fun getAnOrder(orderId: Int)= safeApiCall(dispatcher){
        marketremoteDataSource.getAnOrder(orderId)
    }
    suspend fun updateOrder(orderId: Int, order: UpdateOrder) = safeApiCall(dispatcher) {
        marketremoteDataSource.updateOrder(orderId,order)
    }
}