package com.example.magmarket.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MarketRemoteDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MarkLocalDataBase