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
import java.util.Date

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductViewState())
    val state: StateFlow<ProductViewState> = _state

    fun handleIntent(intent: ProductIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProductIntent.LoadProducts -> loadProducts()
                is ProductIntent.LoadProductById -> loadProductById(intent.id)
                is ProductIntent.RestockProduct -> restockProduct(intent.productId, intent.newStock, intent.restockDate)
                is ProductIntent.DecreaseProductStock -> decreaseStock(intent.productId, intent.newStock)
            }
        }
    }

    private suspend fun loadProducts() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val products = repository.getProducts()
            _state.value = ProductViewState(isLoading = false, products = products)
        } catch (e: Exception) {
            _state.value = ProductViewState(isLoading = false, error = e.message ?: "Error fetching products")
        }
    }

    private suspend fun loadProductById(id: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val product = repository.getProductById(id)
            _state.value = ProductViewState(isLoading = false, selectedProduct = product)
        } catch (e: Exception) {
            _state.value = ProductViewState(isLoading = false, error = e.message ?: "Error fetching product")
        }
    }

    private suspend fun restockProduct(productId: String, newStock: Int, restockDate: Date) {
        try {
            repository.updateProductStock(productId, newStock, restockDate)
            _state.value = _state.value.copy(
                products = _state.value.products.map {
                    if (it.id == productId) it.copy(stock = newStock, restockDate = restockDate) else it
                },
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message)
        }
    }

    private suspend fun decreaseStock(productId: String, newStock: Int) {
        try {
            repository.decreaseProductStock(productId, newStock)
            _state.value = _state.value.copy(
                products = _state.value.products.map {
                    if (it.id == productId) it.copy(stock = newStock) else it
                },
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message)
        }
    }

    fun reduceStockAfterOrder(orderItems: List<OrderItem>) {
        viewModelScope.launch {
            for (item in orderItems) {
                try {
                    val currentStock = repository.getProductStockById(item.product.id)
                    val newStock = currentStock - item.quantity
                    if (newStock >= 0) {
                        handleIntent(ProductIntent.DecreaseProductStock(item.product.id, newStock))
                    } else {
                        Log.e("reduceStock", "Attempted to reduce below 0 for ${item.product.title}")
                    }
                } catch (e: Exception) {
                    Log.e("reduceStock", "Error fetching stock for ${item.product.title}: ${e.message}")
                }
            }
        }
    }

    fun restock(productId: String, amountToAdd: Int) {
        val product = _state.value.products.find { it.id == productId } ?: return
        val newStock = product.stock + amountToAdd
        handleIntent(ProductIntent.RestockProduct(productId, newStock, Date()))
    }

    fun emptyStock(productId: String) {
        handleIntent(ProductIntent.RestockProduct(productId, 0, Date()))
    }
}