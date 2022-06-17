package com.example.magmarket.data.datastore.user

import kotlinx.coroutines.flow.Flow

interface UserInfo {
    suspend fun saveUser(user: User)
    suspend fun getUser():Flow<User>
}