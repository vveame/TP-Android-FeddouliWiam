package com.example.projectproduit.ui.order.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Order

@Composable
fun OrderDetails(order: Order, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Order Details", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        Text("Order ID: ${order.orderId}", style = MaterialTheme.typography.bodyMedium)
        Text("Date: ${order.orderDate}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(8.dp))

        // Status badge
        StatusChip(status = order.status)

        Spacer(Modifier.height(16.dp))
        Text("Items:", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        order.items.forEach { item ->
            Card(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(item.product.title, fontWeight = FontWeight.Medium)
                    Text("Quantity: ${item.quantity}")
                    Text("Unit Price: $ ${"%.2f".format(item.unitPrice)}")

                    if (item.discountApplied > 0) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Discount: −${item.discountApplied.toInt()}%",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(8.dp))

        Text("Total: $ ${"%.2f".format(order.totalAmount)}", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(24.dp))

        Button(onClick = onBack, modifier = Modifier.align(Alignment.End)) {
            Text("Back")
        }
    }
}

