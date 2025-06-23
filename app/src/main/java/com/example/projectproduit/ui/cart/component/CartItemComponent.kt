package com.example.projectproduit.ui.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.CartItem

@Composable
fun CartItem(
    item: CartItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    val discountedPrice = item.product.productPrice * (1 - (item.product.discountPercentage ?: 0.0) / 100)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = item.product.productTitle, style = MaterialTheme.typography.titleMedium)

            Row {
                if (item.product.discountPercentage != null && item.product.discountPercentage > 0.0) {
                    Text(
                        text = "${"%.2f".format(discountedPrice)} DH",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${"%.2f".format(item.product.productPrice)} DH",
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "${"%.2f".format(item.product.productPrice)} DH",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Text(text = "x ${item.quantity}")
        }
        Row {
            IconButton(onClick = { onQuantityChange(item.quantity - 1) }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
            }
            IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

