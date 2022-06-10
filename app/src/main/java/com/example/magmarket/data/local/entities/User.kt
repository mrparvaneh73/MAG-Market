package com.example.magmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo
    val userId: Int=0,
    @ColumnInfo
    val email: String = "",
    @ColumnInfo
    val firstName: String = "",
    @ColumnInfo
    val lastName: String = "",
    @ColumnInfo
    val orderId: Int = 0,
    @ColumnInfo
    val orderStatus: String = "",
    @ColumnInfo
    val productId: Int = 0
)
