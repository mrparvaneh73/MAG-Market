package com.example.magmarket.data.remote.model.order

data class LineItemX(
    val id: Int,
////    val meta_data: List<MetaData>,
//    val name: String,
    val price: Double,
    val product_id: Int,
    val quantity: Int,
//    val sku: String,
    val subtotal: String,
//    val subtotal_tax: String,
//    val tax_class: String,
//    val taxes: List<Taxe>,
//    val total: String,
//    val total_tax: String,
//    val variation_id: Int
)