package com.example.magmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lastProduct")
data class LastProduct(
    @PrimaryKey
    @ColumnInfo
    val id:Int,
    @ColumnInfo
    val name:String
)
