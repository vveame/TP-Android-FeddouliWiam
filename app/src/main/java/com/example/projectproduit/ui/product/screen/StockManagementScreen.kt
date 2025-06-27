package com.example.projectproduit.ui.product.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.product.component.StockItem
import androidx.compose.runtime.getValue
import com.example.projectproduit.ui.product.ProductIntent
import androidx.compose.foundation.lazy.items
import java.util.Date

@Composable
fun StockManagementScreen(
    viewModel: ProductViewModel,
    selectedCategory: String?,
    selectedBrand: String?
) {
    val state by viewModel.state.collectAsState()

    val filteredProducts = state.products.filter { product ->
        (selectedCategory == null || product.category == selectedCategory) &&
                (selectedBrand == null || product.brand == selectedBrand)
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ProductIntent.LoadProducts)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
            filteredProducts.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products match the selected filters")
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredProducts) { product ->
                        StockItem(
                            product = product,
                            onRestock = { amount ->
                                val newStock = product.stock + amount
                                viewModel.handleIntent(
                                    ProductIntent.RestockProduct(
                                        productId = product.id,
                                        newStock = newStock,
                                        restockDate = Date()
                                    )
                                )
                            },
                            onEmptyStock = {
                                viewModel.handleIntent(
                                    ProductIntent.EmptyStock(product.id)
                                )
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

