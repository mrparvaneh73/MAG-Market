package com.example.magmarket.data.local.localdatabase

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.magmarket.data.local.entities.OrderList
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.entities.UserList
import kotlinx.coroutines.flow.Flow

interface LocalDataBase {
    suspend fun insertProduct(productItemLocal: ProductItemLocal)

    fun getAllCartProduct(): Flow<List<ProductItemLocal>>

    suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal)

    suspend fun updateProductCart(productItemLocal: ProductItemLocal)

    suspend fun insertOrder(order: OrderList)

    fun getAllOrders(): Flow<List<OrderList>>

    fun getCartProductById(productId: Int): Flow<ProductItemLocal>

    fun isRowIsExist(id: Int): Flow<Boolean>

    suspend fun insertUser(user: UserList)

    fun getUserFromLocal(): Flow<List<UserList>>

    suspend fun deleteUser(user: UserList)

    suspend fun updateUser(user: UserList)

}