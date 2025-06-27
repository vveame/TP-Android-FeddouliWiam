package com.example.projectproduit.ui.product

import com.example.projectproduit.data.entities.OrderItem
import java.util.Date

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class LoadProductById(val id: String) : ProductIntent()
    data class RestockProduct(val productId: String, val newStock: Int, val restockDate: Date) : ProductIntent()
    data class DecreaseProductStock(val productId: String, val newStock: Int) : ProductIntent()
    data class ReduceStockAfterOrder(val orderItems: List<OrderItem>) : ProductIntent()
    data class EmptyStock(val productId: String) : ProductIntent()
}