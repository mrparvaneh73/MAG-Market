package com.example.magmarket.data.local.localdatabase

import com.example.magmarket.data.local.dao.MarketDao
import com.example.magmarket.data.local.entities.OrderList
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.entities.UserList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

    override suspend fun insertOrder(order: OrderList) {
        marketDao.insertOrder(order)
    }

    override fun getAllOrders(): Flow<List<OrderList>> {
       return marketDao.getAllOrders()
    }

    override fun getCartProductById(productId: Int): Flow<ProductItemLocal> {
      return  marketDao.getCartProductById(productId)
    }

    override  fun isRowIsExist(id: Int): Flow<Boolean> {
     return   marketDao.isRowIsExist(id)
    }

    override suspend fun insertUser(user: UserList) {
        marketDao.insertUser(user)
    }

    override fun getAllUsers(): Flow<List<UserList>> {
        return marketDao.getAllUsers()
    }

    override suspend fun deleteUser(user: UserList) {
        marketDao.deleteUser(user)
    }

    override suspend fun updateUser(user: UserList) {
        marketDao.updateUser(user)
    }
}