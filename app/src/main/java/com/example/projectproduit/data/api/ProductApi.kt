package com.example.projectproduit.data.api

import com.example.projectproduit.data.entities.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<Product>
}