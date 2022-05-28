package com.example.magmarket.data.deserializer

import com.example.magmarket.data.remote.model.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CategoryDeserializer : JsonDeserializer<List<CategoryItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<CategoryItem> {
        val categoryItem = mutableListOf<CategoryItem>()

        json?.asJsonArray?.let { jsonArray ->
            for (element in jsonArray) {
                element.asJsonObject.let {
                    val id = it.get("id").asInt
                    val name = it.get("name").asString
                    val parent=it.get("parent").asInt
                    val imageobject = it.get("image").asJsonObject as Image
                    val count=it.get("count").asInt

                    CategoryItem(
                        id = id,
                        name = name,
                        image = imageobject,
                        parent = parent,
                        count = count
                    ).let { category -> categoryItem.add(category) }

                }
            }
        }
        return categoryItem
    }
}