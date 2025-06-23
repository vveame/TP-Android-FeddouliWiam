package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Product(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("price")
    var price: Double = 0.0,

    @SerializedName("discountPercentage")
    var discountPercentage: Double? = null,

    @SerializedName("rating")
    var rating: Double? = null,

    @SerializedName("stock")
    var stock: Int = 0,

    @SerializedName("restockDate")
    var restockDate: Date? = null,

    @SerializedName("brand")
    var brand: String = "",

    @SerializedName("category")
    var category: String = "",

    @SerializedName("thumbnail")
    var thumbnail: String = "",

    @SerializedName("images")
    var images: List<String> = emptyList()
)
