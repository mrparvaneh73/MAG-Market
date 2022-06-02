package com.example.magmarket.di

import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.deserializer.CategoryDeserializer
import com.example.magmarket.data.remote.network.MarketService
import com.example.magmarket.data.remote.deserializer.ProductDeserializer
import com.example.magmarket.data.remote.network.RemoteDataSource
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
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
            .registerTypeAdapter(
                object : TypeToken<List<CategoryItem>>() {

                }.type,
                CategoryDeserializer()
            )
            .create()

    }


    @Provides
    fun provideMarketService(
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

    @Singleton
    @Provides
    @MarketRemoteDataSource
    fun provideRemoteDataSource(): RemoteDataSource =
        com.example.magmarket.data.remote.remotedatasource.MarketRemoteDataSource(
            provideMarketService(provideOkHttpClient(), gson())
        )
}