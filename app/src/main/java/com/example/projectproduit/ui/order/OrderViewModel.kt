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
        viewModelScope.launch {
            when (intent) {
                is OrderIntent.LoadOrders -> loadOrders(intent.userId)
                is OrderIntent.PlaceOrder -> placeOrder(intent.order)
            }
        }
    }

    private suspend fun loadOrders(userId: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val orders = repository.getOrdersById(userId)
            _state.value = _state.value.copy(isLoading = false, orders = orders)
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, error = e.message)
        }
    }

    private suspend fun placeOrder(order: Order) {
        try {
            repository.placeOrder(order)
            val updatedList = _state.value.orders + order
            _state.value = _state.value.copy(orders = updatedList, error = null)
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message)
        }
    }
}