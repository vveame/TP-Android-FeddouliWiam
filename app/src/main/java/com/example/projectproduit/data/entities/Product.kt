package com.example.projectproduit.data.entities

data class Product(
    val productId: String,
    val productTitle: String,
    val productPrice: Double,
    val productQuantity: Int,
    val productImage: Int
)