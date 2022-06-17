package com.example.magmarket.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductImage(val id:String, val src:String , val name:String):Parcelable
