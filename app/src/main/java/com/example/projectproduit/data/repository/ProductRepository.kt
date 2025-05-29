package com.example.projectproduit.data.repository

import android.util.Log
import com.example.projectproduit.data.entities.Product
import com.example.projectproduit.data.api.ProductApi
import jakarta.inject.Inject


class ProductRepository @Inject constructor(
    private val api: ProductApi
) {

    suspend fun getProducts(): List<Product> {
        val products = api.getProducts()
        Log.d("products repo", "size :"+ products.size)
        return products
    }

    suspend fun getProductById(id: String): Product? {
        val products = getProducts()
        return products.find { it.productId == id }
    }
}