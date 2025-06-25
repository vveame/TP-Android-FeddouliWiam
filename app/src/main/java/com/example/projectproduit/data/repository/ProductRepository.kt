package com.example.projectproduit.data.repository

import android.util.Log
import com.example.projectproduit.data.entities.Product
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
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
                Log.d("ProductRepository", "Fetched products: ${products.size}")
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

    fun updateProductStock(
        productId: String,
        newStock: Int,
        restockDate: Date?,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val updates = hashMapOf<String, Any>(
            "stock" to newStock
        )
        restockDate?.let {
            updates["restockDate"] = it
        }

        firestore.collection("products")
            .document(productId)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }

    suspend fun getProductStockById(productId: String): Int = suspendCoroutine { cont ->
        firestore.collection("products").document(productId)
            .get()
            .addOnSuccessListener { doc ->
                val stock = doc.getLong("stock")?.toInt() ?: 0
                cont.resume(stock)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

}