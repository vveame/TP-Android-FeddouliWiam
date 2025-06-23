package com.example.projectproduit.ui.order

import com.example.projectproduit.data.entities.Order

sealed class OrderIntent {
    data class LoadOrders(val userId: String) : OrderIntent()
    data class PlaceOrder(val order: Order) : OrderIntent()
}