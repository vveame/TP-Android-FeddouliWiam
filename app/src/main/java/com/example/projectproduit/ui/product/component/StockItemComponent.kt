package com.example.projectproduit.ui.product.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Product
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun StockItem(
    product: Product,
    onRestock: (Int) -> Unit,
    onEmptyStock: () -> Unit
) {
    var restockAmountText by remember { mutableStateOf("") }
    val isValidRestockAmount = restockAmountText.toIntOrNull()?.let { it > 0 } == true

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = product.thumbnail,
                        contentDescription = product.title,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(16.dp))

                Column {
                    Text(product.title, style = MaterialTheme.typography.titleMedium)
                    Text("Stock: ${product.stock}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = restockAmountText,
                    onValueChange = { restockAmountText = it.filter(Char::isDigit) },
                    label = { Text("Stock") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isValidRestockAmount) {
                                onRestock(restockAmountText.toInt())
                                restockAmountText = ""
                            }
                        }
                    )
                )

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        val amount = restockAmountText.toIntOrNull() ?: 0
                        if (amount > 0) {
                            onRestock(amount)
                            restockAmountText = ""
                        }
                    },
                    enabled = isValidRestockAmount
                ) {
                    Text("Restock")
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = onEmptyStock,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Empty Stock", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}
