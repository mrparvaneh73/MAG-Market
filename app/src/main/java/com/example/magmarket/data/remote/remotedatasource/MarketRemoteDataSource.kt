package com.example.magmarket.data.remote.remotedatasource

import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.remote.network.MarketService
import com.example.magmarket.data.remote.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRemoteDataSource @Inject constructor (private val marketService: MarketService):RemoteDataSource {
    override suspend fun getAllProduct(orderby:String): Response<List<ProductItem>> {
      return marketService.getAllProduct(orderby = orderby)
    }
}