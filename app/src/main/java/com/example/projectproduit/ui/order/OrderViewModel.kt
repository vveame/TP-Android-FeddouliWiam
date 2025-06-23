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

    fun loadOrders(userId: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        repository.getOrdersById(
            userId,
            onResult = { orders ->
                _state.value = OrderViewState(orders = orders)
            },
            onError = { e ->
                _state.value = OrderViewState(error = e.message)
            }
        )
    }

    fun placeOrder(order: Order) {
        repository.placeOrder(
            order,
            onSuccess = {
                // You can update the state if needed
            },
            onError = { e ->
                // Handle error
            }
        )
    }
}