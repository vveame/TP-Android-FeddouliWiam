package com.example.projectproduit.data.repository

import com.example.projectproduit.data.entities.Product
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getProducts(): List<Product> = suspendCoroutine { cont ->
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull { it.toObject(Product::class.java) }
                cont.resume(products)
            }
            .addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun getProductById(id: String): Product? = suspendCoroutine { cont ->
        firestore.collection("products")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)
                cont.resume(product)
            }
            .addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    suspend fun updateProductStock(
        id: String,
        newStock: Int,
        restockDate: Date?
    ): Unit = suspendCancellableCoroutine { cont ->
        val updates = mutableMapOf<String, Any>("stock" to newStock)
        restockDate?.let { updates["restockDate"] = it }

        firestore.collection("products")
            .document(id)
            .update(updates)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun decreaseProductStock(
        id: String,
        newStock: Int
    ): Unit = suspendCancellableCoroutine { cont ->
        firestore.collection("products")
            .document(id)
            .update("stock", newStock)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    suspend fun getProductStockById(id: String): Int = suspendCoroutine { cont ->
        firestore.collection("products").document(id)
            .get()
            .addOnSuccessListener { doc ->
                val stock = doc.getLong("stock")?.toInt() ?: 0
                cont.resume(stock)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}
