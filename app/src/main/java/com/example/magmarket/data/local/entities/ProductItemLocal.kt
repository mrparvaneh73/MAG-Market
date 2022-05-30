package com.example.magmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.magmarket.data.remote.model.ProductImage

@Entity(
    tableName = "cart"
)
data class ProductItemLocal(
    @PrimaryKey
    @ColumnInfo
    val id: Int,
    @ColumnInfo
    val name: String?="",
    @ColumnInfo
    val price: String?="",
    @ColumnInfo
    val images:String?="",
    @ColumnInfo
    val count: Int
)
