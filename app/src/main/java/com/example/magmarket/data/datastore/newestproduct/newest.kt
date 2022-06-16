package com.example.magmarket.data.datastore.newestproduct

import kotlinx.coroutines.flow.Flow

interface newest {
    suspend fun insertNewestProduct(newestProduct:NewestProduct)
    suspend fun getLastProduct(): Flow<List<NewestProduct>>
}