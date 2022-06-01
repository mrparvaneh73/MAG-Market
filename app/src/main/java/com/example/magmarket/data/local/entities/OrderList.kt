package com.example.magmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderlist")
data class OrderList (
    @PrimaryKey
    @ColumnInfo
    val id:Int
        )

