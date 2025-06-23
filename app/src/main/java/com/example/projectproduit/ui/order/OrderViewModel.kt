package com.example.projectproduit.ui.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectproduit.data.entities.Order
import com.example.projectproduit.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderViewState())
    val state: StateFlow<OrderViewState> = _state

    fun onIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.LoadOrders -> {
                viewModelScope.launch {
                    loadOrders(intent.userId)
                }
            }
            is OrderIntent.PlaceOrder-> {
                viewModelScope.launch { placeOrder(intent.order) }
            }
        }
    }

    private suspend fun loadOrders(userId: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            Log.d("order repo", "loadOrdersById")
            val orders = repository.getOrdersById(userId)
            _state.value = OrderViewState(isLoading = false, orders = orders)
        } catch (e: Exception) {
            Log.e("order repo", "Exception", e)
            _state.value = OrderViewState(
                isLoading = false,
                error = e.message ?: "Error fetching orders"
            )
        }
    }

    private suspend fun placeOrder(order: Order) {
        try {
            repository.placeOrder(order)
            // Optionally reload orders or update UI
        } catch (e: Exception) {
            Log.e("order repo", "Order placement failed", e)
        }
    }
}