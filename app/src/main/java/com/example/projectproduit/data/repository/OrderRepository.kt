package com.example.projectproduit.data.repository

import com.example.projectproduit.data.api.OrderApi
import com.example.projectproduit.data.entities.Order
import jakarta.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi
) {

    suspend fun getOrdersById(userId: String): List<Order> {
        val orders = api.getOrders()
        return orders.filter { it.userId == userId }
    }

    suspend fun placeOrder(order: Order) {
        try {
            val response = api.placeOrder(order)
            if (!response.isSuccessful) {
                throw Exception("Failed to place order: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Order placement failed: ${e.message}")
        }
    }
}