package com.example.magmarket.data.repository

import com.example.magmarket.data.local.entities.User
import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.network.RemoteDataSource
import com.example.magmarket.data.remote.safeApiCall
import com.example.magmarket.di.IoDispatcher
import com.example.magmarket.di.MarkLocalDataBase
import com.example.magmarket.di.MarketRemoteDataSource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    @MarketRemoteDataSource private val marketremoteDataSource: RemoteDataSource,
    @MarkLocalDataBase private val markLocalDataBase: LocalDataBase
) {

    suspend fun createCustomer(customer: Customer) = safeApiCall(dispatcher) {
        marketremoteDataSource.createCustomer(customer)
    }

    suspend fun getCustomer(id: Int) = safeApiCall(dispatcher) {
        marketremoteDataSource.getCustomer(id)
    }

    suspend fun insertUser(user: User) {
        markLocalDataBase.insertUser(user)
    }

    fun getUsersFromLocal(): Flow<User> {
        return markLocalDataBase.getUserFromLocal()
    }

    suspend fun deleteUser(user: User) {
        markLocalDataBase.deleteUser(user)
    }

    suspend fun updateCustomer(id: Int, customer: Customer) = safeApiCall(dispatcher) {
        marketremoteDataSource.updateCustomer(id, customer)
    }

    suspend fun updateUserLocal(user: User) {
        markLocalDataBase.updateUser(user)
    }
}