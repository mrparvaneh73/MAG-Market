package com.example.magmarket.data.remote.model.updateorder

import com.example.magmarket.data.remote.model.order.MetaData

data class UpdateLineItem(
    val id: Int?=0,
   val meta_data: List<MetaData> = emptyList(),
//    val name: String,
//    val parent_name: Any,
//    val price: Int,
    val product_id: Int?=0,
    val quantity: Int?=0,
//    val sku: String,
//    val subtotal: String,
//    val subtotal_tax: String,
//    val tax_class: String,
//    val taxes: List<Any>,
//    val total: String,
//    val total_tax: String,
//    val variation_id: Int
)