package com.example.magmarket.data.local.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.magmarket.data.local.dao.MarketDao
import com.example.magmarket.data.local.entities.ProductItemLocal

@Database(entities = [ProductItemLocal::class], version = 1, exportSchema = false)
abstract class MarketDataBase : RoomDatabase() {
    abstract fun marketDao(): MarketDao
}