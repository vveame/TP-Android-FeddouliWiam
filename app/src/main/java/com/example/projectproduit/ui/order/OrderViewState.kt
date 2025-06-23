package com.example.projectproduit.ui.order

import com.example.projectproduit.data.entities.Order

data class OrderViewState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
