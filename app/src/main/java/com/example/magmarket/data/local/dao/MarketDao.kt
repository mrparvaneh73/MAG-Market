package com.example.magmarket.data.local.dao

import androidx.room.*
import com.example.magmarket.data.local.entities.OrderList
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.entities.User
import kotlinx.coroutines.flow.Flow


@Dao
interface MarketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productItemLocal: ProductItemLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
   @Delete
   suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<User>

    @Query("SELECT * FROM orderlist")
    fun getAllOrders(): Flow<List<OrderList>>

    @Query("SELECT * FROM cart")
    fun getAllCartProduct(): Flow<List<ProductItemLocal>>

    @Query("select * from cart where id == :productId")
    fun getCartProductById(productId: Int?): Flow<ProductItemLocal>

    @Query("SELECT EXISTS(SELECT * FROM cart WHERE id = :id)")
    fun isRowIsExist(id: Int): Flow<Boolean>

    @Delete
    suspend fun deleteProductFromCart(productItemLocal: ProductItemLocal)

    @Update
    suspend fun updateProductCart(productItemLocal: ProductItemLocal)
}