package com.example.magmarket.data.local.localdatabase

import com.example.magmarket.data.local.dao.MarketDao
import com.example.magmarket.data.local.entities.ProductItemLocal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketLocalDataBase @Inject constructor(private val marketDao: MarketDao):LocalDataBase {
    override suspend fun insertProduct(productItemLocal: ProductItemLocal) {
        marketDao.insertProduct(productItemLocal)
    }


    override fun getAllCartProduct(): Flow<List<ProductItemLocal>> {
       return marketDao.getAllCartProduct()
    }

    override suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal) {
       marketDao.deleteProductFromCart(productItemLocal)
    }

    override suspend fun updateProductCart(productItemLocal: ProductItemLocal) {
      marketDao.updateProductCart(productItemLocal)
    }
}