package com.example.magmarket.data.datastore.newestproduct

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.magmarket.data.datastore.user.User
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.datastore.user.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val Datastore_NAME = "newestproduct"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Datastore_NAME)

@Singleton
class NewestProductDataStore @Inject constructor(@ApplicationContext context: Context) : newest {
    private val dataStore = context.dataStore

    companion object {
        val newestProductId = intPreferencesKey("newestProductId")
        val newestProductName = stringPreferencesKey("newestProductName")
    }

    override suspend fun insertNewestProduct(newestProduct: NewestProduct) {
        dataStore.edit { newestProducts ->
            newestProducts[newestProductId] = newestProduct.id
            newestProducts[newestProductName] = newestProduct.name

        }
    }

    override suspend fun getLastProduct() = dataStore.data.map { newestProducts ->
        mutableListOf(
            NewestProduct(
                id = newestProducts[newestProductId] ?: 0,
                name = newestProducts[newestProductName] ?: ""

            )
        )


    }
}