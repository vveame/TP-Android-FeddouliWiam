package com.example.projectproduit.ui.cart.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.cart.CartIntent
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.projectproduit.ui.cart.component.CartItem

@Composable
fun CartScreen(viewModel: CartViewModel,
               navToCheckout: () -> Unit) {

    val cartState by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (cartState.items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty", style = MaterialTheme.typography.headlineMedium)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartState.items.size) { index ->
                    val item = cartState.items[index]
                    CartItem(
                        item = item,
                        onRemove = { viewModel.onIntent(CartIntent.RemoveFromCart(item.product.id)) },
                        onQuantityChange = { qty ->
                            viewModel.onIntent(
                                if (qty > 0)
                                    CartIntent.ChangeQuantity(item.product.id, qty)
                                else
                                    CartIntent.RemoveFromCart(item.product.id)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Total: $ ${"%.2f".format(cartState.totalPrice)}", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.onIntent(CartIntent.ClearCart) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Empty Cart")
                }

                Button(
                    onClick = {
                        navToCheckout()
                    },
                    enabled = cartState.items.isNotEmpty()
                ) {
                    Text("Checkout")
                }
            }
        }
    }
}
