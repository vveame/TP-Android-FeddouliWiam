package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.Product
import com.example.projectproduit.data.api.ProductApi
import jakarta.inject.Inject


class ProductRepository @Inject constructor(
    private val api: ProductApi
) {

    suspend fun getProducts(): List<Product> {
        return api.getProducts()
    }

    suspend fun getProductById(id: String): Product? {
        val products = getProducts()
        return products.find { it.productId == id }
    }
}