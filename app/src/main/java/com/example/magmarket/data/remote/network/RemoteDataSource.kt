package com.example.magmarket.data.remote.network

import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import retrofit2.Response
import java.util.ArrayList

interface RemoteDataSource {
    suspend fun getAllProduct(orderby:String): Response<List<ProductRecyclerViewItem.ProductItem>>
    suspend fun getProduct(id:String):Response<ProductItem>

    suspend fun getAllCategories(): Response<List<CategoryItem>>
    suspend fun getSubCategories(parent:Int):Response<List<CategoryItem>>
    suspend fun getproductOfCategory(categoryId:Int):Response<List<ProductItem>>

    suspend fun getAllOrders(include:String): Response<List<ProductItem>>
}