package com.example.magmarket.data.repository

import com.example.magmarket.data.remote.remotedatasource.MarketRemoteDataSource
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: MarketRemoteDataSource
) {
    suspend fun getRemoteProductList(orderby:String) = safeApiCall(dispatcher) {
        remoteDataSource.getAllProduct(orderby)
    }
}