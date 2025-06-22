package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("product")
    val product: Product,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("unitPrice")
    val unitPrice: Double, // Price at the time of order (in case price changes later)

    @SerializedName("discountApplied")
    val discountApplied: Double // % discount applied (frozen at order time)
)
