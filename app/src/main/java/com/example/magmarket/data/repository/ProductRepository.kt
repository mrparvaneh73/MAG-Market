package com.example.magmarket.data.repository

import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarketRemoteDataSource
import com.example.magmarket.utils.safeApiCall
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@ActivityRetainedScoped
class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
   @MarketRemoteDataSource private val marketremoteDataSource: RemoteDataSource
) {

    suspend fun getRemoteProductList(orderby: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getAllProduct(orderby)
    }

    suspend fun getProduct(id:String)= safeApiCall(dispatcher){
        marketremoteDataSource.getProduct(id)
    }

    suspend fun getAllCategories()= safeApiCall(dispatcher) {
       marketremoteDataSource.getAllCategories()
    }
}