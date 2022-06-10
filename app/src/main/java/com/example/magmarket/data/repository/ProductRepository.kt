package com.example.magmarket.data.repository

import android.util.Log
import com.example.magmarket.data.local.entities.LastProduct
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.entities.UserList
import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.data.remote.safeApiCall
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarkLocalDataBase
import com.example.magmarket.di.MarketRemoteDataSource

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ProductRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    @MarketRemoteDataSource private val marketremoteDataSource: RemoteDataSource,
    @MarkLocalDataBase private val markLocalDataBase: LocalDataBase
) {
    suspend fun getSimilarProducts(include: String)= safeApiCall(dispatcher) {
        marketremoteDataSource.getSimilarProducts(include)
    }
    suspend fun insertProductToCart(productItemLocal: ProductItemLocal) {
        markLocalDataBase.insertProduct(productItemLocal)
    }

    suspend fun updateOrder(productItemLocal: ProductItemLocal) {
        markLocalDataBase.updateProductCart(productItemLocal)
    }
    suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal) {
        markLocalDataBase.deleteProductFromCart(productItemLocal)
    }

    suspend fun getRemoteProductList(orderby: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getAllProduct(orderby)
    }

    suspend fun getProduct(id: String) = safeApiCall(dispatcher) {
        marketremoteDataSource.getProduct(id)
    }

    suspend fun getAllCategories() = safeApiCall(dispatcher) {
        marketremoteDataSource.getAllCategories()
    }
    suspend fun getSearchProduct(search:String)= safeApiCall(dispatcher){
        marketremoteDataSource.searchProduct(search)
    }

    fun getCartProductById(productId: Int): Flow<ProductItemLocal> {
        return  markLocalDataBase.getCartProductById(productId)
    }
     fun isRowIsExist(id:Int): Flow<Boolean> {
      return  markLocalDataBase.isRowIsExist(id)
    }

    suspend fun getProductComment(productId: Int)= safeApiCall(dispatcher){
        marketremoteDataSource.getProductComment(productId)
    }

    suspend fun sendUserComment(review: Review)= safeApiCall(dispatcher) {
        Log.d("commentmifreste", "sendUserComment: to repo ")
        marketremoteDataSource.sendUserComment(review)
    }

    suspend fun deleteUserComment(id: Int)= safeApiCall(dispatcher){
        marketremoteDataSource.deleteUserComment(id)
    }

    suspend fun updateComment(id: Int, review: Review)= safeApiCall(dispatcher) {
        marketremoteDataSource.updateComment(id,review)
    }

    fun getUsersFromLocal(): Flow<List<UserList>> {
        return markLocalDataBase.getUserFromLocal()
    }

    suspend fun getSortedProducts()= flow<List<ProductItem>>{

       val response= marketremoteDataSource.getSortedProduct().body()
        emit(response!!)
    }.flowOn(dispatcher)


     suspend fun insertLastProduct(product: LastProduct) {
       markLocalDataBase.insertLastProduct(product)
    }

     fun getLastProduct(): Flow<List<LastProduct>> {
        return  markLocalDataBase.getLastProduct()
    }

     suspend fun deleteLastPreviewsProduct(product: LastProduct) {
        markLocalDataBase.deleteLastPreviewsProduct(product)
    }

     suspend fun updateLastProduct(lastProduct: LastProduct){
         markLocalDataBase.updateLastProduct(lastProduct)
     }
}