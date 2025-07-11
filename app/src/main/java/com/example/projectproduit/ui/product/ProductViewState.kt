package com.example.projectproduit.ui.product

import com.example.projectproduit.data.entities.Product

data class  ProductViewState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val selectedProduct: Product? = null,
    val error: String? = null
)