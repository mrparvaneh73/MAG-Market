package com.example.magmarket.di

import android.content.Context
import androidx.room.Room
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.local.localdatabase.LocalDataBase
import com.example.magmarket.data.local.localdatabase.MarketDataBase
import com.example.magmarket.data.local.localdatabase.MarketLocalDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun dataBase(@ApplicationContext context: Context): MarketDataBase = Room.databaseBuilder(
        context,
        MarketDataBase::class.java, "user"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDataStore(@ApplicationContext context: Context)=UserDataStore(context)
    @Provides
    @Singleton
    fun dao(db: MarketDataBase) = db.marketDao()

    @Provides
    @MarkLocalDataBase
    fun provideLocalDataBase(@ApplicationContext context: Context): LocalDataBase =
        MarketLocalDataBase(dao(dataBase(context)))
}