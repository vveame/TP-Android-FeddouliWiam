package com.example.projectproduit.ui.order.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.OrderStatus

@Composable
fun StatusChip(status: OrderStatus) {
    val (color, text) = when (status) {
        OrderStatus.PENDING -> MaterialTheme.colorScheme.secondary to "Pending"
        OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.tertiary to "Confirmed"
        OrderStatus.SHIPPED -> MaterialTheme.colorScheme.primary to "Shipped"
        OrderStatus.DELIVERED -> MaterialTheme.colorScheme.primary to "Delivered"
        OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error to "Cancelled"
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, color),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}