package com.example.magmarket.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductCategory(val id: String, val name: String):Parcelable
