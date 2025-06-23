package com.example.projectproduit.ui.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.cart.CartIntent
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.product.ProductIntent
import com.example.projectproduit.ui.product.ProductViewModel

@Composable
fun ProductDetails(
    productId: String,
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Load product by ID once
    LaunchedEffect(productId) {
        viewModel.handleIntent(ProductIntent.LoadProductById(productId))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading -> {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
                )
            }

            state.error != null -> {
                Text(
                    text = "Error : ${state.error}",
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }

            state.selectedProduct != null -> {
                val product = state.selectedProduct!!

                // Image
                ProductImageSlider(
                    images = product.images.takeIf { it.isNotEmpty() } ?: listOf(product.thumbnail)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product details
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price with discount if available
                    val discount = product.discountPercentage ?: 0.0
                    val finalPrice = product.price * (1 - discount / 100)
                    Text(
                        text = "$ ${"%.2f".format(finalPrice)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (discount > 0) {
                        Text(
                            text = "(-${discount.toInt()}%)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stock info
                    Row {
                        Text(
                            text = "In stock : ",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = product.stock.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Optional description
                    product.description?.let { description ->
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Brand and category
                    Text(
                        text = "Brand : ${product.brand}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Category : ${product.category}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Rating if available
                    Text(
                        text = "⭐ %.1f".format(product.rating),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            cartViewModel.onIntent(CartIntent.AddToCart(product))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Add to cart")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to cart")
                    }
                }
            }
        }
    }
}
