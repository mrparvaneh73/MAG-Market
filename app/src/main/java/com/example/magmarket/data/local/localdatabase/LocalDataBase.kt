package com.example.magmarket.data.local.localdatabase

import com.example.magmarket.data.local.entities.ProductItemLocal
import kotlinx.coroutines.flow.Flow

interface LocalDataBase {
    suspend fun insertProduct(productItemLocal: ProductItemLocal)

    fun getAllCartProduct(): Flow<List<ProductItemLocal>>

    suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal)

    suspend fun updateProductCart(productItemLocal: ProductItemLocal)

    fun getCartProductById(productId: Int): Flow<ProductItemLocal>

    fun isRowIsExist(id: Int): Flow<Boolean>

}