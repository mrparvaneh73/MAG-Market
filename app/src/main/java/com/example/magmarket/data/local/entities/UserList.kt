package com.example.magmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userlist")
data class UserList (
    @PrimaryKey
    @ColumnInfo
    val id:Int,
    @ColumnInfo
    val email:String="",
    @ColumnInfo
    val firstName:String="",
    @ColumnInfo
    val lastName:String=""
)
