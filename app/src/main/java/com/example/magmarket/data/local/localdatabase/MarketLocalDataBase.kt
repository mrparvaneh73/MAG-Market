package com.example.magmarket.data.local.localdatabase

import com.example.magmarket.data.local.dao.MarketDao
import com.example.magmarket.data.local.entities.LastProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketLocalDataBase @Inject constructor(private val marketDao: MarketDao):LocalDataBase {

    override suspend fun insertLastProduct(product: LastProduct) {
        marketDao.insertLastProduct(product)
    }

    override fun getLastProduct(): Flow<List<LastProduct>> {
        return  marketDao.getLastProduct()
    }

    override suspend fun deleteLastPreviewsProduct(product: LastProduct) {
        marketDao.deleteLastPreviewsProduct(product)
    }
}