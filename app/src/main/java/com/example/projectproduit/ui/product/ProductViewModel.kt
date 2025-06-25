package com.example.projectproduit.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectproduit.data.entities.OrderItem
import com.example.projectproduit.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Date
import kotlin.coroutines.resumeWithException

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductViewState())
    val state: StateFlow<ProductViewState> = _state

    fun handleIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.LoadProducts -> {
                viewModelScope.launch {
                    loadProducts()
                }
            }
            is ProductIntent.LoadProductById -> {
                viewModelScope.launch { loadProductById(intent.id) }
            }
            is ProductIntent.UpdateStock -> {
                viewModelScope.launch {
                    updateStock(intent.productId, intent.newStock, intent.restockDate)
                }
            }
        }
    }

    private suspend fun loadProducts() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            Log.d("products repo", "loadProducts")
            val products = repository.getProducts()
            _state.value = ProductViewState(isLoading = false, products = products)
        } catch (e: Exception) {
            Log.d("products repo", "Exception")
            _state.value =
                ProductViewState(isLoading = false, error = e.message ?: "Error fetching products")
        }
    }

    private suspend fun loadProductById(id: String){
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            Log.d("product repo", "loadProductById")
            val product = repository.getProductById(id)
            _state.value = ProductViewState(isLoading = false, selectedProduct = product)
        } catch (e: Exception) {
            Log.d("product repo", "Exception")
            _state.value =
                ProductViewState(isLoading = false, error = e.message ?: "Error fetching products")
        }
    }

    // Public method to restock a product by adding an amount
    fun restockProduct(productId: String, amountToAdd: Int) {
        val product = _state.value.products.find { it.id == productId } ?: return
        val newStock = product.stock + amountToAdd
        val now = Date()
        // Send intent to update stock with new stock and current date as restockDate
        handleIntent(ProductIntent.UpdateStock(productId, newStock, now))
    }

    // Public method to empty the stock of a product
    fun emptyStock(productId: String) {
        // Send intent to update stock with 0 and null restockDate
        handleIntent(ProductIntent.UpdateStock(productId, 0, null))
    }

    fun reduceStockAfterOrder(orderItems: List<OrderItem>) {
        viewModelScope.launch {
            orderItems.forEach { item ->
                try {
                    val currentStock = repository.getProductStockById(item.product.id)
                    val newStock = currentStock - item.quantity
                    if (newStock >= 0) {
                        handleIntent(ProductIntent.UpdateStock(item.product.id, newStock, null))
                    } else {
                        Log.e("reduceStock", "Attempted to reduce below 0 for ${item.product.title}")
                    }
                } catch (e: Exception) {
                    Log.e("reduceStock", "Error fetching stock for ${item.product.title}: ${e.message}")
                }
            }
        }
    }

    private suspend fun updateStock(productId: String, newStock: Int, restockDate: Date?) {
        try {
            suspendCancellableCoroutine<Unit> { cont ->
                repository.updateProductStock(
                    productId,
                    newStock,
                    restockDate,
                    onSuccess = {
                        cont.resume(Unit) {}
                    },
                    onError = { e ->
                        cont.resumeWithException(e)
                    }
                )
            }

            val updatedProducts = _state.value.products.map {
                if (it.id == productId) it.copy(stock = newStock, restockDate = restockDate) else it
            }
            _state.value = _state.value.copy(products = updatedProducts, error = null)

        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message)
        }
    }
}