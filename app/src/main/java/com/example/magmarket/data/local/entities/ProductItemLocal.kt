package com.example.magmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart"
)
data class ProductItemLocal(
    @PrimaryKey
    @ColumnInfo
    val id:String
    ,@ColumnInfo
val count:Int)
