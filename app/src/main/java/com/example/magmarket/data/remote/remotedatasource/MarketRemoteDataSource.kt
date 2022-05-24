package com.example.magmarket.data.remote.remotedatasource

import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.data.remote.network.MarketService
import com.example.magmarket.data.remote.network.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRemoteDataSource @Inject constructor (private val marketService: MarketService):RemoteDataSource {
    override suspend fun getAllProduct(orderby:String): Response<List<ProductRecyclerViewItem.ProductItem>> {
      return marketService.getAllProduct(orderby = orderby)
    }

    override suspend fun getAllCategories(): Response<List<CategoryItem>> {
        return marketService.getAllCategories()
    }

    override suspend fun getSubCategories(parent: Int): Response<List<CategoryItem>> {
        return  marketService.getSubCategories(parent)
    }

    override suspend fun getproductOfCategory(categoryId: Int): Response<List<ProductItem>> {
        return marketService.getProductOfCategory(categoryId)
    }

    override suspend fun getProduct(id: String): Response<ProductItem> {
        return marketService.getProduct(id = id)
    }
}