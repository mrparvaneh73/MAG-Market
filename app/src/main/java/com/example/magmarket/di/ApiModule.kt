package com.example.magmarket.di

import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.remote.network.MarketService
import com.example.magmarket.data.remote.network.ProductDeserializer
import com.example.magmarket.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @IoDispatcher
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder().build()
                chain.proceed(request)
            })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    }

    @Provides
    fun gson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<List<ProductItem>>() {
                }.type,
                ProductDeserializer()
            )
            .create()

    }

    @Provides
    fun provideGithubService(
        client: OkHttpClient,
        gson: Gson
    ): MarketService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MarketService::class.java)
    }
}