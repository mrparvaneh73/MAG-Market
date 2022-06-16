package com.example.magmarket.data.local.dao

import androidx.room.*
import com.example.magmarket.data.local.entities.LastProduct


import kotlinx.coroutines.flow.Flow


@Dao
interface MarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastProduct(product: LastProduct)

    @Query("SELECT * FROM lastProduct")
    fun getLastProduct(): Flow<List<LastProduct>>

    @Delete
    suspend fun deleteLastPreviewsProduct(product: LastProduct)


}