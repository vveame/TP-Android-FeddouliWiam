package com.example.projectproduit.ui.cart

import com.example.projectproduit.data.entities.CartItem

data class CartViewState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)