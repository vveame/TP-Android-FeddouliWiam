package com.example.projectproduit.ui.order.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.order.OrderIntent
import com.example.projectproduit.ui.order.OrderViewModel
import com.example.projectproduit.ui.order.component.OrderList
import androidx.compose.runtime.getValue

@Composable
fun OrderScreen(viewModel: OrderViewModel, userId: String) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        viewModel.onIntent(OrderIntent.LoadOrders(userId))
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Text("Erreur : ${state.error}", color = MaterialTheme.colorScheme.error)
            }

            state.orders.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucune commande.")
                }
            }

            else -> {
                OrderList(orders = state.orders)
            }
        }
    }
}
