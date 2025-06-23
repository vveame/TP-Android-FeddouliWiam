package com.example.projectproduit.ui.order.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Order

@Composable
fun OrderItem(order: Order, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text("Order ID: ${order.orderId}", style = MaterialTheme.typography.titleMedium)
        Text("Date: ${order.orderDate}", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(8.dp))

        order.items.forEach { item ->
            Text("${item.product.productTitle} x${item.quantity} — ${"%.2f".format(item.unitPrice)} DH (−${item.discountApplied.toInt()}%)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Total: ${"%.2f".format(order.totalAmount)} DH", fontWeight = FontWeight.Bold)
        Text("Status: ${order.status.name}", color = MaterialTheme.colorScheme.primary)
    }
}
