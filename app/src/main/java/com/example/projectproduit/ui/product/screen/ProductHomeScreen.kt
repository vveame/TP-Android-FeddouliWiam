package com.example.projectproduit.ui.product.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.product.ProductIntent
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.product.component.ProductList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projectproduit.ui.cart.CartViewModel

@Composable
fun ProductHomeScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    selectedCategory: String? = null,
    selectedBrand: String? = null,
    onNavigateToDetails: (String) -> Unit,
    modifier: Modifier
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(viewModel) {
        viewModel.handleIntent(ProductIntent.LoadProducts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        when {
            state.isLoading -> {
                // Display a Circular loader
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
            }

            state.error != null -> {
                // Display an error message
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, category, or brand...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    singleLine = true,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        cursorColor = MaterialTheme.colorScheme.secondary
                    )
                )

                val filteredProducts = state.products.filter { product ->
                    val matchesCategory = selectedCategory == null || selectedCategory == "All" || product.productCategory == selectedCategory
                    val matchesBrand = selectedBrand == null || selectedBrand == "All" || product.productBrand == selectedBrand
                    val matchesSearch = searchQuery.isBlank() || product.productTitle.contains(searchQuery, ignoreCase = true) ||
                            product.productCategory.contains(searchQuery, ignoreCase = true) ||
                            product.productBrand.contains(searchQuery, ignoreCase = true)

                    matchesCategory && matchesBrand && matchesSearch
                }

                ProductList(products = filteredProducts, onNavigateToDetails, cartViewModel)

                // Display products when fetch is success
                // ProductList(products = state.products, onNavigateToDetails)
            }
        }
    }
}