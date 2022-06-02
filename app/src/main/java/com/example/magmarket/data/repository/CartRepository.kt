package com.example.magmarket.data.repository

import com.example.magmarket.data.local.entities.OrderList
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.entities.UserList
import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarkLocalDataBase
import com.example.magmarket.di.MarketRemoteDataSource
import com.example.magmarket.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    @MarketRemoteDataSource private val marketremoteDataSource: RemoteDataSource,
    @MarkLocalDataBase private val markLocalDataBase: LocalDataBase
) {
    suspend fun getAllOrders(include: String)= safeApiCall(dispatcher) {
         marketremoteDataSource.getAllOrders(include)
    }

     fun getAllCartProductFromLocal(): Flow<List<ProductItemLocal>> {
        return markLocalDataBase.getAllCartProduct()
    }

     fun getCartProductById(productId: Int): Flow<ProductItemLocal> {
        return  markLocalDataBase.getCartProductById(productId)
    }

     suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal) {
         markLocalDataBase.deleteProductFromCart(productItemLocal)
    }
    suspend fun creatOrder(customer_id:Int,order: Order) = safeApiCall(dispatcher) {
        marketremoteDataSource.creatOrder(customer_id,order)
    }
     suspend fun updateProductCart(productItemLocal: ProductItemLocal) {
         markLocalDataBase.updateProductCart(productItemLocal)
    }

    suspend fun insertOrder(order: OrderList) {
        markLocalDataBase.insertOrder(order)
    }

    suspend fun getPlacedOrder(include: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getPlacedOrder(include)
    }

    fun getAllPlacedOrders(): Flow<List<OrderList>> {
        return markLocalDataBase.getAllOrders()
    }
    suspend fun getCustomer(id:Int)= safeApiCall(dispatcher){
        marketremoteDataSource.getCustomer(id)
    }

    fun getUsersFromLocal(): Flow<List<UserList>> {
        return markLocalDataBase.getUserFromLocal()
    }
}