package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("ID")
    val productId: String,

    @SerializedName("Title")
    val productTitle: String,

    @SerializedName("Price")
    val productPrice: Double,

    @SerializedName("Quantity")
    val productQuantity: Int,

    @SerializedName("Image")
    val productImage: Int
)