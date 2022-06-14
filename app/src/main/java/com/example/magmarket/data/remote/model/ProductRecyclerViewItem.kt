package com.example.magmarket.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ProductRecyclerViewItem{

    class ProductItem(val id:String,val name:String?,val description:String?,val sale_price: String?
    ,val price:String?="",val categories:List<ProductCategory>?,val images:List<ProductImage>?,val regular_price:String?="",val related_ids: List<Int>? = emptyList()):ProductRecyclerViewItem()

    class ShowAll(val title:String):ProductRecyclerViewItem()

    class HeaderProductTitle ( val title: Int):ProductRecyclerViewItem()
}
