package com.example.magmarket.data.datastore.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val Datastore_NAME = "USER"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Datastore_NAME)

@Singleton
class UserDataStore @Inject constructor(@ApplicationContext context: Context) : UserInfo {
    private val dataStore = context.dataStore

    companion object {
        val userId = intPreferencesKey("USERID")
        val email= stringPreferencesKey("EMAIL")
        val userFirstName = stringPreferencesKey("FIRSTNAME")
        val userLastName = stringPreferencesKey("LASTNAME")
        val login = booleanPreferencesKey("LOGIN")
        val orderId = intPreferencesKey("ORDERID")
    }

    override suspend fun saveUser(user: User) {
        dataStore.edit { users ->
            users[userId]=user.userId
            users[email]=user.email
            users[userFirstName]= user.firstName
            users[userLastName]=user.lastName
            users[login]=user.isLogin
            users[orderId]=user.myorderId

        }
    }

    override suspend fun getUser()=dataStore.data.map { users ->
        User(
            userId =  users[userId] ?:0,
            email =  users[email] ?:"",
            firstName = users[userFirstName] ?:"",
            lastName = users[userLastName] ?:"",
            myorderId = users[orderId] ?:0,
            isLogin = users[login] ?: false

        )

    }
}