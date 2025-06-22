package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Product(
    @SerializedName("id")
    val productId: String,

    @SerializedName("title")
    val productTitle: String,

    @SerializedName("description")
    val productDescription: String?,

    @SerializedName("price")
    val productPrice: Double,

    @SerializedName("discountPercentage")
    val discountPercentage: Double?,

    @SerializedName("rating")
    val productRating: Double?,

    @SerializedName("stock")
    val productStock: Int,

    @SerializedName("restockDate")
    val restockDate: Date?,

    @SerializedName("brand")
    val productBrand: String,

    @SerializedName("category")
    val productCategory: String,

    @SerializedName("thumbnail")
    val productThumbnail: String,

    @SerializedName("images")
    val productImages: List<String>
)
