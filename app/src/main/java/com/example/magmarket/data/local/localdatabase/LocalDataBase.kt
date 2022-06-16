package com.example.magmarket.data.local.localdatabase

import com.example.magmarket.data.local.entities.LastProduct

import kotlinx.coroutines.flow.Flow

interface LocalDataBase {
    suspend fun insertLastProduct(product: LastProduct)
    fun getLastProduct(): Flow<List<LastProduct>>
    suspend fun deleteLastPreviewsProduct(product: LastProduct)

}