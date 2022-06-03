package com.example.magmarket.data.repository

import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.data.remote.safeApiCall
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarkLocalDataBase
import com.example.magmarket.di.MarketRemoteDataSource

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
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
}