package com.example.magmarket.data.local.dao

import androidx.room.*
import com.example.magmarket.data.local.entities.ProductItemLocal
import kotlinx.coroutines.flow.Flow


@Dao
interface MarketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productItemLocal: ProductItemLocal)

    @Query("SELECT * FROM cart")
    fun getAllCartProduct(): Flow<List<ProductItemLocal>>


    @Delete
    suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal)

    @Update
    suspend fun updateProductCart(productItemLocal: ProductItemLocal)
}