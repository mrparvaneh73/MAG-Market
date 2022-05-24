package com.example.magmarket.data.remote.network

import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.utils.Constants
import retrofit2.Response
import retrofit2.http.QueryMap

interface RemoteDataSource {
    suspend fun getAllProduct(orderby:String): Response<List<ProductRecyclerViewItem.ProductItem>>
    suspend fun getProduct(id:String):Response<ProductItem>

    suspend fun getAllCategories(): Response<List<CategoryItem>>
    suspend fun getSubCategories(parent:Int):Response<List<CategoryItem>>
    suspend fun getproductOfCategory(categoryId:Int):Response<List<ProductItem>>
}