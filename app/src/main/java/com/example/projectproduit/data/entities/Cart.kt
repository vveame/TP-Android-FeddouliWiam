package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("items")
    val items: MutableList<CartItem> = mutableListOf()
)
