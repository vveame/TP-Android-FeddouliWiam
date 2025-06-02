package com.example.projectproduit.ui.product

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class LoadProductById(val id: String) : ProductIntent()
}