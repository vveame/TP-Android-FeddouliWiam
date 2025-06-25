package com.example.projectproduit.ui.order.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Order

@Composable
fun OrderItem(order: Order, onClick: (Order) -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(order) }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Order ID: ${order.orderId}", style = MaterialTheme.typography.titleMedium)
            Text("Date: ${order.orderDate}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(4.dp))

            StatusChip(status = order.status)


            Spacer(modifier = Modifier.height(8.dp))

            order.items.take(2).forEach { item ->
                Text(
                    "${item.product.title} x${item.quantity} — $ ${"%.2f".format(item.unitPrice)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (item.discountApplied > 0) {
                    Text(
                        "−${item.discountApplied.toInt()}% off",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            if (order.items.size > 2) {
                Text("+ ${order.items.size - 2} more items", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Total: $ ${"%.2f".format(order.totalAmount)}", fontWeight = FontWeight.Bold)
        }
    }
}

