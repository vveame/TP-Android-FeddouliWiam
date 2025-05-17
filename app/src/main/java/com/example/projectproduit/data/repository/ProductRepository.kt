package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.Product
import com.example.projectproduit.R
import jakarta.inject.Inject
import kotlinx.coroutines.delay


class ProductRepository @Inject constructor() {
    suspend fun getProducts(): List<Product> {
        delay(2000)
        return listOf(
            Product(
                productId = "1",
                productTitle = "Clavier Gamer",
                productPrice = 300.00,
                productQuantity = 4,
                productImage = R.drawable.clavier
            ),
            Product(
                productId = "2",
                productTitle = "Souris Logitech",
                productPrice = 150.50,
                productQuantity = 0,
                productImage = R.drawable.souris
            ),
            Product(
                productId = "3",
                productTitle = "Écran 24",
                productPrice = 1200.00,
                productQuantity = 100,
                productImage = R.drawable.ecran
            ),
            Product(
                productId = "4",
                productTitle = "Ordinateur Portable",
                productPrice = 7000.00,
                productQuantity = 2000,
                productImage = R.drawable.laptop
            ),
            Product(
                productId = "5",
                productTitle = "Tapis de souris",
                productPrice = 40.00,
                productQuantity = 40,
                productImage = R.drawable.tapis
            )
        )
    }

    suspend fun getProductById(id: String): Product? {
        val products = getProducts()
        return products.find { it.productId == id }
    }
}