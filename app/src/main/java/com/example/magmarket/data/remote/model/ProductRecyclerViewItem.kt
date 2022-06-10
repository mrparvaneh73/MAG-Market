package com.example.magmarket.data.remote.model

sealed class ProductRecyclerViewItem{
    class ProductItem(val id:String,val name:String?,val description:String?
    ,val price:String?="",val categories:List<ProductCategory>?,val images:List<ProductImage>?,val regular_price:String?=""):ProductRecyclerViewItem()

    class ShowAll(val title:String):ProductRecyclerViewItem()

    class HeaderProductTitle ( val title: Int):ProductRecyclerViewItem()
}
