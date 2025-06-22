package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("product")
    val product: Product,

    @SerializedName("quantity")
    var quantity: Int
)