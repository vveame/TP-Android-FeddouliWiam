package com.example.projectproduit.ui.cart

import androidx.lifecycle.ViewModel
import com.example.projectproduit.data.entities.CartItem
import com.example.projectproduit.data.entities.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _state = MutableStateFlow(CartViewState())
    val state: StateFlow<CartViewState> = _state

    fun onIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.AddToCart -> {
                val updatedItems = _state.value.items.toMutableList()
                val index = updatedItems.indexOfFirst { it.product.productId == intent.product.productId }
                if (index != -1) {
                    updatedItems[index] = updatedItems[index].copy(quantity = updatedItems[index].quantity + 1)
                } else {
                    updatedItems.add(CartItem(intent.product, 1))
                }
                updateState(updatedItems)
            }

            is CartIntent.RemoveFromCart -> {
                val updatedItems = _state.value.items.filterNot { it.product.productId == intent.productId }
                updateState(updatedItems)
            }

            is CartIntent.ChangeQuantity -> {
                val updatedItems = _state.value.items.mapNotNull {
                    if (it.product.productId == intent.productId) {
                        if (intent.quantity > 0) it.copy(quantity = intent.quantity) else null
                    } else it
                }
                updateState(updatedItems)
            }

            is CartIntent.ClearCart -> {
                updateState(emptyList())
            }
        }
    }

    private fun updateState(newItems: List<CartItem>) {
        val total = newItems.sumOf { getDiscountedPrice(it.product) * it.quantity }
        _state.update { it.copy(items = newItems, totalPrice = total) }
    }

    private fun getDiscountedPrice(product: Product): Double {
        val discount = product.discountPercentage ?: 0.0
        return product.productPrice * (1 - discount / 100)
    }
}