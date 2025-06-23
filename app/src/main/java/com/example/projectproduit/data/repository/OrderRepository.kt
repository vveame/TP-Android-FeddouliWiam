package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.Order

import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getOrdersById(userId: String, onResult: (List<Order>) -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val orders = snapshot.toObjects(Order::class.java)
                onResult(orders)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    fun placeOrder(order: Order, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        firestore.collection("orders")
            .add(order)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }
}
