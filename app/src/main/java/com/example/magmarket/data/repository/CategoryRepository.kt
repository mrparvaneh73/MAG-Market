package com.example.magmarket.data.repository

import com.example.magmarket.data.remote.network.RemoteDataSource

import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarketRemoteDataSource
import com.example.magmarket.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    @MarketRemoteDataSource
   private val marketRemoteDataSource: RemoteDataSource,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getSubCategories(parent:Int)= safeApiCall(dispatcher){
        marketRemoteDataSource.getSubCategories(parent)
    }
}