package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.Order

import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getOrdersById(userId: String): List<Order> = suspendCoroutine { cont ->
        firestore.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val orders = snapshot.toObjects(Order::class.java)
                cont.resume(orders)
            }
            .addOnFailureListener { e ->
                cont.resumeWithException(e)
            }
    }

    suspend fun placeOrder(order: Order): Unit = suspendCancellableCoroutine { cont ->
        firestore.collection("orders")
            .add(order)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }
}
