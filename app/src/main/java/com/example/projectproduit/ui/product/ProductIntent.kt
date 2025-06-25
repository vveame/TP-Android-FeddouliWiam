package com.example.projectproduit.ui.product

import java.util.Date

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class LoadProductById(val id: String) : ProductIntent()
    data class UpdateStock(val productId: String, val newStock: Int, val restockDate: Date? = null) : ProductIntent()
}