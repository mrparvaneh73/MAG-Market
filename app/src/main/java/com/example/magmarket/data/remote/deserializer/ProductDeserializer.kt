package com.example.magmarket.data.remote.deserializer

import com.example.magmarket.data.model.ProductCategory
import com.example.magmarket.data.model.ProductImage
import com.example.magmarket.data.model.ProductItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class ProductDeserializer : JsonDeserializer<List<ProductItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<ProductItem> {
        val productItem = mutableListOf<ProductItem>()
        val productImage = mutableListOf<ProductImage>()
        val ProductCategory = mutableListOf<ProductCategory>()
        json?.asJsonArray?.let { jsonArray ->
            for (element in jsonArray) {
                element.asJsonObject.let {
                    val id = it.get("id").asString
                    val name = it.get("name").asString
                    val description = it.get("description").asString
                    val price = it.get("price").asString
                    val categories = it.get("categories").asJsonArray
                    for (i in 0..categories.size()) {
                        val categoryObject: JsonObject = categories.get(i).getAsJsonObject()
                        val id = categoryObject.get("id").asString
                        val name = categoryObject.get("name").asString
                        val category = ProductCategory(id, name)
                        ProductCategory.add(category)
                    }
                    val images = it.get("images").asJsonArray
                    for (i in 0..images.size()) {
                        val imageObject: JsonObject = images.get(i).getAsJsonObject()
                        val id = imageObject.get("id").asString
                        val src = imageObject.get("src").asString
                        val name = imageObject.get("name").asString
                        val image = ProductImage(id, src, name)
                        productImage.add(image)
                    }
                    ProductItem(
                        id = id,
                        name = name,
                        description = description,
                        price = price,
                        categories = ProductCategory,
                        images = productImage
                    ).let { product -> productItem.add(product) }

                }
            }
        }
        return productItem
    }
}