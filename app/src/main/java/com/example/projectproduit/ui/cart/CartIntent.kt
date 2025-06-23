package com.example.projectproduit.ui.cart

import com.example.projectproduit.data.entities.Product

sealed class CartIntent {
    data class AddToCart(val product: Product) : CartIntent()
    data class RemoveFromCart(val productId: String) : CartIntent()
    data class ChangeQuantity(val productId: String, val quantity: Int) : CartIntent()
    object ClearCart : CartIntent()
}