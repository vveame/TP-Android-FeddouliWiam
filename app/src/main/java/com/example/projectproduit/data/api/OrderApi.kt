package com.example.projectproduit.data.api

import com.example.projectproduit.data.entities.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApi {
    @GET("orders.json")
    suspend fun getOrders(): List<Order>

    @POST
    suspend fun placeOrder(@Body order: Order): Response<Unit>
}