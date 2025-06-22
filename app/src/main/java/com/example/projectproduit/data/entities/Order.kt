package com.example.projectproduit.data.entities

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Order(
    @SerializedName("orderId")
    val orderId: String,

    @SerializedName("orderDate")
    val orderDate: Date = Date(),

    @SerializedName("items")
    val items: List<OrderItem>,

    @SerializedName("totalAmount")
    val totalAmount: Double, // Includes product prices and shipping fee

    @SerializedName("shippingFee")
    val shippingFee: Double = 0.0,

    @SerializedName("deliveryAddress")
    val deliveryAddress: Address,

    @SerializedName("paymentMethod")
    val paymentMethod: PaymentMethod,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("status")
    val status: OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

enum class PaymentMethod {
    CASH_ON_DELIVERY,
    CREDIT_CARD,
    PAYPAL,
    STRIPE
}

