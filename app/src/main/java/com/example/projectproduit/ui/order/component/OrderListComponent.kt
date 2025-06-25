package com.example.projectproduit.ui.order.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Order
import androidx.compose.foundation.lazy.items

@Composable
fun OrderList(orders: List<Order>,  onOrderClick: (Order) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(orders, key = { it.orderId }) { order ->
            OrderItem(order = order, onClick = onOrderClick)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}