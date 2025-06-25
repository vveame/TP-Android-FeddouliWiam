package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("product")
    val product: Product = Product(),

    @SerializedName("quantity")
    val quantity: Int = 0,

    @SerializedName("unitPrice")
    val unitPrice: Double = 0.0, // Price at the time of order (in case price changes later)

    @SerializedName("discountApplied")
    val discountApplied: Double = 0.0 // % discount applied (frozen at order time)
)
